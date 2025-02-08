package org.chatta.View;

public enum Scenes {

    PantallaPrincipal("/org/chatta/View/PantallaPrincipal.fxml"),
    PantallaSecundaria("/org/chatta/View/PantallaSecundaria.fxml"),
    PANTALLADEIDENTIFICACION("/org/chatta/View/PantalladeIdentificacion.fxml"),
    PANTALLADEREGISTRO("/org/chatta/View/PantalladeRegistro.fxml"),
    PANTALLADECREDITOS("/org/chatta/View/PantalladeCreditos.fxml"),
    PANTALLACRUD("/org/chatta/View/PantallaCrud.fxml"),
    PANTALLAESTADISTICAS("/org/chatta/View/PantallaEstadisticas.fxml"),
    PANTALLATIPS("/org/chatta/View/PantallaTips.fxml"),
    PANTALLAESTADISTICASSINGULARES("/org/chatta/View/estadisticassingulares.fxml"),
    PANTALLAESTADISTICASGENERALES("/org/chatta/View/pantalla_estadisticas_generales.fxml");


    private String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }

}
