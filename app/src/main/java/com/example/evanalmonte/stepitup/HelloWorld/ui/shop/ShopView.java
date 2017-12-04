package com.example.evanalmonte.stepitup.HelloWorld.ui.shop;

import com.example.evanalmonte.stepitup.HelloWorld.model.Item;

import java.util.List;

/**
 * Created by evanalmonte on 12/2/17.
 */

public interface ShopView {
    void showItems(List<Item> itemlist);

    public void setHatImage(int id, boolean animateable);

    public void setPantsImage(int id, boolean animateable);

    public void setShirtImage(int id, boolean animateable);

    public void setShoesImage(int id, boolean animateable);
}
