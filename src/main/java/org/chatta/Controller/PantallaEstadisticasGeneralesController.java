package org.chatta.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.chatta.Dao.HuellaDAO;
import org.chatta.Entities.Huella;
import org.chatta.Utils.App;
import org.chatta.View.Scenes;

public class PantallaEstadisticasGeneralesController {

    @FXML
    private Button btnBack;

    @FXML
    private BarChart<String, Number> impactoChart;

    @FXML
    private Label resumenLabel;

    private HuellaDAO huellaDAO = new HuellaDAO();

    @FXML
    public void initialize() {
        // Calcular y mostrar el impacto de todas las huellas
        calcularYMostrarImpactoGenerales();
    }

    private void calcularYMostrarImpactoGenerales() {
        // Obtener todas las huellas de todos los usuarios
        List<Huella> huellas = huellaDAO.getAll();

        if (!huellas.isEmpty()) {
            // Agrupar las huellas por usuario y categoría
            Map<String, Map<String, BigDecimal>> impactoPorUsuarioYCategoria = calcularImpactoPorUsuarioYCategoria(huellas);

            // Llenar el gráfico con los datos calculados
            mostrarImpactoEnGrafico(impactoPorUsuarioYCategoria);

            // Mostrar resumen
            resumenLabel.setText("Impacto total estimado de todos los usuarios.");
        } else {
            resumenLabel.setText("No se encontraron huellas de carbono.");
        }
    }

    private Map<String, Map<String, BigDecimal>> calcularImpactoPorUsuarioYCategoria(List<Huella> huellas) {
        // Agrupar las huellas por usuario y categoría, y calcular el impacto total
        return huellas.stream()
                .collect(Collectors.groupingBy(
                        huella -> huella.getUsuario().getNombre(), // Agrupar por nombre de usuario
                        Collectors.groupingBy(
                                huella -> huella.getIdActividad().getIdCategoria().getNombre(), // Agrupar por categoría
                                Collectors.mapping(
                                        huella -> huella.getValor().multiply(huella.getIdActividad().getIdCategoria().getFactorEmision()),
                                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                                )
                        )
                ));
    }

    private void mostrarImpactoEnGrafico(Map<String, Map<String, BigDecimal>> impactoPorUsuarioYCategoria) {
        // Limpiar el gráfico antes de agregar nuevos datos
        impactoChart.getData().clear();

        // Iterar sobre los usuarios y categorías
        for (Map.Entry<String, Map<String, BigDecimal>> entryUsuario : impactoPorUsuarioYCategoria.entrySet()) {
            String usuario = entryUsuario.getKey();
            Map<String, BigDecimal> impactoPorCategoria = entryUsuario.getValue();

            // Crear una serie de datos para cada usuario
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(usuario);

            // Añadir los datos de impacto por categoría para el usuario
            for (Map.Entry<String, BigDecimal> entryCategoria : impactoPorCategoria.entrySet()) {
                series.getData().add(new XYChart.Data<>(entryCategoria.getKey(), entryCategoria.getValue()));
            }

            // Agregar la serie de datos al gráfico
            impactoChart.getData().add(series);
        }
    }

    @FXML
    public void switchToCrud(ActionEvent event) throws IOException {
        App.setRoot(Scenes.PANTALLACRUD);
    }
}
