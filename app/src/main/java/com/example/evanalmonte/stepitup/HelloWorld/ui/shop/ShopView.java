package com.example.evanalmonte.stepitup.HelloWorld.ui.shop;

import com.example.evanalmonte.stepitup.HelloWorld.model.Item;

import java.util.List;

/**
 * Created by evanalmonte on 12/2/17.
 */

public interface ShopView {

    void showItems(List<Item> itemlist);

    void setHatImage(int id, boolean animateable);

    void setPantsImage(int id, boolean animateable);

    void setShirtImage(int id, boolean animateable);

    void setShoesImage(int id, boolean animateable);

    void setCurrencyText(String text);

    void reloadAdapter();
}
