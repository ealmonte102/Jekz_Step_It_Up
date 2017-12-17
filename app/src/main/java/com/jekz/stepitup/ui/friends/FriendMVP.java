package com.jekz.stepitup.ui.friends;

import com.jekz.stepitup.ui.AvatarView;
import com.jekz.stepitup.ui.BasePresenter;

/**
 * Created by evanalmonte on 12/13/17.
 */

interface FriendMVP {
    interface View extends AvatarView {
        void reloadFriendsList();

        void showMessage(String s);

        void showSearch(boolean show);

        void showAvatar(boolean show);
    }

    interface Presenter extends BasePresenter<View> {
        void searchUser(String username);

        void loadPending();

        void loadFriends();
    }
}
