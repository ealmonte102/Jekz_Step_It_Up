package com.example.evanalmonte.stepitup.HelloWorld.ui.shop;

import com.example.evanalmonte.stepitup.HelloWorld.model.Item;
import com.example.evanalmonte.stepitup.HelloWorld.model.ItemInteractor;

import java.util.ArrayList;

/**
 * Created by evanalmonte on 12/2/17.
 */

public class ShopPresenter {
    private ItemInteractor itemInteractor;
    private ShopView shopView;

    public ShopPresenter(ShopView view, ItemInteractor instance) {
        this.shopView = view;
        this.itemInteractor = instance;
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
}
