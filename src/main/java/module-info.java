module org.example.student_record_tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires json.simple;
    requires com.google.gson;
    requires java.desktop;
    requires zxcvbn;

    opens org.example.student_record_tracker to javafx.fxml, com.google.gson;

    exports org.example.student_record_tracker;
}
