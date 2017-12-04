package com.example.evanalmonte.stepitup.HelloWorld.ui.shop;

import android.util.Log;

import com.example.evanalmonte.stepitup.HelloWorld.model.Avatar;
import com.example.evanalmonte.stepitup.HelloWorld.model.Item;
import com.example.evanalmonte.stepitup.HelloWorld.model.ItemInteractor;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

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
        avatar.addCurrency(300);
    }

    public void loadAvatar() {
        shopView.setCurrencyText("x" + String.valueOf(avatar.getCurrency()));
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

    public void shopItemClicked(Item item) {
        Log.d(TAG, item.getName() + " " + item.getId() + " " + item.isAnimated());
        avatar.wearItem(item);
        int id = item.getId();
        boolean isAnimated = item.isAnimated();
        switch (item.getType()) {
            case PANTS:
                shopView.setPantsImage(id, isAnimated);
                break;
            case SHOES:
                shopView.setShoesImage(id, isAnimated);
                break;
            case SHIRT:
                shopView.setShirtImage(id, isAnimated);
                break;
            case HAT:
                shopView.setHatImage(id, isAnimated);
                break;
        }
    }
}
