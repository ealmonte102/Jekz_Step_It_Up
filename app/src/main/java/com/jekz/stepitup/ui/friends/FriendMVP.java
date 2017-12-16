package com.jekz.stepitup.ui.friends;

import com.jekz.stepitup.ui.AvatarView;
import com.jekz.stepitup.ui.BasePresenter;

/**
 * Created by evanalmonte on 12/13/17.
 */

interface FriendMVP {
    interface View extends AvatarView {
        void reloadFriendsList();

        void showAddedFriend(int x);

        void showRemovedFriend(int x);

        void showMessage(String s);

        void showSearch(boolean show);
    }

    interface Presenter extends BasePresenter<View> {
        void searchUser();

        void loadPending();

        void loadFriends();
    }
}
