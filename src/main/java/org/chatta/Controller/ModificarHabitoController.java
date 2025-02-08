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
import org.chatta.Dao.HuellaDAO;
import org.chatta.Entities.Actividad;
import org.chatta.Entities.Habito;
import org.chatta.Entities.HabitoDTO;
import org.chatta.Entities.Tipo;

import java.time.Instant;
import java.util.List;

public class ModificarHabitoController {

    @FXML
    private TextField  txtFrecuencia;
    @FXML
    private ComboBox<Tipo> comboBoxTipo;
    @FXML
    private ComboBox<Actividad> comboActividad;

    private Habito habito;
    private final HabitoDAO habitoDAO = new HabitoDAO();


    public void setHabito(HabitoDTO habitoDTO, int idUsuario) {
        this.habito = habitoDAO.getById(idUsuario, habitoDTO.getIdActividad());
        if (this.habito != null) {
            comboBoxTipo.getItems().setAll(Tipo.values());
            comboBoxTipo.setValue(Tipo.valueOf(habito.getTipo()));
            txtFrecuencia.setText(String.valueOf(habito.getFrecuencia()));
            List<Actividad> actividades = habitoDAO.obtenerTodasLasActividades();
            comboActividad.getItems().setAll(actividades);

            comboActividad.setValue(habito.getIdActividad());

            comboActividad.setCellFactory(new Callback<ListView<Actividad>, ListCell<Actividad>>() {
                @Override
                public ListCell<Actividad> call(ListView<Actividad> param) {
                    return new ListCell<Actividad>() {
                        @Override
                        protected void updateItem(Actividad item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setText(item.getNombre());
                            } else {
                                setText(null);
                            }
                        }
                    };
                }
            });

            comboActividad.setButtonCell(new ListCell<Actividad>() {
                @Override
                protected void updateItem(Actividad item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getNombre());
                    } else {
                        setText(null);
                    }
                }
            });

            comboActividad.setConverter(new StringConverter<Actividad>() {
                @Override
                public String toString(Actividad actividad) {
                    return actividad != null ? actividad.getNombre() : "";
                }

                @Override
                public Actividad fromString(String string) {
                    return null;
                }
            });
        }
    }

    @FXML
    private void guardarCambios() {
        if (habito != null) {
            Tipo tipoSeleccionado = comboBoxTipo.getValue();
            int idUsuario = habito.getId().getIdUsuario();
            int idActividadAntigua = habito.getId().getIdActividad();
            int idActividadNueva = comboActividad.getValue().getId();
            Integer frecuencia = Integer.parseInt(txtFrecuencia.getText());
            String tipo = String.valueOf(tipoSeleccionado);
            Instant ultimaFecha = habito.getUltimaFecha();


            habito.setFrecuencia(Integer.parseInt(txtFrecuencia.getText()));
            habito.setTipo(String.valueOf(tipoSeleccionado));

            if (idActividadAntigua != idActividadNueva) {
                habitoDAO.updateHabitoConNuevaActividad(idUsuario,idActividadAntigua, idActividadNueva, frecuencia, tipo, ultimaFecha);
            }
            habitoDAO.update(habito);
            cerrarVentana();
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) comboActividad.getScene().getWindow();
        stage.close();
    }
}
