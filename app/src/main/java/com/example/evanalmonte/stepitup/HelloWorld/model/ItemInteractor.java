package com.example.evanalmonte.stepitup.HelloWorld.model;

import android.content.res.Resources;
import android.content.res.TypedArray;

import com.example.evanalmonte.stepitup.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by evanalmonte on 12/1/17.
 */

public class ItemInteractor {
    private static ItemInteractor instance;
    private HashMap<Item.Item_Type, LinkedHashMap<Integer, Item>> itemList;

    private ItemInteractor(HashMap<Item.Item_Type, LinkedHashMap<Integer, Item>> items) {
        this.itemList = items;
    }

    public static ItemInteractor getInstance(Resources resources) {
        if (instance != null) {
            return instance;
        }
        int numOfAttributes = resources.getInteger(R.integer.attributes);
        HashMap<Item.Item_Type, LinkedHashMap<Integer, Item>> tempItemList = new HashMap<>();
        extractFromResources(Item.Item_Type.HAT, tempItemList, resources, numOfAttributes, R
                .array.hats);
        extractFromResources(Item.Item_Type.SHIRT, tempItemList, resources, numOfAttributes, R
                .array.shirts);

        extractFromResources(Item.Item_Type.SHOES, tempItemList, resources, numOfAttributes, R
                .array.shoes);
        extractFromResources(Item.Item_Type.PANTS, tempItemList, resources, numOfAttributes, R
                .array.pants);

        instance = new ItemInteractor(tempItemList);
        return instance;
    }

    private static void extractFromResources(Item.Item_Type item_type, HashMap<Item.Item_Type,
            LinkedHashMap<Integer, Item>> itemList, Resources res, int numOfAttributes, int arrayID
    ) {
        LinkedHashMap<Integer, Item> tempHats = new LinkedHashMap<>();
        itemList.put(item_type, tempHats);
        TypedArray itemArray = res.obtainTypedArray(arrayID);
        int id;
        for (int i = 0; i < itemArray.length(); i += numOfAttributes) {
            id = itemArray.getResourceId(i, 0);
            tempHats.put(id,
                    new Item(id,
                            itemArray.getString(i + 1),
                            item_type,
                            itemArray.getBoolean(i + 2, false),
                            itemArray.getInt(i + 3, -1)));
        }
        itemArray.recycle();
    }

    public Collection<Item> getItems(Item.Item_Type type) {
        LinkedHashMap<Integer, Item> fetchedItems = itemList.get(type);
        return fetchedItems == null ? null : fetchedItems.values();
    }

    public Item getItems(Item.Item_Type type, int id) {
        return itemList.get(type).get(id);
    }
}
