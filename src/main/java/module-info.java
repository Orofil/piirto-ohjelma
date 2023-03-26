module com.example.piirto {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.desktop;


    opens com.example.piirto to javafx.fxml;
    exports com.example.piirto;
}