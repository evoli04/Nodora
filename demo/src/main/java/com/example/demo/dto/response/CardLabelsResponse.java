package com.example.demo.dto.response;

public class CardLabelsResponse {
    private Integer cardLabelId;
    private Integer labelId;
    private Integer cardId;
    private Integer memberId;

    public Integer getCardLabelId() {
        return cardLabelId;
    }

    public void setCardLabelId(Integer cardLabelId) {
        this.cardLabelId = cardLabelId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
}
}