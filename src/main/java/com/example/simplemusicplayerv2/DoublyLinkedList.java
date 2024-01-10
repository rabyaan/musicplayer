package com.example.simplemusicplayerv2;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Random;


public class DoublyLinkedList {
    private Node head;
    private Node tail;

    private Node historyHead; // Head of the history queue
    private Node historyTail; // Tail of the history queue

    private Node originalHead; // Head of the original playlist
    private Node originalTail; // Tail of the original playlist

    private boolean shuffleEnabled;

    private MediaPlayer mediaPlayer;

    private MainController mainController;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }





    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.historyHead = null;
        this.historyTail = null;
        this.originalHead = null;
        this.originalTail = null;
        this.shuffleEnabled = false;

    }


    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }


    public Node getHistoryHead() {
        return historyHead;
    }

    public void setHistoryHead(Node historyHead) {
        this.historyHead = historyHead;
    }

//    String songsFolderPath = "src/main/songs"; // Replace with the actual path to your "songs" folder

//    public void toggleShuffle() {
//        shuffleEnabled = !shuffleEnabled;
//
//        if (shuffleEnabled) {
//            shufflePlaylist();  // Call shufflePlaylist when shuffle is enabled
//        }
//        else if (!shuffleEnabled) {
//            System.out.println("shuffle off...");
////            loadSongsFromDirectory(songsFolderPath);
//        }
//    }

    public void toggleShuffle() {
        if (shuffleEnabled) {
            shuffleEnabled = false;
            System.out.println("Shuffle off, reverting to original playlist...");

            if (historyHead != null) {
                // Revert to the original playlist by setting the current playlist to the original playlist
                head = originalHead;
                tail = originalTail;

                // Clear the history playlist
                historyHead = null;
                historyTail = null;

                // Optionally update UI to reflect shuffle state
//                updateShuffleButtonState();
            } else {
                System.out.println("No original playlist available.");
            }
        } else {
            shuffleEnabled = true;
            System.out.println("Shuffle on...");
            shufflePlaylist();
        }
    }




    public boolean isShuffleEnabled() {
        return shuffleEnabled;
    }



    public Node findNode(Song targetSong) {
        Node forward = head;
        Node backward = tail;

        while (forward != null || backward != null) {
            if (forward != null && forward.getData().equals(targetSong)) {
                return forward;
            }

            if (backward != null && backward.getData().equals(targetSong)) {
                return backward;
            }

            if (forward != null) {
                forward = forward.getNext();
            }

            if (backward != null) {
                backward = backward.getPrev();
            }
        }

        return null;  // Node not found
    }





    public void addSong(Song song) {
        Node newNode = new Node(song);
        if (head == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
        }
        tail = newNode;
    }



    public void playCurrentSong() {
        if (head != null) {
            Media media = new Media(new File(head.getData().getFilePath()).toURI().toString());

            System.out.println("Playing: " + head.getData());
            if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mainController.setSongLabel("Playing: " + head.getData().getTitle());
                mediaPlayer.play(); // Resume playback if paused
                return;
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
            }

            if (mainController != null) {
                mainController.setSongLabel("Playing: " + head.getData().getTitle());
            }

            mediaPlayer = new MediaPlayer(media);
            // Add listener for end of media event
            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("Song completed. Playing next song.");
                playNextSong();
            });
            mediaPlayer.play();
//            historyPlaylist.setHistoryHead(head);
            addToHistory(head.getData());

        }
    }




    public void playNextSong() {
        if (head != null && head.getNext() != null) {
            head = head.getNext();
            System.out.println("Playing next song: " + head.getData());
            playCurrentSong(); // Play the new current song
        } else {
            System.out.println("No next song in the playlist.");
        }
    }

    public void playPreviousSong() {
        if (head != null && head.getPrev() != null) {
            head = head.getPrev();
            System.out.println("Playing previous song: " + head.getData());
            playCurrentSong(); // Play the new current song
        } else {
            System.out.println("No previous song in the playlist.");
        }
    }

    public void pauseCurrentSong() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            mainController.setSongLabel("Paused: " + head.getData().getTitle());
            System.out.println("Song paused: " + head.getData());
        } else {
            System.out.println("No song is currently playing.");
        }
    }



    public void shufflePlaylist() {
        System.out.println("Shuffling playlist...");

        if (shuffleEnabled && head != null) {
            // Convert the linked list to an array
            Node[] nodeList = toArray();
            int length = nodeList.length;

            Random random = new Random();

            for (int i = length - 1; i > 0; i--) {
                int swapIndex = random.nextInt(i + 1);

                // Swap nodes
                Node temp = nodeList[i];
                nodeList[i] = nodeList[swapIndex];
                nodeList[swapIndex] = temp;
            }

            // Reconstruct the playlist from the shuffled array
            head = nodeList[0];
            head.setPrev(null);

            Node current = head;

            for (int i = 1; i < length; i++) {
                Node nextNode = nodeList[i];
                current.setNext(nextNode);
                nextNode.setPrev(current);
                current = nextNode;
            }

            tail = current;
            tail.setNext(null);
        }
    }





    private Node[] toArray() {
        // Convert the linked list to an array
        int size = getSize();
        Node[] array = new Node[size];

        Node current = head;
        int index = 0;

        while (current != null) {
            array[index++] = current;
            current = current.getNext();
        }

        return array;
    }

    private int getSize() {
        // Calculate the size of the linked list
        int size = 0;
        Node current = head;

        while (current != null) {
            size++;
            current = current.getNext();
        }

        return size;
    }

    public void loadSongsFromDirectory(String directoryPath) {
        // Check if the directory path is valid
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path or directory does not exist.");
            return;
        }

        // Count the number of valid song files
        int songCount = 0;
        String[] fileNames = directory.list();
        if (fileNames != null) {
            for (String fileName : fileNames) {
                if (fileName.toLowerCase().endsWith(".mp3")) {
                    songCount++;
                }
            }
        }

        // Create an array to store song files
        String[] songFiles = new String[songCount];

        // Copy valid song file names to the array
        int index = 0;
        if (fileNames != null) {
            for (String fileName : fileNames) {
                if (fileName.toLowerCase().endsWith(".mp3")) {
                    songFiles[index++] = fileName;
                }
            }
        }

        // Add songs to your collection
        for (String songFileName : songFiles) {
            addSong(new Song(songFileName, directoryPath + File.separator + songFileName));
        }
        originalHead = cloneList(head);
        originalTail = findTail(originalHead);
    }

    private Node cloneList(Node sourceHead) {
        Node newHead = null;
        Node currentSource = sourceHead;
        Node currentNew = null;

        while (currentSource != null) {
            Node newNode = new Node(currentSource.getData());

            if (newHead == null) {
                newHead = newNode;
                currentNew = newHead;
            } else {
                currentNew.setNext(newNode);
                newNode.setPrev(currentNew);
                currentNew = newNode;
            }

            currentSource = currentSource.getNext();
        }

        return newHead;
    }

    private Node findTail(Node listHead) {
        Node current = listHead;

        while (current != null && current.getNext() != null) {
            current = current.getNext();
        }

        return current;
    }



    public Node findHistoryNode(Song targetSong) {
        Node forward = historyHead;
        Node backward = historyTail;

        while (forward != null || backward != null) {
            if (forward != null && forward.getData().equals(targetSong)) {
                return forward;
            }

            if (backward != null && backward.getData().equals(targetSong)) {
                return backward;
            }

            if (forward != null) {
                forward = forward.getNext();
            }

            if (backward != null) {
                backward = backward.getPrev();
            }
        }
        return null;
    }


    public void addToHistory(Song song) {
        Node newHistoryNode = new Node(song);

        if (historyHead == null) {
            historyHead = newHistoryNode;
        } else {
            historyTail.setNext(newHistoryNode);
            newHistoryNode.setPrev(historyTail);
        }

        historyTail = newHistoryNode;
    }



    public void removeFromHistory(Song targetSong) {
        Node historyNodeToRemove = findHistoryNode(targetSong);

        if (historyNodeToRemove != null) {
            // Check if the node to remove is the head or tail of the history
            if (historyNodeToRemove == historyHead) {
                historyHead = historyNodeToRemove.getNext();
            }

            if (historyNodeToRemove == historyTail) {
                historyTail = historyNodeToRemove.getPrev();
            }

            // Adjust the pointers of the surrounding nodes
            Node prevNode = historyNodeToRemove.getPrev();
            Node nextNode = historyNodeToRemove.getNext();

            if (prevNode != null) {
                prevNode.setNext(nextNode);
            }

            if (nextNode != null) {
                nextNode.setPrev(prevNode);
            }
        }

//        updateHistoryListView();
    }

}
