package com.evry.dto;

public class QuantityDto {
    private Long itemId;
    private Long quantity;

    public QuantityDto() {
    }

    public QuantityDto(Long itemId, Long quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
