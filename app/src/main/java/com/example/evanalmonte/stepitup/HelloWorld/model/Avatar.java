package com.example.evanalmonte.stepitup.HelloWorld.model;

import java.util.Set;

public class Avatar {
    Item hat;
    Item shirt;
    Item pants;
    Item shoes;
    Set<Item> inventory;
    int currency;

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
