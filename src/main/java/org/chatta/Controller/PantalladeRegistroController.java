package org.chatta.Controller;

import java.io.IOException;
import java.time.Instant;
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

public class PantalladeRegistroController {

    @FXML
    public TextField txtUsuario;
    @FXML
    public TextField txtEmail;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public PasswordField txtConfirmPassword;
    @FXML
    public Button btnRegister;
    @FXML
    public Button btnBack;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void registrarUsuario() throws IOException {
        String nombre = txtUsuario.getText().trim();
        String email = txtEmail.getText().trim();
        String contraseña = txtPassword.getText().trim();
        String confirmarContraseña = txtConfirmPassword.getText().trim();

        // 1️⃣ VALIDACIONES
        if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", AlertType.ERROR);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            mostrarAlerta("Error", "Ingresa un email válido.", AlertType.ERROR);
            return;
        }
        if (contraseña.length() < 6) {
            mostrarAlerta("Error", "La contraseña debe tener al menos 6 caracteres.", AlertType.ERROR);
            return;
        }
        if (!contraseña.equals(confirmarContraseña)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden.", AlertType.ERROR);
            return;
        }

        // 2️⃣ CREAR USUARIO Y GUARDAR EN BASE DE DATOS
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setContraseña(contraseña);
        nuevoUsuario.setFechaRegistro(Instant.now());

        usuarioDAO.save(nuevoUsuario);

        // 3️⃣ MENSAJE DE ÉXITO Y LIMPIEZA DE CAMPOS
        mostrarAlerta("Éxito", "Usuario registrado correctamente.", AlertType.INFORMATION);
        limpiarCampos();

        // 4️⃣ GUARDAR EL USUARIO EN EL SINGLETON, MOSTRAR MENSAJE CON EL USUARIO DEL SINGLETON Y CAMBIAR DE PANTALLA
        SesionUsuario.getInstancia().setUsuarioActual(nuevoUsuario);
        mostrarAlerta("Usuario actual", SesionUsuario.getInstancia().getUsuarioActual().getNombre(), AlertType.INFORMATION);
        System.out.println(SesionUsuario.getInstancia().getUsuarioActual().getNombre());

        switchToCrud();
    }

    // Función para mostrar pop-ups de error o confirmación
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Función para limpiar los campos después del registro exitoso
    private void limpiarCampos() {
        txtUsuario.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtConfirmPassword.clear();
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
}
