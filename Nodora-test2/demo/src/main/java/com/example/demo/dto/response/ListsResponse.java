package com.example.demo.dto.response;

public class ListsResponse {
    private Integer list_id;
    private String title;
    private Integer board_id;
    private Integer position;

    public ListsResponse() {}

    public ListsResponse(Integer list_id, String title, Integer board_id, Integer position) {
        this.list_id = list_id;
        this.title = title;
        this.board_id = board_id;
        this.position = position;
    }

    public Integer getList_id() {
        return list_id;
    }

    public void setList_id(Integer list_id) {
        this.list_id = list_id;
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