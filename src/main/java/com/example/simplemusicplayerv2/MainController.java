package com.example.simplemusicplayerv2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

public class MainController{


    private DoublyLinkedList playlist;

    @FXML
    private ListView<Song> songListView;

    @FXML
    private ListView<Song> historyListView;

    @FXML
    private Label songPlaying;

    @FXML
    private void handlePlayButton() {
        playlist.playCurrentSong();
    }

    @FXML
    private void handlePauseButton() {
        playlist.pauseCurrentSong();
    }

    @FXML
    private void handleNextButton() {
        playlist.playNextSong();
    }

    @FXML
    private void handlePrevButton() {
        playlist.playPreviousSong();
    }


    @FXML
    private void handleRefreshButton(){
        updateHistoryListView();
    }

    @FXML
    private void handleShuffleButton(){
        playlist.shufflePlaylist();
    }

    @FXML
    private ToggleButton toggleShuffleButton;

    // ... existing code ...

    @FXML
    private void handleToggleShuffleButton(ActionEvent event) {
        playlist.toggleShuffle();
        updateShuffleButtonState();
    }


    private void updateShuffleButtonState() {
        // Optionally update UI to reflect shuffle state
        boolean shuffleEnabled = playlist.isShuffleEnabled();
        String buttonText = shuffleEnabled ? "Shuffle: ON" : "Shuffle: OFF";
        toggleShuffleButton.setText(buttonText);
    }


    public void setSongLabel(String song){
        songPlaying.setText(song);
    }






    public void setPlaylist(DoublyLinkedList playlist) {
        this.playlist = playlist;
        updateSongListView();
    }


    private void updateSongListView() {
        songListView.getItems().clear();
        Node currentNode = playlist.getHead();

        while (currentNode != null) {
            songListView.getItems().add(currentNode.getData());
            currentNode = currentNode.getNext();
        }
    }

    @FXML
    private void handleSongClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            clickSetNodes();
            playlist.playCurrentSong();
        } else if (event.getClickCount() == 1) {
            clickSetNodes();
        }
    }

    private void clickSetNodes(){
        Song selectedSong = songListView.getSelectionModel().getSelectedItem();
        Node n = playlist.findNode(selectedSong);
        System.out.println(selectedSong + "\t" + n);
        playlist.setHead(n);
    }


    private void updateHistoryListView() {
        historyListView.getItems().clear();
        Node currentHistoryNode = playlist.getHistoryHead();

        while (currentHistoryNode != null) {
            historyListView.getItems().add(currentHistoryNode.getData());
            currentHistoryNode = currentHistoryNode.getNext();
        }
    }


    @FXML
    private void handleHistoryDelete(){
        Song selectedSong = historyListView.getSelectionModel().getSelectedItem();
        if (selectedSong != null){
            System.out.println("your selected song to delete: " + selectedSong);
            playlist.removeFromHistory(selectedSong);
            updateHistoryListView();
        }
        else{
            System.out.println("No song selected!");
        }
    }

}
