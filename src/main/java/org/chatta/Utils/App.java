package org.chatta.Utils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.chatta.View.Scenes;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Object currentController; // Variable para almacenar el controlador actual

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML(Scenes.PantallaPrincipal), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(Scenes url) throws IOException {
        Parent root = loadFXML(url);
        scene.setRoot(root);
    }

    // Método para cargar el FXML y almacenar el controlador
    private static Parent loadFXML(Scenes url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(url.getURL()));
        Parent parent = fxmlLoader.load(); // Carga el FXML
        currentController = fxmlLoader.getController(); // Guarda el controlador
        return parent;
    }

    // Método para obtener el controlador actual
    public static Object getCurrentController() {
        return currentController; // Retorna el controlador almacenado
    }

    public static void begin() {
        launch();
    }
}
