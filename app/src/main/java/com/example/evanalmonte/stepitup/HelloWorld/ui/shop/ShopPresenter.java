package com.example.evanalmonte.stepitup.HelloWorld.ui.shop;

import com.example.evanalmonte.stepitup.HelloWorld.model.Avatar;
import com.example.evanalmonte.stepitup.HelloWorld.model.Item;
import com.example.evanalmonte.stepitup.HelloWorld.model.ItemInteractor;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by evanalmonte on 12/2/17.
 */

public class ShopPresenter {
    private ItemInteractor itemInteractor;
    private ShopView shopView;
    private Avatar avatar;

    public ShopPresenter(ShopView view, ItemInteractor instance) {
        this.shopView = view;
        this.itemInteractor = instance;
        this.avatar = new Avatar();
        avatar.addCurrency(11300);
    }

    public void reloadAvatar() {
        shopView.setCurrencyText("x" + NumberFormat.getInstance().format(avatar.getCurrency()));
        Item[] items = {avatar.getHat(), avatar.getShirt(), avatar.getPants(), avatar.getShoes()};
        int i = 0;
        if (items[i] != null) {
            shopView.setHatImage(items[i].getId(), items[i++].isAnimated());
        }
        if (items[i] != null) {
            shopView.setShirtImage(items[i].getId(), items[i++].isAnimated());

        }
        if (items[i] != null) {
            shopView.setPantsImage(items[i].getId(), items[i++].isAnimated());
        }
        if (items[i] != null) {
            shopView.setShoesImage(items[i].getId(), items[i].isAnimated());

        }

    }

    public void loadHats() {
        shopView.showItems(new ArrayList<>(itemInteractor.getItems(Item.Item_Type.HAT)));
    }

    public void loadShirts() {
        shopView.showItems(new ArrayList<>(itemInteractor.getItems(Item.Item_Type.SHIRT)));
    }

    public void loadPants() {
        shopView.showItems(new ArrayList<>(itemInteractor.getItems(Item.Item_Type.PANTS)));

    }

    public void loadShoes() {
        shopView.showItems(new ArrayList<>(itemInteractor.getItems(Item.Item_Type.SHOES)));
    }

    public void buyItem(Item item) {
        if (!avatar.buyItem(item)) { return; }
        avatar.wearItem(item);
        reloadAvatar();
        shopView.reloadAdapter();
    }

    public boolean checkForItem(Item item) {
        return avatar.hasItem(item);
    }

    public void equipItem(Item item) {
        avatar.wearItem(item);
        reloadAvatar();
    }
}
