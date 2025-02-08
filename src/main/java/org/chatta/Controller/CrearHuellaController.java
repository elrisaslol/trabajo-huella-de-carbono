package org.chatta.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.chatta.Dao.HuellaDAO;
import org.chatta.Entities.Actividad;
import org.chatta.Entities.Huella;
import org.chatta.Entities.HuellaDTO;
import org.chatta.Entities.Usuario;
import org.chatta.Utils.SesionUsuario;
import org.chatta.Entities.Unidad;

import java.math.BigDecimal;
import java.util.List;

public class CrearHuellaController {

    @FXML
    private ComboBox<Actividad> comboActividad; // ComboBox de Actividad
    @FXML
    private ComboBox<Unidad> comboBoxUnidad;
    @FXML
    private TextField txtValor;

    private final HuellaDAO huellaDAO = new HuellaDAO();

    public void initialize() {
        // Cargar las actividades en el ComboBox
        List<Actividad> actividades = huellaDAO.obtenerTodasLasActividades();
        comboActividad.getItems().setAll(actividades);
        comboBoxUnidad.getItems().setAll(Unidad.values());
        // Configurar el ComboBox para mostrar el nombre de la actividad
        comboActividad.setCellFactory(new Callback<ListView<Actividad>, ListCell<Actividad>>() {
            @Override
            public ListCell<Actividad> call(ListView<Actividad> param) {
                return new ListCell<Actividad>() {
                    @Override
                    protected void updateItem(Actividad item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getNombre()); // Mostrar solo el nombre de la actividad
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        // Configurar cómo se verá el elemento seleccionado en el ComboBox
        comboActividad.setButtonCell(new ListCell<Actividad>() {
            @Override
            protected void updateItem(Actividad item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getNombre()); // Mostrar solo el nombre de la actividad
                } else {
                    setText(null);
                }
            }
        });

        // Usar un StringConverter para mostrar el nombre de la actividad en lugar de la entidad
        comboActividad.setConverter(new StringConverter<Actividad>() {
            @Override
            public String toString(Actividad actividad) {
                return actividad != null ? actividad.getNombre() : "";
            }

            @Override
            public Actividad fromString(String string) {
                // Este método se usa cuando el ComboBox está en un estado de texto (no seleccionando un elemento)
                return null;
            }
        });
    }

    @FXML
    private void guardarNuevo() {
        // Crear una nueva huella
        Huella nuevaHuella = new Huella();
        Usuario idUsuario = SesionUsuario.getInstancia().getUsuarioActual();

        // Obtener la actividad seleccionada
        Actividad actividadSeleccionada = comboActividad.getValue();
        Unidad unidadSeleccionada = comboBoxUnidad.getValue();


        if (actividadSeleccionada != null) {
            nuevaHuella.setUsuario(idUsuario);
            nuevaHuella.setIdActividad(actividadSeleccionada);
            nuevaHuella.setValor(new BigDecimal(txtValor.getText()));
            nuevaHuella.setUnidad(String.valueOf(unidadSeleccionada));
            nuevaHuella.setFecha(java.time.Instant.now());
            // Insertar la nueva huella en la base de datos
            huellaDAO.save(nuevaHuella);

            // Cerrar la ventana
            cerrarVentana();
        } else {
            // Mostrar un mensaje de error si no se ha seleccionado ninguna actividad
            System.out.println("Por favor, selecciona una actividad");
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) comboActividad.getScene().getWindow();
        stage.close();
    }
}
