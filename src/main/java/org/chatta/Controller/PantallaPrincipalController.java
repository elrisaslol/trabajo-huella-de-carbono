package org.chatta.Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.chatta.Utils.App;
import org.chatta.View.Scenes;

public class PantallaPrincipalController {

    @FXML
    public Button btnLogin;
    @FXML
    public Button btnRegister;
    @FXML
    public Button btnCredits;


    @FXML
    private void switchToIdentificacion() throws IOException {
        App.setRoot(Scenes.PANTALLADEIDENTIFICACION);
    }

    @FXML
    private void switchToRegistro() throws IOException {
        App.setRoot(Scenes.PANTALLADEREGISTRO);
    }

    @FXML
    private void switchToCreditos() throws IOException {
        App.setRoot(Scenes.PANTALLADECREDITOS);
    }
}

