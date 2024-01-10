module com.example.simplemusicplayerv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.simplemusicplayerv2 to javafx.fxml;
    exports com.example.simplemusicplayerv2;
}