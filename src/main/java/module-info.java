module com.example.piirto {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.piirto to javafx.fxml;
    exports com.example.piirto;
}