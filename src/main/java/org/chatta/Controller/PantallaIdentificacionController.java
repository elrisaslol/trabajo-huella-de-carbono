package org.chatta.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import org.chatta.Utils.App;
import org.chatta.Utils.SesionUsuario;
import org.chatta.View.Scenes;
import org.chatta.Dao.UsuarioDAO;
import org.chatta.Entities.Usuario;

import java.io.IOException;

public class PantallaIdentificacionController {

    @FXML
    public TextField txtUsuario;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Button btnLogin;
    @FXML
    public Button btnBack;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void iniciarSesion() throws IOException {
        String nombre = txtUsuario.getText().trim();
        String contraseña = txtPassword.getText().trim();

        // 1️⃣ VALIDACIONES
        if (nombre.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Error", "Por favor, completa todos los campos.", AlertType.ERROR);
            return;
        }

        // 2️⃣ BUSCAR USUARIO EN LA BASE DE DATOS
        Usuario usuario = usuarioDAO.getByNombre(nombre);
        if (usuario == null || !usuario.getContraseña().equals(contraseña)) {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.", AlertType.ERROR);
            return;
        }

        // 3️⃣ GUARDAR EL USUARIO EN EL SINGLETON, MOSTRAR MENSAJE CON EL USUARIO DEL SINGLETON Y CAMBIAR DE PANTALLA
        SesionUsuario.getInstancia().setUsuarioActual(usuario);
        mostrarAlerta("Éxito", "Inicio de sesión exitoso.", AlertType.INFORMATION);
        mostrarAlerta("Usuario actual", SesionUsuario.getInstancia().getUsuarioActual().getNombre(), AlertType.INFORMATION);
        System.out.println(SesionUsuario.getInstancia().getUsuarioActual().getNombre());

        switchToCrud();
    }

    // Botón para regresar a la pantalla principal
    @FXML
    private void switchToPrincipal() throws IOException {
        App.setRoot(Scenes.PantallaPrincipal);
    }

    @FXML
    private void switchToCrud() throws IOException {
        App.setRoot(Scenes.PANTALLACRUD);
    }

    // Función para mostrar pop-ups
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
