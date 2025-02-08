package org.chatta.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.chatta.Dao.HabitoDAO;
import org.chatta.Dao.HuellaDAO;
import org.chatta.Entities.Habito;
import org.chatta.Entities.HabitoDTO;
import org.chatta.Entities.Huella;
import org.chatta.Entities.HuellaDTO;
import org.chatta.Utils.App;
import org.chatta.Utils.SesionUsuario;
import org.chatta.View.Scenes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PantallaCrudController {

    @FXML
    private TableView<Object> tablaDatos;
    @FXML
    private TableColumn<Object, Instant> colFecha;
    @FXML
    private TableColumn<Object, String> colActividad;
    @FXML
    private TableColumn<Object, String> colValorTipo;
    @FXML
    private TableColumn<Object, String> colUnidadFrecuencia;

    @FXML
    public Button btnCrearHuellas, btnModificarHuellas, btnBorrarHuellas;
    @FXML
    public Button btnCrearHabitos, btnModificarHabitos, btnBorrarHabitos;
    @FXML
    public Button btnBack, btnEstadisticas, btnTips, btnMostrarHuellas, btnMostrarHabitos;

    private final HuellaDAO huellaDAO = new HuellaDAO();
    private final HabitoDAO habitoDAO = new HabitoDAO();
    private boolean mostrandoHuellas = true;

    @FXML
    private void initialize() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha")); // ✅ Para HuellaDTO
        colActividad.setCellValueFactory(new PropertyValueFactory<>("nombreActividad"));
        colValorTipo.setCellValueFactory(new PropertyValueFactory<>("valor")); // ✅ Para HuellaDTO
        colUnidadFrecuencia.setCellValueFactory(new PropertyValueFactory<>("unidad")); // ✅ Para HuellaDTO

        mostrarHuellas();
    }

    @FXML
    private void mostrarHuellas() {
        mostrandoHuellas = true;
        int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId();
        List<Object[]> resultados = huellaDAO.getHuellasConNombreActividad(idUsuario);

        ObservableList<Object> huellasObservable = FXCollections.observableArrayList();
        for (Object[] row : resultados) {
            huellasObservable.add(new HuellaDTO(
                    (Integer) row[0], (String) row[1], (BigDecimal) row[2], (String) row[3], (Instant) row[4]));
        }

        colFecha.setText("Fecha");
        colValorTipo.setText("Valor");
        colUnidadFrecuencia.setText("Unidad");

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colValorTipo.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colUnidadFrecuencia.setCellValueFactory(new PropertyValueFactory<>("unidad"));

        tablaDatos.setItems(huellasObservable);
    }

    @FXML
    private void mostrarHabitos() {
        mostrandoHuellas = false;
        int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId();
        List<Object[]> resultados = habitoDAO.getHabitosConNombreActividad(idUsuario);

        ObservableList<Object> habitosObservable = FXCollections.observableArrayList();
        for (Object[] row : resultados) {
            habitosObservable.add(new HabitoDTO(
                    (Integer) row[0], (String) row[1], (Integer) row[2], (String) row[3], (Instant) row[4]));
        }

        colFecha.setText("Última Fecha");
        colValorTipo.setText("Tipo");
        colUnidadFrecuencia.setText("Frecuencia");

        colFecha.setCellValueFactory(new PropertyValueFactory<>("ultimaFecha"));
        colValorTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colUnidadFrecuencia.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));

        tablaDatos.setItems(habitosObservable);
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot(Scenes.PantallaPrincipal);
    }

    @FXML
    private void switchToEstadisticas() throws IOException {
        App.setRoot(Scenes.PANTALLAESTADISTICAS);
    }

    @FXML
    private void switchToTips() throws IOException {
        App.setRoot(Scenes.PANTALLATIPS);
    }

    public void crearHuellas(ActionEvent actionEvent) {
        try {
            // Cargar el formulario de creación de huella
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/chatta/View/crearHuellas.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Crear Nueva Huella");
            stage.setScene(new Scene(loader.load()));

            // Mostrar la ventana modal
            stage.showAndWait();

            // Refrescar la tabla de huellas
            mostrarHuellas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void modificarHuellas(ActionEvent actionEvent) {
        HuellaDTO huellaSeleccionada = (HuellaDTO) tablaDatos.getSelectionModel().getSelectedItem();

        if (huellaSeleccionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/chatta/View/modificarHuellas.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Modificar Huella");
                stage.setScene(new Scene(loader.load()));

                ModificarHuellaController controller = loader.getController();
                controller.setHuella(huellaSeleccionada);

                stage.showAndWait();
                mostrarHuellas();  // Refrescar la tabla después de modificar
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void borrarHuellas(ActionEvent actionEvent) {
        HuellaDTO huellaSeleccionada = (HuellaDTO) tablaDatos.getSelectionModel().getSelectedItem();

        if (huellaSeleccionada != null) {
            Huella huella = huellaDAO.getById(huellaSeleccionada.getId());
            if (huella != null){
                huellaDAO.delete(huella);  // Eliminar la huella de la base de datos

                mostrarHuellas();  // Actualizar la tabla
            }
        }
    }

    public void crearHabitos(ActionEvent actionEvent) {
        try {
            // Cargar el formulario de creación de huella
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/chatta/View/crearHabitos.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Crear Nuevo Hábito");
            stage.setScene(new Scene(loader.load()));

            // Mostrar la ventana modal
            stage.showAndWait();


            mostrarHabitos();  // Refrescar la tabla después de modificar

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void modificarHabitos(ActionEvent actionEvent) {
        HabitoDTO habitoSeleccionado = (HabitoDTO) tablaDatos.getSelectionModel().getSelectedItem();
        int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId();

        if (habitoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/chatta/View/modificarHabitos.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Modificar Hábito");
                stage.setScene(new Scene(loader.load()));

                ModificarHabitoController controller = loader.getController();
                controller.setHabito(habitoSeleccionado, idUsuario);

                stage.showAndWait();
                mostrarHabitos();  // Refrescar la tabla después de modificar
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void borrarHabitos(ActionEvent actionEvent) {
        HabitoDTO habitoSeleccionado = (HabitoDTO) tablaDatos.getSelectionModel().getSelectedItem();
        int idUsuario = SesionUsuario.getInstancia().getUsuarioActual().getId();
        if (habitoSeleccionado != null) {

            Habito habito = habitoDAO.getById(idUsuario,habitoSeleccionado.getIdActividad());

            if (habito != null){
                habitoDAO.delete(habito);
                mostrarHabitos();
            }

        }
    }
}
