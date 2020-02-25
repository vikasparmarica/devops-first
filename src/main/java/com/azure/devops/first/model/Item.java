package com.azure.devops.first.model;

public class Item {
    private String id;
    private String name;
    private int quantity;
    private Double price;
    private Double totalPrice;
    private String imgURL;

    public Item(String id, String name, int quantity, Double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.azure.devops.first.model.Item item = (com.azure.devops.first.model.Item) o;
        return quantity == item.quantity &&
                java.util.Objects.equals(id, item.id) &&
                java.util.Objects.equals(name, item.name) &&
                java.util.Objects.equals(price, item.price) &&
                java.util.Objects.equals(totalPrice, item.totalPrice) &&
                java.util.Objects.equals(imgURL, item.imgURL);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, quantity, price, totalPrice, imgURL);
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

}
