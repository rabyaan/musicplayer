package com.example.simplemusicplayerv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Parent root = fxmlLoader.load();


        // Load songs from the specified directory
        DoublyLinkedList playlist = new DoublyLinkedList();
        String songsFolderPath = "src/main/songs"; // Replace with the actual path to your "songs" folder
        playlist.loadSongsFromDirectory(songsFolderPath);

        // Pass the playlist to the controller
        MainController mainController = fxmlLoader.getController();
        mainController.setPlaylist(playlist);
        playlist.setMainController(mainController);  // Add this line


        Scene scene = new Scene(root);
        stage.setTitle("Simple Music Player");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
