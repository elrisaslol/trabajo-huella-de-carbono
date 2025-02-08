package org.chatta.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.chatta.Utils.App;
import org.chatta.View.Scenes;

import java.io.IOException;

public class PantalladeCreditosController {
    @FXML
    public Button btnBack;


    @FXML
    private void switchToPrincipal() throws IOException {
        App.setRoot(Scenes.PantallaPrincipal);
    }

}