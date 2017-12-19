package com.jekz.stepitup.model.avatar;

import com.jekz.stepitup.model.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Avatar {
    private static final String DEFAULT_MODEL = "male";

    private String model = DEFAULT_MODEL;
    private Item hat;
    private Item shirt;
    private Item pants;
    private Item shoes;
    private Set<Item> inventory = new HashSet<>();
    private int currency;

    private ArrayList<AvatarObserver> observers = new ArrayList<>();

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public void setCurrency(int x) {
        currency = x;
        notifyCurrencyChange(x);
    }

    private void notifyCurrencyChange(int x) {
        for (AvatarObserver observer : observers) {
            observer.onCurrencyChanged(x);
        }
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

    public void removeItem(Item.Item_Type item_type) {
        switch (item_type) {
            case HAT:
                hat = null;
                break;
            case SHIRT:
                shirt = null;
                break;
            case PANTS:
                pants = null;
                break;
            case SHOES:
                shoes = null;
                break;
        }
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

    public boolean isItemEquipped(Item item) {
        switch (item.getType()) {
            case HAT:
                return item.equals(hat);
            case SHIRT:
                return item.equals(shirt);
            case PANTS:
                return item.equals(pants);
            case SHOES:
                return item.equals(shoes);
        }
        return false;
    }

    public void addAvatarObserver(AvatarObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(AvatarObserver observer) {
        observers.remove(observer);
    }

    public interface AvatarObserver {
        void onCurrencyChanged(int x);
    }
}
