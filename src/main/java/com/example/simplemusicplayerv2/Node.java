package com.example.simplemusicplayerv2;

public class Node {
    private Song data;
    private Node prev;
    private Node next;

    public Node(Song data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }

    public Song getData() {
        return data;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getNext() {
        return next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
