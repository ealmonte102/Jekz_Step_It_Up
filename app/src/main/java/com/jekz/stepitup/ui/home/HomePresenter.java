package com.jekz.stepitup.ui.home;

import android.util.Log;
import android.util.Pair;

import com.jekz.stepitup.AvatarRepo;
import com.jekz.stepitup.graphtest.AsyncResponse;
import com.jekz.stepitup.model.avatar.Avatar;
import com.jekz.stepitup.model.item.Item;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.ui.shop.ShopRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by evanalmonte on 12/10/17.
 */

public class HomePresenter implements HomeMVP.Presenter, AsyncResponse {
    private ItemInteractor itemInteractor;
    private Avatar avatar;
    private HomeMVP.View view;

    public HomePresenter(ItemInteractor itemInteractor) {
        this.itemInteractor = itemInteractor;
        avatar = AvatarRepo.getInstance().getAvatar();
        retrieveItem(1, "items");
        retrieveItem(1, "user_data");
    }

    @Override
    public void loadAvatar() {
        Item hat = avatar.getHat();
        Item shirt = avatar.getShirt();
        Item pants = avatar.getPants();
        Item shoes = avatar.getShoes();
        String model = avatar.isMale() ? "male" : "female";
        if (hat != null) {
            view.setHatImage(itemInteractor.getItem(hat.getId()).second);
        }

        if (shirt != null) {
            view.setShirtImage(itemInteractor.getItem(shirt.getId()).second);
        }

        if (pants != null) {
            view.setPantsImage(itemInteractor.getItem(pants.getId()).second);
        }

        if (shoes != null) {
            view.setShoesImage(itemInteractor.getItem(shoes.getId()).second);
        }
        view.setAvatarImage(itemInteractor.getModel(model));
    }

    @Override
    public void accessShop() {
        view.navigateToShop();
    }

    @Override
    public void accessLoginScreen() {
        view.navigateToLoginScreen();
    }

    @Override
    public void accessGraphs() {
        view.displayMessage("Graph functionality not supported yet");
    }

    @Override
    public void onViewAttached(HomeMVP.View view) {
        this.view = view;
        loadAvatar();
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    private void retrieveItem(int userid, String datatype) {

        JSONObject postData = new JSONObject();
        try {
            postData.put("data_type", datatype);
            postData.put("userid", userid);

        } catch (JSONException e) {e.printStackTrace();}

        ShopRequest asyncTask2 = new ShopRequest(postData);
        asyncTask2.delegate = this;
        asyncTask2.execute("https://jekz.herokuapp.com/api/db/retrieve");
    }


    @Override
    public void processFinish(JSONArray output) {
        if (view == null) { return; }
        for (int i = 0; i < output.length(); i++) {

            //Equip or Unequip
            try {
                JSONObject q = output.getJSONObject(i);
                String verify = q.getString("return_data");

                if (verify.equals("equip")) {
                    Log.d("EQUIP", "Equipping items");
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
                            view.setHatImage(hatID);
                        } else {
                            avatar.removeItem(Item.Item_Type.HAT);
                            view.setHatImage(0);
                        }

                        if (shirtid != 0) {
                            Item shirt = itemInteractor.getItem(shirtid).first;
                            int shirtID = itemInteractor.getItem(shirtid).second;
                            avatar.wearItem(shirt);
                            view.setShirtImage(shirtID);
                        } else {
                            avatar.removeItem(Item.Item_Type.SHIRT);
                            view.setShirtImage(0);
                        }

                        if (pantsid != 0) {
                            Item pants = itemInteractor.getItem(pantsid).first;
                            int pantsID = itemInteractor.getItem(pantsid).second;
                            avatar.wearItem(pants);
                            view.setPantsImage(pantsID);
                        } else {
                            avatar.removeItem(Item.Item_Type.PANTS);
                            view.setPantsImage(0);
                        }

                        if (shoesid != 0) {
                            Item shoes = itemInteractor.getItem(shoesid).first;
                            int shoesID = itemInteractor.getItem(shoesid).second;
                            avatar.wearItem(shoes);
                            view.setShoesImage(shoesID);
                        } else {
                            avatar.removeItem(Item.Item_Type.SHOES);
                            view.setShoesImage(0);
                        }

                        //reloadAnimations();
                    }

                }
            } catch (JSONException ignored) {}


            //Retrieve
            try {
                JSONObject r = output.getJSONObject(i);
                int itemcount = r.getInt("count");
                if (itemcount > 0) {
                    int itemid = r.getInt("itemid");

                    Pair<Item, Integer> itemPair;
                    itemPair = itemInteractor.getItem(itemid);
                    if (itemPair != null) {
                        avatar.addItem(itemPair.first);
                    }

                }
            } catch (JSONException ignored) {}

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
                        view.setAvatarImage(modelID);
                    }
                }

            } catch (JSONException ignored) {}


            //User Data
            try {
                JSONObject s = output.getJSONObject(i);
                int filter = s.getInt("total_sessions");

                String gender = s.getString("gender");

                if (gender.equals("male") || gender.equals("female")) {
                    avatar.setMale(gender);
                    int modelID = itemInteractor.getModel(gender);
                    view.setAvatarImage(modelID);
                }

                int hatid = s.getInt("hat");
                int shirtid = s.getInt("shirt");
                int pantsid = s.getInt("pants");
                int shoesid = s.getInt("shoes");

                if (hatid != 0) {
                    Item hat = itemInteractor.getItem(hatid).first;
                    int hatID = itemInteractor.getItem(hatid).second;
                    avatar.wearItem(hat);
                    view.setHatImage(hatID);
                } else {
                    avatar.removeItem(Item.Item_Type.HAT);
                    view.setHatImage(0);
                }

                if (shirtid != 0) {
                    Item shirt = itemInteractor.getItem(shirtid).first;
                    int shirtID = itemInteractor.getItem(shirtid).second;
                    avatar.wearItem(shirt);
                    view.setShirtImage(shirtID);
                } else {
                    avatar.removeItem(Item.Item_Type.SHIRT);
                    view.setShirtImage(0);
                }

                if (pantsid != 0) {
                    Item pants = itemInteractor.getItem(pantsid).first;
                    int pantsID = itemInteractor.getItem(pantsid).second;
                    avatar.wearItem(pants);
                    view.setPantsImage(pantsID);
                } else {
                    avatar.removeItem(Item.Item_Type.PANTS);
                    view.setPantsImage(0);
                }

                if (shoesid != 0) {
                    Item shoes = itemInteractor.getItem(shoesid).first;
                    int shoesID = itemInteractor.getItem(shoesid).second;
                    avatar.wearItem(shoes);
                    view.setShoesImage(shoesID);
                } else {
                    avatar.removeItem(Item.Item_Type.SHOES);
                    view.setShoesImage(0);
                }

                int currency = s.getInt("currency");

                avatar.setCurrency(currency);
                //view.setCurrencyText("x" + NumberFormat.getInstance().format(avatar.getCurrency
                // ()));

            } catch (JSONException e) {e.getMessage();}
        }
    }

}
