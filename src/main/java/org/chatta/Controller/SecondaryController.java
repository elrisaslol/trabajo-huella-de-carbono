package org.chatta.Controller;

import java.io.IOException;
import javafx.fxml.FXML;
import org.chatta.Utils.App;
import org.chatta.View.Scenes;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot(Scenes.PantallaPrincipal);
    }
}