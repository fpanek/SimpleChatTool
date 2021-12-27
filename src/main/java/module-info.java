module at.ac.fhcampuswien.simplechattool {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens at.ac.fhcampuswien.simplechattool to javafx.fxml;
    exports at.ac.fhcampuswien.simplechattool;
    exports at.ac.fhcampuswien.simplechattool.controller;
    opens at.ac.fhcampuswien.simplechattool.controller to javafx.fxml;
}