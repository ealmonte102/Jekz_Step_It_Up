package com.jekz.stepitup.ui.friends;

import android.util.Log;

import com.jekz.stepitup.R;
import com.jekz.stepitup.adapter.FriendsListRecyclerAdapter;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.model.friend.Friend;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.ui.shop.ShopRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

import static com.jekz.stepitup.adapter.FriendsListRecyclerAdapter.FriendRowView;
import static com.jekz.stepitup.adapter.FriendsListRecyclerAdapter.FriendsListPresenter;

/**
 * Created by Evan and Kevin
 */

public class FriendPresenter implements FriendMVP.Presenter, FriendsListPresenter,
        FriendsListPresenter.PendingFriendButtonListener, FriendsListPresenter
                .FriendClickListener, com.jekz.stepitup.ui.shop.AsyncResponse {
    private static final String TAG = FriendPresenter.class.getName();

    private HashSet<Friend> confirmedList = new HashSet<>();
    private HashSet<Friend> pendingList = new HashSet<>();

    private HashSet<Friend> activeFriendList = new HashSet<>();

    private FriendMVP.View view;

    private LoginManager loginManager;

    private ItemInteractor itemInteractor;


    FriendPresenter(LoginManager loginManager, ItemInteractor itemInteractor) {
        this.loginManager = loginManager;
        this.itemInteractor = itemInteractor;
        activeFriendList = confirmedList;
    }

    @Override
    public void onViewAttached(FriendMVP.View view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void addFriend() {
        if (view == null) { return; }
        adjustFriends("add_friend");
        //TODO add friend here, than call view.reloadFriendsList(List<Friends> you get from db);
    }

    @Override
    public void removeFriend() {
        if (view == null) { return; }
        adjustFriends("remove_friend");
        //TODO remove friend here, than call view.reloadFriendsList(List<Friends> you get from db);
    }

    @Override
    public void searchUser() {
        //Load search bar with keyboard to type in
        String typedName = "";
        //Run request
        searchUser(typedName);
    }

    @Override
    public void loadPending() {
        activeFriendList = pendingList;
        retrieveFriends("pending_friends");
        view.reloadFriendsList();
    }

    @Override
    public void loadFriends() {
        activeFriendList = confirmedList;
        retrieveFriends("friends");
        view.reloadFriendsList();
    }

    @Override
    public int getItemCount() {
        return activeFriendList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "Position: " + position);
        Friend friend = (Friend) activeFriendList.toArray()[position];
        return friend.getFriendType().ordinal();
    }

    @Override
    public void onBindFriendsRowViewAtPosition(int position, FriendRowView
            rowView, int selectedPosition) {
        Friend friend = (Friend) activeFriendList.toArray()[position];
        rowView.setUsername(friend.getUsername());
        rowView.addFriendClickListener(this);
        if (activeFriendList == pendingList) {
            ((FriendsListRecyclerAdapter.PendingFriendRowView) rowView).addButtonListener(this);
        } else {
            if (position == selectedPosition) {
                rowView.setBackgroundColor(R.drawable.shape_selected_friend);
            } else {
                rowView.setBackgroundColor(R.drawable.shape_shop_item_border);
            }
        }
    }

    @Override
    public void onConfirm(int position) {
        Friend friend = (Friend) activeFriendList.toArray()[position];
        view.showMessage(friend.getUsername() + " has been accepted");
        adjustFriends("accept_friend");
    }

    @Override
    public void onDeny(int position) {
        Friend friend = (Friend) activeFriendList.toArray()[position];
        view.showMessage(friend.getUsername() + " has been denied");
        adjustFriends("deny_friend");
    }

    @Override
    public void onFriendClicked(int position) {
        Friend friend = (Friend) activeFriendList.toArray()[position];
        view.showMessage("Loading " + friend.getUsername() + "'s avatar");
        retrieveFriendEquip(friend.getId());
    }

    @Override
    public void processFinish(JSONObject returnObject) {
        try {
            JSONArray output = returnObject.getJSONArray("rows");
            Log.d(TAG, output.toString());
            for (int i = 0; i < output.length(); i++) {

                // Load Friends
                try {
                    JSONObject q = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("friends")) {
                        int friendID = q.getInt("friendid");
                        String friendName = q.getString("friendname");
                        Log.d(TAG, "FriendId: " + friendID);
                        Log.d(TAG, "Friend Name: " + friendName);
                        Friend newFriend = new Friend(friendName, friendID, Friend.FriendType
                                .CONFIRMED);
                        if (confirmedList.add(newFriend)) {
                            view.showAddedFriend(i);
                        }
                    }

                } catch (JSONException ignored) {}

                // Load Pending Friends
                try {
                    JSONObject q = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("pending_friends")) {
                        int friendID = q.getInt("friendid");
                        String friendName = q.getString("friendname");
                        Log.d(TAG, "Pending FriendId: " + friendID);
                        Log.d(TAG, "Pending Friend Name: " + friendName);
                        Friend newFriend = new Friend(friendName, friendID, Friend.FriendType
                                .PENDING);
                        if (pendingList.add(newFriend)) {
                            view.showAddedFriend(i);
                        }
                    }

                } catch (JSONException ignored) { Log.d(TAG, ignored.getMessage());}

                // Load Search Results
                try {
                    JSONObject q = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("search_user")) {
                        int friendID = q.getInt("friendid");
                        String friendName = q.getString("friendname");
                        Log.d(TAG, "FriendId: " + friendID);
                        Log.d(TAG, "Friend Name: " + friendName);
                        Friend newFriend = new Friend(friendName, friendID, Friend.FriendType
                                .SEARCHED);
                        //ADD TO SEARCH RESULTS TAB
                    }

                } catch (JSONException ignored) {}

                // Remove Friend
                try {
                    JSONObject q = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("remove_friends")) {
                        //TODO: remove friend from list of friends
                    }

                } catch (JSONException ignored) {}

                // Deny Pending Friend
                try {
                    JSONObject q = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("deny_friends")) {
                        //TODO: Remove friend from list of pending friends
                    }

                } catch (JSONException ignored) {}

                // Accept Pending Friend
                try {
                    JSONObject q = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("accept_friends")) {
                        //TODO: Remove friend from list of pending friends and add to list of
                    }

                } catch (JSONException ignored) {}

                //Retrieve Friend Data
                try {
                    JSONObject s = output.getJSONObject(i);
                    String verify = returnObject.getString("return_data");

                    if (verify.equals("user_data")) {
                        String gender = s.getString("gender");
                        if (gender.equals("male") || gender.equals("female")) {
                            int modelID = itemInteractor.getModel(gender);
                            view.setAvatarImagePart(AvatarImage.AvatarPart.MODEL, modelID);
                            view.animateAvatarImagePart(AvatarImage.AvatarPart.MODEL, true);
                        }
                        int hatid = s.getInt("hat");
                        int shirtid = s.getInt("shirt");
                        int pantsid = s.getInt("pants");
                        int shoesid = s.getInt("shoes");
                        int hatID = itemInteractor.getItem(hatid).second;
                        view.setAvatarImagePart(AvatarImage.AvatarPart.HAT, hatID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.HAT, true);
                        int shirtID = itemInteractor.getItem(shirtid).second;
                        view.setAvatarImagePart(AvatarImage.AvatarPart.SHIRT, shirtID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.SHIRT, true);
                        int pantsID = itemInteractor.getItem(pantsid).second;
                        view.setAvatarImagePart(AvatarImage.AvatarPart.PANTS, pantsID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.PANTS, true);
                        int shoesID = itemInteractor.getItem(shoesid).second;
                        view.setAvatarImagePart(AvatarImage.AvatarPart.SHOES, shoesID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.SHOES, true);
                    }

                } catch (JSONException ignored) {}
            }

        } catch (JSONException ignored) { }
    }

    private void retrieveFriends(String datatype) {
        String session = loginManager.getSession();

        JSONObject postData = new JSONObject();
        try {
            postData.put("action", datatype);

        } catch (JSONException e) {e.printStackTrace();}

        ShopRequest asyncTask = new ShopRequest(postData, session);
        asyncTask.delegate = this;
        asyncTask.execute("https://jekz.herokuapp.com/api/db/retrieve");
    }

    private void searchUser(String searchName) {
        String session = loginManager.getSession();

        JSONObject postData = new JSONObject();
        try {
            postData.put("action", "search_user");

        } catch (JSONException e) {e.printStackTrace();}

        ShopRequest asyncTask = new ShopRequest(postData, session);
        asyncTask.delegate = this;
        asyncTask.execute("https://jekz.herokuapp.com/api/db/retrieve");
    }

    // TYPE = add_friend, remove_friend, deny_friend or accept_friend.
    private void adjustFriends(String type) {
        String session = loginManager.getSession();

        JSONObject postData = new JSONObject();

        try {
            postData.put("action", type);

        } catch (JSONException e) {e.printStackTrace();}


        ShopRequest asyncTask = new ShopRequest(postData, session);
        asyncTask.delegate = this;
        asyncTask.execute("https://jekz.herokuapp.com/api/db/update");
    }

    private void retrieveFriendEquip(int id) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("action", "user_data");
            postData.put("userid", id);
        } catch (JSONException e) {e.printStackTrace();}

        ShopRequest asyncTask = new ShopRequest(postData, null);
        asyncTask.delegate = this;
        asyncTask.execute("https://jekz.herokuapp.com/api/db/retrieve");
    }
}
