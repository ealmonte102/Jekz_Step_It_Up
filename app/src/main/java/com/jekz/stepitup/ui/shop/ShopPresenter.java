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
            updateItem(1, item.getId());
    }

    boolean checkForItem(Item item) {
        return avatar.hasItem(item);
    }

    void equipItem(Item item) {
        avatar.wearItem(item);
        int resourceID = itemInteractor.getItem(item.getId()).second;
        switch (item.getType()) {
            case HAT:
                shopView.setHatImage(resourceID);
                break;
            case SHIRT:
                shopView.setShirtImage(resourceID);
                break;
            case PANTS:
                shopView.setPantsImage(resourceID);
                break;
            case SHOES:
                shopView.setShoesImage(resourceID);
                break;
        }
        if (item.isAnimated()) {
            reloadAnimations();
        }
        shopView.reloadAdapter();
    }

    void changeGender() {
        boolean isMale = avatar.isMale();
        avatar.setMale(!isMale);
        String modelString = avatar.isMale() ? "male" : "female";
        int modelID = itemInteractor.getModel(modelString);
        shopView.setAvatarImage(modelID);
        reloadAnimations();
    }

    int resourceRequested(Item item) {
        return itemInteractor.getItem(item.getId()).second;
    }

    void unequipItem(Item item) {
        avatar.removeItem(item.getType());
        switch (item.getType()) {
            case HAT:
                shopView.setHatImage(0);
            case SHIRT:
                shopView.setShirtImage(0);
                break;
            case PANTS:
                shopView.setPantsImage(0);
                break;
            case SHOES:
                shopView.setShoesImage(0);
                break;
        }
        shopView.reloadAdapter();
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
    public void onStepDetected() {
        avatar.addCurrency(1);
        shopView.setCurrencyText("x" + NumberFormat.getInstance().format(avatar.getCurrency()));
    }

    @Override
    public void processFinish(JSONArray output) {

        //Retrieve
            for (int i = 0; i < output.length(); i++) {
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


                //User Data
                try {
                    JSONObject s = output.getJSONObject(i);
                    int filter = s.getInt("total_sessions");

                    int currency = s.getInt("currency");

                    avatar.setCurrency(currency);
                    shopView.setCurrencyText(
                            "x" + NumberFormat.getInstance().format(avatar.getCurrency()));
                    shopView.reloadAdapter();


                } catch (JSONException e) {e.printStackTrace();}

                //Update
                try {
                    JSONObject t = output.getJSONObject(i);
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
                } catch (JSONException e) {e.printStackTrace();}
            }
    }


    void updateItem(int userid, int itemid) {

        ShopRequest asyncTask = new ShopRequest(null);

        asyncTask.delegate = this;

        JSONObject postData = new JSONObject();

        try {
            postData.put("data_type", "items");
            postData.put("userid", 1);
            postData.put("itemid", itemid);
            postData.put("amount", 1);

        } catch (JSONException e) {e.printStackTrace();}

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
