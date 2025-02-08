package org.chatta.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.chatta.Dao.HuellaDAO;
import org.chatta.Entities.Huella;
import org.chatta.Utils.App;
import org.chatta.Utils.SesionUsuario;
import org.chatta.View.Scenes;

public class PantallaEstadisticasController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnPrinttoPdf;

    @FXML
    private BarChart<String, Number> impactoChart;

    @FXML
    private Label resumenLabel;

    private HuellaDAO huellaDAO = new HuellaDAO();
    private int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId(); // ID del usuario actual

    @FXML
    private void switchToCrud() throws IOException {
        App.setRoot(Scenes.PANTALLACRUD);
    }

    public void switchToEstadisticasSingulares() throws IOException {
        App.setRoot(Scenes.PANTALLAESTADISTICASSINGULARES);
    }

    public void switchToEstadisticasGenerales() throws IOException {
        App.setRoot(Scenes.PANTALLAESTADISTICASGENERALES);
    }


    @FXML
    public void printToPdf() throws IOException {
        // Crear un archivo PDF
        String pdfPath = "grafico_impacto.pdf";
        File pdfFile = new File(pdfPath);
        FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

        // Crear un documento PDF
        PdfDocument pdfDocument = new PdfDocument(new com.itextpdf.kernel.pdf.PdfWriter(fileOutputStream));
        Document document = new Document(pdfDocument);

        // Capturar la imagen del gráfico
        WritableImage image = impactoChart.snapshot(null, null);

        // Convertir la WritableImage a BufferedImage
        BufferedImage bufferedImage = convertToBufferedImage(image);

        // Crear un archivo temporal para guardar la imagen
        File tempFile = File.createTempFile("chart", ".png");
        ImageIO.write(bufferedImage, "PNG", tempFile);

        // Agregar la imagen al PDF
        Image pdfImage = new Image(ImageDataFactory.create(tempFile.getAbsolutePath()));
        document.add(new Paragraph("Impacto Ambiental del Usuario"));
        document.add(pdfImage);

        // Agregar un resumen del impacto al PDF
        document.add(new Paragraph(resumenLabel.getText()));

        // Cerrar el documento
        document.close();

        // Mostrar mensaje de éxito
        System.out.println("El gráfico se ha guardado correctamente como PDF en " + pdfPath);
    }

    @FXML
    public void initialize() {
        // Cargar y mostrar el impacto ambiental
        calcularYMostrarImpacto();
    }

    private void calcularYMostrarImpacto() {
        // Obtener todas las huellas del usuario
        List<Huella> huellas = huellaDAO.getAll();

        // Filtrar las huellas por usuario
        List<Huella> huellasUsuario = huellas.stream()
                .filter(huella -> huella.getUsuario().getId() == idUsuario)
                .collect(Collectors.toList());

        if (!huellasUsuario.isEmpty()) {
            // Crear un mapa para almacenar el impacto por categoría
            Map<String, BigDecimal> impactoPorCategoria = calcularImpactoPorCategoria(huellasUsuario);

            // Llenar el gráfico con los datos calculados
            mostrarImpactoEnGrafico(impactoPorCategoria);

            // Mostrar resumen
            resumenLabel.setText("Impacto total estimado: " + calcularImpactoTotal(impactoPorCategoria) + " kgCO2e");
        } else {
            resumenLabel.setText("No se encontraron huellas de carbono.");
        }
    }

    private Map<String, BigDecimal> calcularImpactoPorCategoria(List<Huella> huellasUsuario) {
        // Agrupar las huellas por categoría y calcular el impacto total
        return huellasUsuario.stream()
                .collect(Collectors.groupingBy(
                        huella -> huella.getIdActividad().getIdCategoria().getNombre(),
                        Collectors.mapping(huella -> huella.getValor().multiply(huella.getIdActividad().getIdCategoria().getFactorEmision()),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

    private void mostrarImpactoEnGrafico(Map<String, BigDecimal> impactoPorCategoria) {
        // Limpiar el gráfico antes de agregar nuevos datos
        impactoChart.getData().clear();

        // Crear una serie de datos para el gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Impacto Ambiental por Categoría");

        // Añadir los datos al gráfico
        for (Map.Entry<String, BigDecimal> entry : impactoPorCategoria.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Agregar la serie de datos al gráfico
        impactoChart.getData().add(series);
    }

    private BigDecimal calcularImpactoTotal(Map<String, BigDecimal> impactoPorCategoria) {
        // Sumar todos los impactos por categoría para obtener el impacto total
        return impactoPorCategoria.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Método para convertir WritableImage a BufferedImage
    private BufferedImage convertToBufferedImage(WritableImage writableImage) {
        // Crear un BufferedImage con las dimensiones de la WritableImage
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Obtener un PixelReader de la WritableImage
        PixelReader pixelReader = writableImage.getPixelReader();

        // Recorrer los píxeles y transferirlos al BufferedImage
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = pixelReader.getColor(x, y);
                int argb = (int) (color.getOpacity() * 255) << 24 | (int) (color.getRed() * 255) << 16 |
                        (int) (color.getGreen() * 255) << 8 | (int) (color.getBlue() * 255);
                bufferedImage.setRGB(x, y, argb);
            }
        }

        return bufferedImage;
    }


}
