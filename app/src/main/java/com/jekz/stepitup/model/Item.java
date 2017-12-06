package com.jekz.stepitup.model;

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
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + type.hashCode();
        result = prime * result + price;
        result = prime * result + (animated ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Item)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        final Item o = (Item) obj;
        return o.getName().equals(name) &&
               o.type.equals(type) &&
               (o.price == price) &&
               (o.isAnimated() == isAnimated());
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
        SHOES,
        MISC
    }
}