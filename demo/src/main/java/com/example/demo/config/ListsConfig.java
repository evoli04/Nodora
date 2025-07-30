package com.example.demo.config;

import com.example.demo.model.lists.Lists;

// Lists entity'si ile DTO arasında manuel dönüşüm sağlayan yardımcı config sınıfı
public class ListsConfig {
    // Entity'den DTO'ya dönüştürme örneği
    public static ListsDTO toDTO(Lists entity) {
        if (entity == null) return null;
        return new ListsDTO(
                entity.getListId(),
                entity.getTitle(),
                entity.getPosition(),
                entity.getBoardId()
        );
    }

    // DTO'dan Entity'ye dönüştürme örneği
    public static Lists toEntity(ListsDTO dto) {
        if (dto == null) return null;
        Lists lists = new Lists(
                dto.getListId(),
                dto.getTitle(),
                dto.getPosition(),
                dto.getBoardId()
        );
        // Eğer DTO'ya memberId eklersen burada set edebilirsin.
        // lists.setMemberId(dto.getMemberId());
        return lists;
    }

    // Lists tablosu için sade DTO (Data Transfer Object) sınıfı
    public static class ListsDTO {
        private Integer listId;
        private String title;
        private String position;
        private Integer boardId;

        public ListsDTO() {}

        // Tüm alanları içeren constructor
        public ListsDTO(Integer listId, String title, String position, Integer boardId) {
            this.listId = listId;
            this.title = title;
            this.position = position;
            this.boardId = boardId;
        }

        // Getter ve setter metotları
        public Integer getListId() { return listId; }
        public void setListId(Integer listId) { this.listId = listId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }

        public Integer getBoardId() { return boardId; }
        public void setBoardId(Integer boardId) { this.boardId = boardId;}
   }
}