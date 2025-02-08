package org.chatta.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import org.chatta.Dao.HuellaDAO;
import org.chatta.Entities.Recomendacion;
import org.chatta.Utils.App;
import org.chatta.Utils.SesionUsuario;
import org.chatta.View.Scenes;

import java.io.IOException;
import java.math.BigDecimal;

public class PantallaTipsController {

    @FXML
    private Label recomendacionLabel;

    @FXML
    private Label descripcionLabel;

    @FXML
    private Label impactoLabel;

    @FXML
    private Button btnBack;

    private HuellaDAO huellaDAO = new HuellaDAO();
    private int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId(); // Este ID debe ser obtenido de la sesión o del usuario logueado

    public void initialize() {
        // Obtener la recomendación más relevante basada en la categoría más repetida
        Recomendacion recomendacion = huellaDAO.obtenerTipDeCategoriaMasRepetida(idUsuario);

        if (recomendacion != null) {
            // Actualizar las etiquetas con la información de la recomendación
            recomendacionLabel.setText("Recomendación para la categoría más repetida:");
            descripcionLabel.setText(recomendacion.getDescripcion());

            // Asumiendo que getImpactoEstimado() y getFactorEmision() devuelven BigDecimal
            BigDecimal impactoEstimado = recomendacion.getImpactoEstimado();
            BigDecimal factorEmision = recomendacion.getIdCategoria().getFactorEmision();

// Realizar la multiplicación
            BigDecimal impactoFinal = impactoEstimado.multiply(factorEmision);

            // Mostrar el resultado en el Label
            impactoLabel.setText("Impacto estimado: " + impactoFinal.toString() + " kgCO2e");
        } else {
            // Si no hay recomendación, mostramos un mensaje
            recomendacionLabel.setText("No se ha encontrado una recomendación.");
            descripcionLabel.setText("");
            impactoLabel.setText("");
        }
    }

    @FXML
    private void switchToCrud() throws IOException {
        // Cambiar a la pantalla principal (ajustar según sea necesario)
        App.setRoot(Scenes.PANTALLACRUD);
    }
}
