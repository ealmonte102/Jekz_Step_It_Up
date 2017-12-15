package com.jekz.stepitup.ui.friends;

import com.jekz.stepitup.data.request.LoginManager;

/**
 * Created by evanalmonte on 12/13/17.
 */

public class FriendPresenter implements FriendMVP.Presenter {
    private FriendMVP.View view;
    private LoginManager loginManager;

    public FriendPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
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
}
