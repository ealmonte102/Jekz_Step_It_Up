package com.jekz.stepitup.ui.shop;

import android.util.Log;

import com.jekz.stepitup.customview.AvatarImage;
import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.RequestString;
import com.jekz.stepitup.model.avatar.Avatar;
import com.jekz.stepitup.model.avatar.AvatarRepo;
import com.jekz.stepitup.model.item.Item;
import com.jekz.stepitup.model.item.ItemInteractor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * Created by evanalmonte on 12/2/17.
 */

public class ShopPresenter implements Presenter,
        AsyncResponse, Avatar.AvatarObserver {
    private static final String TAG = ShopPresenter.class.getName();

    private ItemInteractor itemInteractor;
    private ShopView shopView;
    private AvatarRepo repo = AvatarRepo.getInstance();
    private LoginPreferences preferences;
    private NumberFormat numberFormat = NumberFormat.getInstance();


    ShopPresenter(ItemInteractor instance, LoginPreferences preferences) {
        this.itemInteractor = instance;
        this.preferences = preferences;
    }

    void reloadImages() {
        Avatar avatar = repo.getAvatar();
        Item hat = avatar.getHat();
        Item shirt = avatar.getShirt();
        Item pants = avatar.getPants();
        Item shoes = avatar.getShoes();
        String model = avatar.getModel();

        if (hat != null) {
            shopView.setAvatarImagePart(AvatarImage.AvatarPart.HAT, itemInteractor.getItem(hat
                    .getId()).second);
        }

        if (shirt != null) {
            shopView.setAvatarImagePart(AvatarImage.AvatarPart.SHIRT, itemInteractor.getItem(shirt
                    .getId()).second);
        }

        if (pants != null) {
            shopView.setAvatarImagePart(AvatarImage.AvatarPart.PANTS, itemInteractor.getItem(pants
                    .getId()).second);
        }

        if (shoes != null) {
            shopView.setAvatarImagePart(AvatarImage.AvatarPart.SHOES, itemInteractor.getItem(shoes
                    .getId()).second);
        }
        shopView.setAvatarImagePart(AvatarImage.AvatarPart.MODEL, itemInteractor.getModel(model));
    }

    void reloadAnimations() {
        Avatar avatar = repo.getAvatar();
        Item hat = avatar.getHat();
        Item shirt = avatar.getShirt();
        Item pants = avatar.getPants();
        Item shoes = avatar.getShoes();

        if (hat != null && hat.isAnimated()) {
            shopView.animateAvatarImagePart(AvatarImage.AvatarPart.HAT, true);
        }
        if (shirt != null && shirt.isAnimated()) {
            shopView.animateAvatarImagePart(AvatarImage.AvatarPart.SHIRT, true);
        }
        if (pants != null && pants.isAnimated()) {
            shopView.animateAvatarImagePart(AvatarImage.AvatarPart.PANTS, true);
        }
        if (shoes != null && shoes.isAnimated()) {
            shopView.animateAvatarImagePart(AvatarImage.AvatarPart.SHOES, true);
        }
        shopView.animateAvatarImagePart(AvatarImage.AvatarPart.MODEL, true);

    }

    void loadHats() {
        shopView.showItems(itemInteractor.getItems(Item.Item_Type.HAT));
    }

    void loadShirts() {
        shopView.showItems(itemInteractor.getItems(Item.Item_Type.SHIRT));
    }

    void loadPants() {
        shopView.showItems(itemInteractor.getItems(Item.Item_Type.PANTS));

    }

    void loadShoes() {
        shopView.showItems(itemInteractor.getItems(Item.Item_Type.SHOES));
    }

    void buyItem(Item item) {
        updateItem(item.getId(), "purchase", 0, 0, 0, 0, "nogender");
    }

    boolean checkForItem(Item item) {
        return repo.getAvatar().hasItem(item);
    }

    void equipItem(Item item) {
        Avatar avatar = repo.getAvatar();
        Item hat = avatar.getHat();
        Item shirt = avatar.getShirt();
        Item pants = avatar.getPants();
        Item shoes = avatar.getShoes();
        int hatid = 0;
        int shirtid = 0;
        int pantsid = 0;
        int shoesid = 0;
        if (hat != null) {
            hatid = hat.getId();
        }
        if (shirt != null) {
            shirtid = shirt.getId();
        }
        if (pants != null) {
            pantsid = pants.getId();
        }
        if (shoes != null) {
            shoesid = shoes.getId();
        }
        switch (item.getType()) {
            case HAT:
                hatid = item.getId();
                break;
            case SHIRT:
                shirtid = item.getId();
                break;
            case PANTS:
                pantsid = item.getId();
                break;
            case SHOES:
                shoesid = item.getId();
                break;
        }
        updateItem(0, "equip", hatid, shirtid, pantsid, shoesid, "nogender");
    }

    void changeGender() {
        String model = repo.getAvatar().getModel().equals("male") ? "female" : "male";
        updateItem(0, "gender", 0, 0, 0, 0, model);
    }

    int resourceRequested(Item item) {
        return itemInteractor.getItem(item.getId()).second;
    }

    void unequipItem(Item item) {
        Avatar avatar = repo.getAvatar();
        Item hat = avatar.getHat();
        Item shirt = avatar.getShirt();
        Item pants = avatar.getPants();
        Item shoes = avatar.getShoes();
        int hatid = 0;
        int shirtid = 0;
        int pantsid = 0;
        int shoesid = 0;
        if (hat != null) {
            hatid = hat.getId();
        }
        if (shirt != null) {
            shirtid = shirt.getId();
        }
        if (pants != null) {
            pantsid = pants.getId();
        }
        if (shoes != null) {
            shoesid = shoes.getId();
        }
        switch (item.getType()) {
            case HAT:
                hatid = 0;
                break;
            case SHIRT:
                shirtid = 0;
                break;
            case PANTS:
                pantsid = 0;
                break;
            case SHOES:
                shoesid = 0;
                break;
        }
        updateItem(0, "equip", hatid, shirtid, pantsid, shoesid, "nogender");
    }

    @Override
    public void onViewAttached(ShopView view) {
        this.shopView = view;
        shopView.setCurrencyText("x" + numberFormat.format(repo.getAvatar().getCurrency()));
        repo.addRepoObserver(this);
    }

    @Override
    public void onViewDetached() {
        this.shopView = null;
        repo.removeRepoObserver(this);
    }

    boolean isItemEquipped(Item item) {
        return repo.getAvatar().isItemEquipped(item);
    }


    @Override
    public void processFinish(JSONObject returnObject) {
        Avatar avatar = repo.getAvatar();
        try {
            JSONArray output = returnObject.getJSONArray("rows");
            for (int i = 0; i < output.length(); i++) {

                //Update Equipped Items (Equip / Unequip)
                try {
                    JSONObject q = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("equip_items")) {
                        boolean succeed = q.getBoolean("success");
                        if (succeed) {
                            int hatid = q.getInt("hat");
                            int shirtid = q.getInt("shirt");
                            int pantsid = q.getInt("pants");
                            int shoesid = q.getInt("shoes");
                            if (hatid != 0) {
                                Item hat = itemInteractor.getItem(hatid).first;
                                int hatResId = itemInteractor.getItem(hatid).second;
                                avatar.wearItem(hat);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.HAT, hatResId);
                            } else {
                                avatar.removeItem(Item.Item_Type.HAT);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.HAT, 0);
                            }
                            if (shirtid != 0) {
                                Item shirt = itemInteractor.getItem(shirtid).first;
                                int shirtID = itemInteractor.getItem(shirtid).second;
                                avatar.wearItem(shirt);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.SHIRT, shirtID);
                            } else {
                                avatar.removeItem(Item.Item_Type.SHIRT);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.SHIRT, 0);
                            }
                            if (pantsid != 0) {
                                Item pants = itemInteractor.getItem(pantsid).first;
                                int pantsID = itemInteractor.getItem(pantsid).second;
                                avatar.wearItem(pants);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.PANTS, pantsID);
                            } else {
                                avatar.removeItem(Item.Item_Type.PANTS);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.PANTS, 0);
                            }
                            if (shoesid != 0) {
                                Item shoes = itemInteractor.getItem(shoesid).first;
                                int shoesID = itemInteractor.getItem(shoesid).second;
                                avatar.wearItem(shoes);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.SHOES, shoesID);
                            } else {
                                avatar.removeItem(Item.Item_Type.SHOES);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.SHOES, 0);
                            }
                            reloadAnimations();
                            shopView.reloadAdapter();
                        }
                    }

                } catch (JSONException ignored) {
                    Log.d(TAG, "Error parsing during equip items update: " + ignored.getMessage());
                }

                //Update Gender
                try {
                    JSONObject r = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("update_user_info")) {
                        boolean succeed = r.getBoolean("success");
                        if (succeed) {
                            String gender = r.getString("gender");
                            if (gender.equals("male") || gender.equals("female")) {
                                avatar.setModel(gender);
                                int modelID = itemInteractor.getModel(gender);
                                shopView.setAvatarImagePart(AvatarImage.AvatarPart.MODEL, modelID);
                                reloadAnimations();
                            }
                        }
                    }

                } catch (JSONException ignored) {
                    Log.d(TAG, "Error parsing during gender update: " + ignored.getMessage());
                }

                //Purchase Item, Update Currency
                try {
                    JSONObject t = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("purchase")) {
                        boolean success = t.getBoolean("success");
                        Log.d(TAG, "Purchase result: " + success);
                        if (success) {
                            int currency = t.getInt("user_currency");
                            int itemid = t.getInt("itemid");
                            Item item = itemInteractor.getItem(itemid).first;
                            Log.d(TAG, "Item bought: " + item.toString());
                            avatar.addItem(item);
                            avatar.setCurrency(currency);
                            shopView.setCurrencyText(
                                    "x" + NumberFormat.getInstance().format(avatar.getCurrency()));
                            shopView.reloadAdapter();
                        }
                    }

                } catch (JSONException ignored) {
                    Log.d(TAG, "Error parsing during currency update: " + ignored.getMessage());
                }
            }

        } catch (JSONException ignored) {}

    }


    void updateItem(int itemid, String type, int hatid, int shirtid, int pantsid, int shoesid,
                    String gender) {
        JSONObject postData = new JSONObject();

        if (type.equals("purchase")) {
            try {
                postData.put("action", "purchase");
                postData.put("itemid", itemid);
                postData.put("amount", 1);

            } catch (JSONException e) {e.printStackTrace();}
        }
        if (type.equals("equip")) {
            try {
                postData.put("action", "equip_items");
                postData.put("hat", hatid);
                postData.put("shirt", shirtid);
                postData.put("pants", pantsid);
                postData.put("shoes", shoesid);


            } catch (JSONException e) {e.printStackTrace();}
        }

        if (type.equals("gender")) {
            try {
                postData.put("action", "update_user_info");
                postData.put("weight", preferences.getString(SharedPrefsManager.Key.WEIGHT));
                postData.put("height", preferences.getString(SharedPrefsManager.Key.HEIGHT));
                postData.put("gender", gender);

            } catch (JSONException e) {e.printStackTrace();}
        }

        String session = preferences.getString(SharedPrefsManager.Key.SESSION);
        Log.d(TAG, "Current session cookie: " + session);
        ShopRequest shopRequest = new ShopRequest(postData, session);
        shopRequest.delegate = this;
        shopRequest.execute(RequestString.getURL() + "/api/db/update");
    }

    @Override
    public void onCurrencyChanged(int x) {
        shopView.setCurrencyText("x" + numberFormat.format(x));
    }
}
