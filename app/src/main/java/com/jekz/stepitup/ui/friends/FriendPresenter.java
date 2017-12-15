package com.jekz.stepitup.ui.friends;

import com.jekz.stepitup.R;
import com.jekz.stepitup.adapter.FriendsListRecyclerAdapter;
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
        FriendsListPresenter.PendingFriendButtonListener, FriendsListPresenter.FriendClickListener {
    private static final String TAG = FriendPresenter.class.getName();

    private List<Friend> confirmedList = new ArrayList<>();
    private List<Friend> pendingList = new ArrayList<>();

    private List<Friend> activeFriendList = new ArrayList<>();

    private FriendMVP.View view;

    private LoginManager loginManager;


    FriendPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
        confirmedList.add(new Friend("zia", 1, false));
        confirmedList.add(new Friend("bob", 2, false));
        confirmedList.add(new Friend("jun", 4, false));
        confirmedList.add(new Friend("kevin", 5, false));


        pendingList.add(new Friend("bonsy89", 1, true));
        pendingList.add(new Friend("obrenic12", 2, true));
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
    }
}
