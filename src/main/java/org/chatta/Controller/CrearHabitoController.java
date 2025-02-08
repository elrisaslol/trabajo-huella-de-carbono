package org.chatta.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.chatta.Dao.HabitoDAO;
import org.chatta.Entities.Actividad;
import org.chatta.Entities.Habito;
import org.chatta.Entities.HabitoId;
import org.chatta.Entities.Tipo;
import org.chatta.Utils.SesionUsuario;

import java.time.Instant;
import java.util.List;

public class CrearHabitoController {

    @FXML
    private TextField txtFrecuencia;
    @FXML
    private ComboBox<Tipo> comboBoxTipo;
    @FXML
    private ComboBox<Actividad> comboActividad;

    private final HabitoDAO habitoDAO = new HabitoDAO();

    @FXML
    public void initialize() {
        comboBoxTipo.getItems().setAll(Tipo.values());

        List<Actividad> actividades = habitoDAO.obtenerTodasLasActividades();
        comboActividad.getItems().setAll(actividades);

        configurarComboActividad();
    }

    private void configurarComboActividad() {
        comboActividad.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Actividad> call(ListView<Actividad> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Actividad item, boolean empty) {
                        super.updateItem(item, empty);
                        setText((item != null) ? item.getNombre() : null);
                    }
                };
            }
        });

        comboActividad.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Actividad item, boolean empty) {
                super.updateItem(item, empty);
                setText((item != null) ? item.getNombre() : null);
            }
        });

        comboActividad.setConverter(new StringConverter<>() {
            @Override
            public String toString(Actividad actividad) {
                return (actividad != null) ? actividad.getNombre() : "";
            }

            @Override
            public Actividad fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void guardarHabito() {
        Tipo tipoSeleccionado = comboBoxTipo.getValue();
        Actividad actividadSeleccionada = comboActividad.getValue();
        int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId();

        if (actividadSeleccionada == null || tipoSeleccionado == null || txtFrecuencia.getText().isEmpty()) {
            System.out.println("Error: Debes completar todos los campos.");
            return;
        }

        int idActividad = actividadSeleccionada.getId();
        int frecuencia;
        try {
            frecuencia = Integer.parseInt(txtFrecuencia.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error: La frecuencia debe ser un número válido.");
            return;
        }

        Habito nuevoHabito = new Habito();
        nuevoHabito.setId(new HabitoId(idUsuario, idActividad));
        nuevoHabito.setIdActividad(actividadSeleccionada);
        nuevoHabito.setTipo(tipoSeleccionado.name());
        nuevoHabito.setFrecuencia(frecuencia);
        nuevoHabito.setUltimaFecha(Instant.now());

        habitoDAO.save(nuevoHabito);
        cerrarVentana();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) comboActividad.getScene().getWindow();
        stage.close();
    }
}
