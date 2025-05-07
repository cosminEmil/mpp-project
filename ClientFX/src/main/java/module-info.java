module com.clientfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.model;

    requires MPP.project.Networking.main;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.services;

    opens com.clientfx;
    exports com.clientfx;
    exports com.clientfx.gui;
    opens com.clientfx.gui;
}