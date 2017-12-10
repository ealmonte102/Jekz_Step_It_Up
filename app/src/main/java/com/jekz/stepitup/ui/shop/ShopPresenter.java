package com.jekz.stepitup.ui.shop;

import android.util.Pair;

import com.jekz.stepitup.model.avatar.Avatar;
import com.jekz.stepitup.model.item.Item;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.model.step.AndroidStepCounter;
import com.jekz.stepitup.model.step.StepCounter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * Created by evanalmonte on 12/2/17.
 */

public class ShopPresenter implements Presenter, StepCounter.StepCounterCallback,
        com.jekz.stepitup.graphtest.AsyncResponse {
    private ItemInteractor itemInteractor;
    private ShopView shopView;
    private Avatar avatar;
    private NumberFormat numberFormat = NumberFormat.getInstance();
    private AndroidStepCounter androidStepCounter;


    ShopPresenter(AndroidStepCounter androidStepCounter, ItemInteractor instance) {
        this.itemInteractor = instance;
        this.androidStepCounter = androidStepCounter;
        this.androidStepCounter.addListener(this);
        avatar = new Avatar();
        retrieveItem(1, "items");
        retrieveItem(1, "user_data");
        //avatar.addCurrency(13000);
    }

    void reloadImages() {
        Item hat = avatar.getHat();
        Item shirt = avatar.getShirt();
        Item pants = avatar.getPants();
        Item shoes = avatar.getShoes();

        if (hat != null) {
            shopView.setHatImage(itemInteractor.getItem(hat.getId()).second);
        }

        if (shirt != null) {
            shopView.setShirtImage(itemInteractor.getItem(shirt.getId()).second);
        }

        if (pants != null) {
            shopView.setPantsImage(itemInteractor.getItem(pants.getId()).second);
        }

        if (shoes != null) {
            shopView.setShoesImage(itemInteractor.getItem(shoes.getId()).second);
        }
    }

    void reloadAnimations() {
        Item hat = avatar.getHat();
        Item shirt = avatar.getShirt();
        Item pants = avatar.getPants();
        Item shoes = avatar.getShoes();

        if (hat != null && hat.isAnimated()) {
            shopView.animateHat(true);
        }
        if (shirt != null && shirt.isAnimated()) {
            shopView.animateShirt(true);
        }
        if (pants != null && pants.isAnimated()) {
            shopView.animatePants(true);
        }
        if (shoes != null && shoes.isAnimated()) {
            shopView.animateShoes(true);
        }
        shopView.animateAvatar(true);

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

        updateItem(1, item.getId(), "purchase", 0, 0, 0, 0, "nogender");
    }

    boolean checkForItem(Item item) {
        return avatar.hasItem(item);
    }

    void equipItem(Item item) {
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
        updateItem(1, 0, "equip", hatid, shirtid, pantsid, shoesid, "nogender");
    }

    void changeGender() {
        boolean isMale = avatar.isMale();
        if (isMale) {
            updateItem(1, 0, "gender", 0, 0, 0, 0, "female");
        } else {
            updateItem(1, 0, "gender", 0, 0, 0, 0, "male");
        }
    }

    int resourceRequested(Item item) {
        return itemInteractor.getItem(item.getId()).second;
    }

    void unequipItem(Item item) {
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
        updateItem(1, 0, "equip", hatid, shirtid, pantsid, shoesid, "nogender");
    }

    @Override
    public void onViewAttached(ShopView view) {
        this.shopView = view;
        shopView.setCurrencyText("x" + numberFormat.format(avatar.getCurrency()));
    }

    @Override
    public void onViewDetached() {
        this.shopView = null;
    }

    boolean isItemEquipped(Item item) {
        return avatar.isItemEquipped(item);
    }

    void registerStepCounter(boolean b) {
        if (b) {
            androidStepCounter.registerSensor();
        } else {
            androidStepCounter.unregisterSensor();
        }
    }

    @Override
    public void onStepDetected(int x) {
        avatar.addCurrency(1);
        if (shopView != null) {
            shopView.setCurrencyText("x" + NumberFormat.getInstance().format(avatar.getCurrency()));
        }
    }

    @Override
    public void processFinish(JSONArray output) {


            for (int i = 0; i < output.length(); i++) {

                //Equip or Unequip
                try {
                    JSONObject q = output.getJSONObject(i);
                    String verify = q.getString("return_data");

                    if (verify.equals("equip")) {

                        boolean succeed = q.getBoolean("success");

                        if (succeed) {
                            int hatid = q.getInt("hat_");
                            int shirtid = q.getInt("shirt_");
                            int pantsid = q.getInt("pants_");
                            int shoesid = q.getInt("shoes_");

                            if (hatid != 0) {
                                Item hat = itemInteractor.getItem(hatid).first;
                                int hatID = itemInteractor.getItem(hatid).second;
                                avatar.wearItem(hat);
                                shopView.setHatImage(hatID);
                            } else {
                                avatar.removeItem(Item.Item_Type.HAT);
                                shopView.setHatImage(0);
                            }

                            if (shirtid != 0) {
                                Item shirt = itemInteractor.getItem(shirtid).first;
                                int shirtID = itemInteractor.getItem(shirtid).second;
                                avatar.wearItem(shirt);
                                shopView.setShirtImage(shirtID);
                            } else {
                                avatar.removeItem(Item.Item_Type.SHIRT);
                                shopView.setShirtImage(0);
                            }

                            if (pantsid != 0) {
                                Item pants = itemInteractor.getItem(pantsid).first;
                                int pantsID = itemInteractor.getItem(pantsid).second;
                                avatar.wearItem(pants);
                                shopView.setPantsImage(pantsID);
                            } else {
                                avatar.removeItem(Item.Item_Type.PANTS);
                                shopView.setPantsImage(0);
                            }

                            if (shoesid != 0) {
                                Item shoes = itemInteractor.getItem(shoesid).first;
                                int shoesID = itemInteractor.getItem(shoesid).second;
                                avatar.wearItem(shoes);
                                shopView.setShoesImage(shoesID);
                            } else {
                                avatar.removeItem(Item.Item_Type.SHOES);
                                shopView.setShoesImage(0);
                            }

                            reloadAnimations();
                            shopView.reloadAdapter();
                        }

                    }
                } catch (JSONException e) {e.printStackTrace();}


                //Retrieve
                try {
                    JSONObject r = output.getJSONObject(i);
                    //Log.d("Test Object;", r.getInt("count") + "");
                    int itemcount = r.getInt("count");
                    if (itemcount > 0) {
                        int itemid = r.getInt("itemid");

                        Pair<Item, Integer> itemPair;
                        itemPair = itemInteractor.getItem(itemid);
                        if (itemPair != null) {
                            avatar.addItem(itemPair.first);
                            shopView.reloadAdapter();
                        }

                    }
                } catch (JSONException e) {e.printStackTrace();}

                //Gender
                try {
                    JSONObject r = output.getJSONObject(i);
                    //Log.d("Test Object;", r.getInt("count") + "");
                    String gender = r.getString("gender_");
                    boolean verify = r.getBoolean("success");

                    if (verify) {
                        if (gender.equals("male") || gender.equals("female")) {
                            avatar.setMale(gender);
                            int modelID = itemInteractor.getModel(gender);
                            shopView.setAvatarImage(modelID);
                            reloadAnimations();
                        }
                    }

                } catch (JSONException e) {e.printStackTrace();}


                //User Data
                try {
                    JSONObject s = output.getJSONObject(i);
                    int filter = s.getInt("total_sessions");

                    String gender = s.getString("gender");

                    if (gender.equals("male") || gender.equals("female")) {
                        avatar.setMale(gender);
                        int modelID = itemInteractor.getModel(gender);
                        shopView.setAvatarImage(modelID);
                        reloadAnimations();
                    }

                    int hatid = s.getInt("hat");
                    int shirtid = s.getInt("shirt");
                    int pantsid = s.getInt("pants");
                    int shoesid = s.getInt("shoes");

                    if (hatid != 0) {
                        Item hat = itemInteractor.getItem(hatid).first;
                        int hatID = itemInteractor.getItem(hatid).second;
                        avatar.wearItem(hat);
                        shopView.setHatImage(hatID);
                    } else {
                        avatar.removeItem(Item.Item_Type.HAT);
                        shopView.setHatImage(0);
                    }

                    if (shirtid != 0) {
                        Item shirt = itemInteractor.getItem(shirtid).first;
                        int shirtID = itemInteractor.getItem(shirtid).second;
                        avatar.wearItem(shirt);
                        shopView.setShirtImage(shirtID);
                    } else {
                        avatar.removeItem(Item.Item_Type.SHIRT);
                        shopView.setShirtImage(0);
                    }

                    if (pantsid != 0) {
                        Item pants = itemInteractor.getItem(pantsid).first;
                        int pantsID = itemInteractor.getItem(pantsid).second;
                        avatar.wearItem(pants);
                        shopView.setPantsImage(pantsID);
                    } else {
                        avatar.removeItem(Item.Item_Type.PANTS);
                        shopView.setPantsImage(0);
                    }

                    if (shoesid != 0) {
                        Item shoes = itemInteractor.getItem(shoesid).first;
                        int shoesID = itemInteractor.getItem(shoesid).second;
                        avatar.wearItem(shoes);
                        shopView.setShoesImage(shoesID);
                    } else {
                        avatar.removeItem(Item.Item_Type.SHOES);
                        shopView.setShoesImage(0);
                    }

                    int currency = s.getInt("currency");

                    avatar.setCurrency(currency);
                    shopView.setCurrencyText(
                            "x" + NumberFormat.getInstance().format(avatar.getCurrency()));

                    reloadAnimations();
                    shopView.reloadAdapter();


                } catch (JSONException e) {e.printStackTrace();}

                //Update
                try {
                    JSONObject t = output.getJSONObject(i);
                    String verify = t.getString("return_data");

                    if (verify.equals("purchase")) {
                        boolean success = t.getBoolean("success");

                        if (success) {
                            int currency = t.getInt("user_currency");
                            int itemid = t.getInt("itemid_");

                            Item item = itemInteractor.getItem(itemid).first;
                            avatar.addItem(item);
                            avatar.setCurrency(currency);
                            shopView.setCurrencyText(
                                    "x" + NumberFormat.getInstance().format(avatar.getCurrency()));
                            shopView.reloadAdapter();


                        }
                    }

                } catch (JSONException e) {e.printStackTrace();}
            }
    }


    void updateItem(int userid, int itemid, String type, int hatid, int shirtid, int pantsid, int shoesid, String gender) {

        ShopRequest asyncTask = new ShopRequest(null);

        asyncTask.delegate = this;

        JSONObject postData = new JSONObject();

        if (type.equals("purchase")) {
            try {
                postData.put("data_type", "items");
                postData.put("userid", userid);
                postData.put("itemid", itemid);
                postData.put("amount", 1);

            } catch (JSONException e) {e.printStackTrace();}
        }
        if (type.equals("equip")) {
            try {
                postData.put("data_type", "user_data");
                postData.put("userid", userid);
                postData.put("hat", hatid);
                postData.put("shirt", shirtid);
                postData.put("pants", pantsid);
                postData.put("shoes", shoesid);


            } catch (JSONException e) {e.printStackTrace();}
        }

        if (type.equals("gender")) {
            try {
                postData.put("action", "update_user_info");
                postData.put("userid", userid);
                postData.put("weight", 0);
                postData.put("height", 0);
                postData.put("gender", gender);

            } catch (JSONException e) {e.printStackTrace();}
        }

        asyncTask.postData = postData;
        asyncTask.execute("https://jekz.herokuapp.com/api/db/update");
    }

    void retrieveItem(int userid, String datatype) {
        ShopRequest asyncTask2 = new ShopRequest(null);
        asyncTask2.delegate = this;

        JSONObject postData = new JSONObject();
        try {
            postData.put("data_type", datatype);
            postData.put("userid", userid);

        } catch (JSONException e) {e.printStackTrace();}

        asyncTask2.postData = postData;
        asyncTask2.execute("https://jekz.herokuapp.com/api/db/retrieve");
    }
}
