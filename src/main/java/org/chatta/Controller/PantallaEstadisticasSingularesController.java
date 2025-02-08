package org.chatta.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.util.StringConverter;
import org.chatta.Dao.HuellaDAO;
import org.chatta.Entities.Huella;
import org.chatta.Utils.App;
import org.chatta.Utils.SesionUsuario;
import org.chatta.View.Scenes;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class PantallaEstadisticasSingularesController {

    @FXML
    private BarChart<String, Number> impactoChart;

    @FXML
    private Label resumenLabel;

    @FXML
    private ComboBox<Huella> huellaComboBox;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnPrinttoPdf;

    private HuellaDAO huellaDAO = new HuellaDAO();
    private int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId(); // ID del usuario actual

    public void initialize() {
        // Cargar todas las huellas del usuario en el ComboBox
        cargarHuellasEnComboBox();
    }

    private void cargarHuellasEnComboBox() {
        // Obtener todas las huellas del usuario
        List<Huella> huellas = huellaDAO.getAll();

        // Filtrar las huellas por usuario
        huellaComboBox.getItems().clear();
        huellaComboBox.getItems().addAll(huellas.stream()
                .filter(huella -> huella.getUsuario().getId() == idUsuario)
                .toList());

        // Establecer un StringConverter para mostrar el nombre de la actividad
        huellaComboBox.setConverter(new StringConverter<Huella>() {
            @Override
            public String toString(Huella huella) {
                if (huella == null) {
                    return "";
                }
                return huella.getIdActividad().getNombre(); // Mostrar el nombre de la actividad
            }

            @Override
            public Huella fromString(String string) {
                return null; // No necesitamos este método en este caso
            }
        });
    }

    @FXML
    private void handleHuellaSelection(ActionEvent event) {
        // Obtener la huella seleccionada
        Huella selectedHuella = huellaComboBox.getSelectionModel().getSelectedItem();

        if (selectedHuella != null) {
            // Calcular y mostrar el impacto solo para la huella seleccionada
            calcularYMostrarImpacto(selectedHuella);
        }
    }

    private void calcularYMostrarImpacto(Huella huellaSeleccionada) {
        // Obtener el impacto de la huella seleccionada
        BigDecimal impacto = huellaSeleccionada.getValor()
                .multiply(huellaSeleccionada.getIdActividad().getIdCategoria().getFactorEmision());

        // Limpiar el gráfico antes de agregar nuevos datos
        impactoChart.getData().clear();

        // Crear una serie de datos para el gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Impacto Ambiental");

        // Añadir los datos al gráfico
        series.getData().add(new XYChart.Data<>(huellaSeleccionada.getIdActividad().getIdCategoria().getNombre(), impacto));

        // Agregar la serie al gráfico
        impactoChart.getData().add(series);

        // Mostrar el resumen del impacto
        resumenLabel.setText("Impacto estimado para la huella seleccionada: " + impacto.toString() + " kgCO2e");
    }

    @FXML
    private void switchToCrud() throws IOException {
        // Cambiar a la pantalla principal
        App.setRoot(Scenes.PANTALLACRUD);
    }

    @FXML
    public void printToPdf() throws IOException {
        // Implementar la lógica para imprimir a PDF
    }
}
