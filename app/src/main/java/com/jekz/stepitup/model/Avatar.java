package com.jekz.stepitup.model;

import java.util.HashSet;
import java.util.Set;

public class Avatar {
    boolean isMale = true;
    Item hat;
    Item shirt;
    Item pants;
    Item shoes;
    Set<Item> inventory = new HashSet<>();
    int currency;

    public Avatar(Item hat, Item shirt, Item pants, Item shoes, int currency) {
        this.hat = hat;
        this.shirt = shirt;
        this.pants = pants;
        this.shoes = shoes;
        this.currency = currency;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }
    public Item getHat() {
        return hat;
    }

    public Item getShirt() {
        return shirt;
    }

    public Item getPants() {
        return pants;
    }

    public Item getShoes() {
        return shoes;
    }

    public boolean hasItem(Item item) {
        return inventory.contains(item);
    }

    public int getCurrency() {
        return currency;
    }

    public void addCurrency(int x) {
        if (x <= 0) { x = 0; }
        currency += x;
    }

    public void removeCurrency(int x) {
        if (x <= 0) { x = 0; }
        if (x >= currency) { x = currency; }
        currency -= x;
    }

    public boolean buyItem(Item item) {
        if (item.getPrice() > currency) {
            return false;
        }
        removeCurrency(item.getPrice());
        addItem(item);
        return true;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public boolean wearItem(Item item) {
        if (!inventory.contains(item)) {
            return false;
        }
        switch (item.getType()) {
            case HAT:
                hat = item;
                break;
            case PANTS:
                pants = item;
                break;
            case SHIRT:
                shirt = item;
                break;
            case SHOES:
                shoes = item;
                break;
        }
        return true;
    }


}
