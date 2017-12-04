package com.example.evanalmonte.stepitup.HelloWorld.model;

/**
 * Created by evanalmonte on 11/25/17.
 */

public class Item {
    private int id;
    private String name;
    private Item_Type type;
    private int price;
    private boolean animated;

    public Item(int id, String name, Item_Type type, boolean animated, int price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.animated = animated;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Item_Type getType() { return type; }

    public int getPrice() {
        return price;
    }

    public boolean isAnimated() {
        return animated;
    }

    @Override
    public String toString() {
        if (name == null) {
            return "NULL ?";
        } else {
            return name + ":" + price;
        }
    }

    public enum Item_Type {
        HAT,
        SHIRT,
        PANTS,
        SHOES
    }
}