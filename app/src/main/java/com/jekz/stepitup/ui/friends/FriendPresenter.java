package com.jekz.stepitup.ui.friends;

import android.util.Log;

import com.jekz.stepitup.R;
import com.jekz.stepitup.adapter.FriendsListRecyclerAdapter;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.model.friend.Friend;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.ui.shop.AsyncResponse;
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
                .FriendClickListener, AsyncResponse, FriendsListPresenter.SearchClickListener {
    private static final String TAG = FriendPresenter.class.getName();

    private HashSet<Friend> confirmedList = new HashSet<>();
    private HashSet<Friend> pendingList = new HashSet<>();
    private HashSet<Friend> searchList = new HashSet<>();
    private HashSet<Friend> activeFriendList = new HashSet<>();

    private int friendEditedPositon = -1;

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
    public void searchUser() {
        activeFriendList = searchList;
        view.showSearch(true);
        view.reloadFriendsList();
        searchUser("");
    }

    @Override
    public void loadPending() {
        activeFriendList = pendingList;
        retrieveFriends("pending_friends");
        view.reloadFriendsList();
        view.showSearch(false);
    }

    @Override
    public void loadFriends() {
        activeFriendList = confirmedList;
        confirmedList.clear();
        retrieveFriends("friends");
        view.reloadFriendsList();
        view.showSearch(false);
    }

    @Override
    public int getItemCount() {
        return activeFriendList.size();
    }

    @Override
    public int getItemViewType(int position) {
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
        } else if (activeFriendList == searchList) {
            ((FriendsListRecyclerAdapter.SearchFriendRowView) rowView).addSearchClickListener(this);
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
        friendEditedPositon = position;
        adjustFriends("accept_friend", friend.getId());
    }

    @Override
    public void onDeny(int position) {
        Friend friend = (Friend) activeFriendList.toArray()[position];
        friendEditedPositon = position;
        view.showMessage(friend.getUsername() + " has been denied");
        adjustFriends("deny_friend", friend.getId());
    }

    @Override
    public void onFriendClicked(int position) {
        Friend friend = (Friend) activeFriendList.toArray()[position];
        view.showMessage("Loading " + friend.getUsername() + "'s avatar");
        retrieveFriendEquip(friend.getId());
    }

    @Override
    public void onRemoveClicked(int position) {
        Friend friend = (Friend) activeFriendList.toArray()[position];
        friendEditedPositon = position;
        view.showMessage(friend.getUsername() + " has been removed");
        adjustFriends("remove_friend", friend.getId());
    }

    @Override
    public void onAddFriend(int position) {
        Friend friendToRequest = (Friend) activeFriendList.toArray()[position];
        adjustFriends("request_friend", friendToRequest.getId());
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
            postData.put("username", searchName);

        } catch (JSONException e) {e.printStackTrace();}

        ShopRequest asyncTask = new ShopRequest(postData, session);
        asyncTask.delegate = this;
        asyncTask.execute("https://jekz.herokuapp.com/api/db/retrieve");
    }

    // TYPE = add_friend, remove_friend, deny_friend or accept_friend.
    private void adjustFriends(String type, int id) {
        String session = loginManager.getSession();

        JSONObject postData = new JSONObject();

        try {
            postData.put("action", type);
            postData.put("friendid", id);

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

    @Override
    public void processFinish(JSONObject returnObject) {
        try {
            JSONArray output = returnObject.getJSONArray("rows");
            String actionType = returnObject.getString("return_data");
            Log.d(TAG, output.toString());
            Log.d(TAG, "Action type: " + actionType);
            for (int i = 0; i < output.length(); i++) {
                JSONObject q = output.getJSONObject(i);
                switch (actionType) {
                    case "friends": {
                        int friendID = q.getInt("friendid");
                        String friendName = q.getString("friendname");
                        Log.d(TAG, "Friend: " + friendID + " - " + friendName);
                        Friend newFriend = new Friend(friendName, friendID, Friend.FriendType
                                .CONFIRMED);
                        if (confirmedList.add(newFriend)) {
                            view.showAddedFriend(activeFriendList.size() - 1);
                        }
                        break;
                    }
                    case "pending_friends": {
                        int friendID = q.getInt("friendid");
                        String friendName = q.getString("friendname");
                        Log.d(TAG, "Pending friend: " + friendID + " - " + friendName);
                        Friend newFriend = new Friend(friendName, friendID, Friend.FriendType
                                .PENDING);
                        if (pendingList.add(newFriend)) {
                            view.showAddedFriend(activeFriendList.size() - 1);
                        }
                        break;
                    }
                    case "search_user": {
                        int friendID = q.getInt("userid");
                        String friendName = q.getString("username");
                        Log.d(TAG, "Search friend: " + friendID + " - " + friendName);
                        Friend searchedFriend = new Friend(friendName, friendID, Friend.FriendType
                                .SEARCHED);
                        searchList.add(searchedFriend);
                        view.showAddedFriend(activeFriendList.size() - 1);
                        break;
                    }
                    case "remove_friend": {
                        Friend friendToAccept = (Friend) activeFriendList.toArray()
                                [friendEditedPositon];
                        confirmedList.remove(friendToAccept);
                        view.showRemovedFriend(friendEditedPositon);
                        break;
                    }
                    case "accept_friend": {
                        if (q.getBoolean("success")) {
                            Friend friendToAccept = (Friend) activeFriendList.toArray()
                                    [friendEditedPositon];

                            pendingList.remove(friendToAccept);
                            friendToAccept.setFriendType(Friend.FriendType.CONFIRMED);
                            confirmedList.add(friendToAccept);
                            view.showRemovedFriend(friendEditedPositon);
                            view.showMessage("Friend accepted");
                        } else {
                            Log.d(TAG, "Could not add friend");
                            view.showMessage("You can not add yourself");
                        }
                        break;
                    }
                    case "deny_friend": {
                        Friend friendToDeny = (Friend) activeFriendList.toArray()
                                [friendEditedPositon];
                        pendingList.remove(friendToDeny);
                        view.showRemovedFriend(friendEditedPositon);
                        break;
                    }
                    case "user_data": {
                        String gender = q.getString("gender");
                        int modelID = itemInteractor.getModel(gender);
                        view.setAvatarImagePart(AvatarImage.AvatarPart.MODEL, modelID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.MODEL, true);
                        int hatid = q.getInt("hat");
                        int shirtid = q.getInt("shirt");
                        int pantsid = q.getInt("pants");
                        int shoesid = q.getInt("shoes");
                        int hatID = itemInteractor.getItem(hatid).second;
                        int shirtID = itemInteractor.getItem(shirtid).second;
                        int pantsID = itemInteractor.getItem(pantsid).second;
                        int shoesID = itemInteractor.getItem(shoesid).second;
                        view.setAvatarImagePart(AvatarImage.AvatarPart.HAT, hatID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.HAT, true);
                        view.setAvatarImagePart(AvatarImage.AvatarPart.SHIRT, shirtID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.SHIRT, true);
                        view.setAvatarImagePart(AvatarImage.AvatarPart.PANTS, pantsID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.PANTS, true);
                        view.setAvatarImagePart(AvatarImage.AvatarPart.SHOES, shoesID);
                        view.animateAvatarImagePart(AvatarImage.AvatarPart.SHOES, true);
                        view.showQuestonMark(false);
                        break;
                    }
                }
            }
        } catch (JSONException ignored) {}
    }
}
