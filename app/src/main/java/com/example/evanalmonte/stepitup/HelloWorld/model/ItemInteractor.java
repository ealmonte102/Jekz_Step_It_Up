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
    private HashMap<String, Integer> modelList;

    private ItemInteractor(HashMap<Item.Item_Type, LinkedHashMap<Integer, Item>> items,
                           HashMap<String, Integer> modelList) {
        this.itemList = items;
        this.modelList = modelList;
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
        extractFromResources(Item.Item_Type.MISC, tempItemList, resources, numOfAttributes, R
                .array.misc);
        HashMap<String, Integer> modelList = extractModels(resources);

        instance = new ItemInteractor(tempItemList, modelList);
        return instance;
    }

    private static HashMap<String, Integer> extractModels(Resources res) {
        HashMap<String, Integer> modelList = new HashMap<>();
        TypedArray typedArray = res.obtainTypedArray(R.array.models);
        for (int i = 0; i < typedArray.length(); i += 2) {
            modelList.put(typedArray.getString(i + 1), typedArray.getResourceId(i, 0));
        }
        typedArray.recycle();
        return modelList;
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

    public int getModel(String name) {
        return modelList.get(name);
    }

    public Item getInitialItem() {
        return itemList.get(Item.Item_Type.MISC).get(R.drawable.blank);
    }
}
