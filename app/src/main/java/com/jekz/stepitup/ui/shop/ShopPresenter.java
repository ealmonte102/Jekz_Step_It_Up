package com.jekz.stepitup.ui.shop;

import com.jekz.stepitup.model.Avatar;
import com.jekz.stepitup.model.Item;
import com.jekz.stepitup.model.ItemInteractor;

import java.text.NumberFormat;

/**
 * Created by evanalmonte on 12/2/17.
 */

public class ShopPresenter {
    private ItemInteractor itemInteractor;
    private ShopView shopView;
    private Avatar avatar;

    ShopPresenter(ShopView view, ItemInteractor instance) {
        this.shopView = view;
        this.itemInteractor = instance;
        Item blank = instance.getItem(0).first;
        avatar = new Avatar(blank, blank, blank, blank, 11300);
    }

    void reloadAvatar() {
        shopView.setCurrencyText("x" + NumberFormat.getInstance().format(avatar.getCurrency()));
        String modelString = avatar.isMale() ? "male" : "female";
        int modelID = itemInteractor.getModel(modelString);
        Item hat = avatar.getHat();
        Item shirt = avatar.getShirt();
        Item pants = avatar.getPants();
        Item shoes = avatar.getShoes();
        shopView.setHatImage(itemInteractor.getItem(hat.getId()).second);
        shopView.setShirtImage(itemInteractor.getItem(shirt.getId()).second);
        shopView.setPantsImage(itemInteractor.getItem(pants.getId()).second);
        shopView.setShoesImage(itemInteractor.getItem(shoes.getId()).second);
        shopView.setAvatarImage(modelID);
        if (hat.isAnimated()) {
            shopView.animateHat(true);
        }
        if (shirt.isAnimated()) {
            shopView.animateShirt(true);
        }
        if (pants.isAnimated()) {
            shopView.animatePants(true);
        }
        if (shoes.isAnimated()) {
            shopView.animateShoes(true);
        }
        shopView.animateAvatar(true);

    }

    public void loadHats() {
        shopView.showItems(itemInteractor.getItems(Item.Item_Type.HAT));
    }

    public void loadShirts() {
        shopView.showItems(itemInteractor.getItems(Item.Item_Type.SHIRT));
    }

    public void loadPants() {
        shopView.showItems(itemInteractor.getItems(Item.Item_Type.PANTS));

    }

    public void loadShoes() {
        shopView.showItems(itemInteractor.getItems(Item.Item_Type.SHOES));
    }

    public void buyItem(Item item) {
        if (!avatar.buyItem(item)) { return; }
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

    public void changeGender() {
        boolean isMale = avatar.isMale();
        avatar.setMale(!isMale);
        reloadAvatar();
    }

    public int resourceRequested(Item item) {
        return itemInteractor.getItem(item.getId()).second;
    }
}
