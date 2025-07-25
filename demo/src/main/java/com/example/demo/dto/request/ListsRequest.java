package com.example.demo.dto.request;

public class ListsRequest {
    private String title;
    private Integer board_id;
    private Integer position;

    public ListsRequest() {}

    public ListsRequest(String title, Integer board_id, Integer position) {
        this.title = title;
        this.board_id = board_id;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Integer board_id) {
        this.board_id = board_id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
}
}