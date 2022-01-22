module at.ac.fhcampuswien.simplechattool {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens at.ac.fhcampuswien.simplechattool to javafx.fxml;
    exports at.ac.fhcampuswien.simplechattool;
}