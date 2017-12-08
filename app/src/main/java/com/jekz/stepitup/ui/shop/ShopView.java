package com.jekz.stepitup.ui.shop;

import com.jekz.stepitup.model.item.Item;

import java.util.List;

/**
 * Created by evanalmonte on 12/2/17.
 */

public interface ShopView {

    void showItems(List<Item> itemlist);

    void setAvatarImage(int id);

    void setHatImage(int id);

    void setPantsImage(int id);

    void setShirtImage(int id);

    void setShoesImage(int id);

    void animateHat(boolean animate);

    void animateShirt(boolean animate);

    void animateShoes(boolean animate);

    void animatePants(boolean animate);

    void animateAvatar(boolean animate);

    void setCurrencyText(String text);

    void reloadAdapter();
}
