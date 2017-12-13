package com.jekz.stepitup.ui.shop;

import com.jekz.stepitup.model.item.Item;
import com.jekz.stepitup.ui.AvatarView;

import java.util.List;

/**
 * Created by evanalmonte on 12/2/17.
 */

public interface ShopView extends AvatarView {

    void showItems(List<Item> itemlist);

    void setCurrencyText(String text);

    void reloadAdapter();
}
