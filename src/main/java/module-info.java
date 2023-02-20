module com.slukovskyi.airlinesui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires com.google.gson;
    requires java.sql;
    requires org.slf4j;

    opens com.slukovskyi.airlinesui to javafx.fxml;
    exports com.slukovskyi.airlinesui;
    exports com.slukovskyi.models;
    exports com.slukovskyi.models.planes;
}