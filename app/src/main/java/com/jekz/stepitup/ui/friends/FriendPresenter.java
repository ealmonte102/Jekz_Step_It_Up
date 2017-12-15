package com.jekz.stepitup.ui.friends;

import com.jekz.stepitup.R;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.model.friend.Friend;

import java.util.ArrayList;
import java.util.List;

import static com.jekz.stepitup.adapter.FriendsListRecyclerAdapter.FriendRowView;
import static com.jekz.stepitup.adapter.FriendsListRecyclerAdapter.FriendType;
import static com.jekz.stepitup.adapter.FriendsListRecyclerAdapter.FriendsListPresenter;

/**
 * Created by evanalmonte on 12/13/17.
 */

public class FriendPresenter implements FriendMVP.Presenter, FriendsListPresenter,
        FriendsListPresenter.PendingFriendButtonListener {
    private List<Friend> friendList = new ArrayList<>();
    private List<Friend> pendingList = new ArrayList<>();

    private List<Friend> activeFriendList = new ArrayList<>();

    private FriendMVP.View view;

    private LoginManager loginManager;


    FriendPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
        friendList.add(new Friend("zia", 1));
        friendList.add(new Friend("bob", 2));
        friendList.add(new Friend("jun", 4));
        friendList.add(new Friend("kevin", 5));
        activeFriendList = friendList;
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
        //TODO add friend here, than call view.reloadFriendsList(List<Friends> you get from db);
    }

    @Override
    public void removeFriend() {
        if (view == null) { return; }
        //TODO remove friend here, than call view.reloadFriendsList(List<Friends> you get from db);
    }

    @Override
    public int getItemCount() {
        return activeFriendList.size();
    }

    @Override
    public int getItemViewType(int position) {
        FriendType type;
        if (friendList.get(position).isPending()) {
            type = FriendType.PENDING;
        } else {
            type = FriendType.CONFIRMED;
        }
        return type.ordinal();
    }

    @Override
    public void onBindFriendsRowViewAtPosition(int position, FriendRowView
            rowView, int selectedPosition) {
        if (position == selectedPosition) {
            rowView.setBackgroundColor(R.drawable.shape_selected_friend);
        } else {
            rowView.setBackgroundColor(R.drawable.shape_shop_item_border);
        }
        rowView.setUsername(activeFriendList.get(position).getUsername());
    }

    @Override
    public void onConfirm(int position) {
        //TODO confirm pending friend, if successful change pending to false, reload adapter
    }

    @Override
    public void onDeny(int position) {
        //TODO deny pending friend, if successful, remove friend from list, reload position
    }
}
