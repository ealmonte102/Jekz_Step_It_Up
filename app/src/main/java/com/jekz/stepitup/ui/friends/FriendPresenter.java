package com.jekz.stepitup.ui.friends;

import android.util.Log;

import com.jekz.stepitup.R;
import com.jekz.stepitup.adapter.FriendsListRecyclerAdapter;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.graphtest.AsyncResponse;
import com.jekz.stepitup.model.friend.Friend;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.ui.shop.ShopRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.jekz.stepitup.adapter.FriendsListRecyclerAdapter.FriendRowView;
import static com.jekz.stepitup.adapter.FriendsListRecyclerAdapter.FriendType;
import static com.jekz.stepitup.adapter.FriendsListRecyclerAdapter.FriendsListPresenter;

/**
 * Created by evanalmonte on 12/13/17.
 */

public class FriendPresenter implements FriendMVP.Presenter, FriendsListPresenter,
        FriendsListPresenter.PendingFriendButtonListener, FriendsListPresenter
                .FriendClickListener, AsyncResponse {
    private static final String TAG = FriendPresenter.class.getName();

    private List<Friend> confirmedList = new ArrayList<>();
    private List<Friend> pendingList = new ArrayList<>();

    private List<Friend> activeFriendList = new ArrayList<>();

    private FriendMVP.View view;

    private LoginManager loginManager;

    private ItemInteractor itemInteractor;


    FriendPresenter(LoginManager loginManager, ItemInteractor itemInteractor) {
        this.loginManager = loginManager;
        this.itemInteractor = itemInteractor;
        activeFriendList = confirmedList;
        /*confirmedList.add(new Friend("zia", 1, false));
        confirmedList.add(new Friend("bob", 2, false));
        confirmedList.add(new Friend("jun", 4, false));
        confirmedList.add(new Friend("kevin", 5, false));


        pendingList.add(new Friend("bonsy89", 1, true));
        pendingList.add(new Friend("obrenic12", 2, true));
            */
    }

    @Override
    public void onViewAttached(FriendMVP.View view) {
        this.view = view;
        retrieveFriends("friends");
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void addFriend() {
        if (view == null) { return; }
        //TODO add friend here, than call view.reloadFriendsList(List<Friends> you get from db);
    }

    @Override
    public void removeFriend() {
        if (view == null) { return; }
        //TODO remove friend here, than call view.reloadFriendsList(List<Friends> you get from db);
    }

    @Override
    public void loadPending() {
        activeFriendList = pendingList;
        view.reloadFriendsList();
    }

    @Override
    public void loadFriends() {
        activeFriendList = confirmedList;
        view.reloadFriendsList();
    }

    @Override
    public int getItemCount() {
        return activeFriendList.size();
    }

    @Override
    public int getItemViewType(int position) {
        FriendType type;
        if (activeFriendList.get(position).isPending()) {
            type = FriendType.PENDING;
        } else {
            type = FriendType.CONFIRMED;
        }
        return type.ordinal();
    }

    @Override
    public void onBindFriendsRowViewAtPosition(int position, FriendRowView
            rowView, int selectedPosition) {
        rowView.setUsername(activeFriendList.get(position).getUsername());
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
        view.showMessage(activeFriendList.get(position).getUsername() + " has been accepted");
        //TODO confirm pending friend, if successful change pending to false, reload adapter
    }

    @Override
    public void onDeny(int position) {
        view.showMessage(activeFriendList.get(position).getUsername() + " has been denied");
        //TODO deny pending friend, if successful, remove friend from list, reload position
    }

    @Override
    public void onFriendClicked(int position) {
        view.showMessage("Loading " + activeFriendList.get(position).getUsername() + "'s avatar");
        retrieveFriendEquip(activeFriendList.get(position).getId());
    }

    @Override
    public void processFinish(JSONArray output) {
        Log.d(TAG, "Output: " + output.toString());
        for (int i = 0; i < output.length(); i++) {

            // Friends
            try {
                JSONObject q = output.getJSONObject(i);
                int friendID = q.getInt("friendid");
                String friendName = q.getString("friendname");
                Log.d(TAG, "FriendId: " + friendID);
                Log.d(TAG, "Friend Name: " + friendName);
                Friend newFriend = new Friend(friendName, friendID, false);
                confirmedList.add(newFriend);
                view.showAddedFriend(i);
            } catch (JSONException e) {}

            // Pending Friends
            try {
                JSONObject q = output.getJSONObject(i);
                String verify = q.getString("return_data");
                if (verify.equals("pending_friends")) {
                    int friendID = q.getInt("friendid");
                    String friendName = q.getString("friendname");
                    Friend newFriend = new Friend(friendName, friendID, true);
                    pendingList.add(newFriend);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //User Data
            try {
                JSONObject s = output.getJSONObject(i);
                int filter = s.getInt("total_sessions");

                String gender = s.getString("gender");

                if (gender.equals("male") || gender.equals("female")) {
                    int modelID = itemInteractor.getModel(gender);
                    view.setAvatarImagePart(AvatarImage.AvatarPart.MODEL, modelID);
                }

                int hatid = s.getInt("hat");
                int shirtid = s.getInt("shirt");
                int pantsid = s.getInt("pants");
                int shoesid = s.getInt("shoes");

                int hatID = itemInteractor.getItem(hatid).second;
                view.setAvatarImagePart(AvatarImage.AvatarPart.HAT, hatID);
                int shirtID = itemInteractor.getItem(shirtid).second;
                view.setAvatarImagePart(AvatarImage.AvatarPart.SHIRT, shirtID);
                int pantsID = itemInteractor.getItem(pantsid).second;
                view.setAvatarImagePart(AvatarImage.AvatarPart.PANTS, pantsID);
                int shoesID = itemInteractor.getItem(shoesid).second;
                view.setAvatarImagePart(AvatarImage.AvatarPart.SHOES, shoesID);
            } catch (JSONException e) {
                e.getMessage();
            }
        }
    }

    void retrieveFriends(String datatype) {
        String session = loginManager.getSession();

        JSONObject postData = new JSONObject();
        try {
            postData.put("action", datatype);

        } catch (JSONException e) {e.printStackTrace();}

        ShopRequest asyncTask = new ShopRequest(postData, session);
        asyncTask.delegate = this;
        asyncTask.execute("https://jekz.herokuapp.com/api/db/retrieve");
    }

    void retrieveFriendEquip(int id) {
        JSONObject postData = new JSONObject();
        try {
            postData.put("data_type", "user_data");
            postData.put("userid", id);
        } catch (JSONException e) {e.printStackTrace();}

        ShopRequest asyncTask = new ShopRequest(postData, null);
        asyncTask.delegate = this;
        asyncTask.execute("https://jekz.herokuapp.com/api/db/retrieve");
    }
}
