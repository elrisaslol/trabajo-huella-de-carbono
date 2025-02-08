module org.chatta {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;  // Asegúrate de incluir este módulo
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires kernel;
    requires layout;
    requires java.desktop;
    requires io;

    exports org.chatta.Controller;
    opens org.chatta.Controller to javafx.fxml;
    exports org.chatta.Utils;
    opens org.chatta.Utils to javafx.fxml;
    exports org.chatta.Connection;
    opens org.chatta.Connection to javafx.fxml;
    exports org.chatta.Entities;
    opens org.chatta.Entities to javafx.fxml, org.hibernate.orm.core;
    exports org.chatta.View;
    opens org.chatta.View to javafx.fxml;
}
