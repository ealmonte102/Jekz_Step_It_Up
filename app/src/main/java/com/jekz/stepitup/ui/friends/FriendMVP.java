package com.jekz.stepitup.ui.friends;

import com.jekz.stepitup.model.friend.Friend;
import com.jekz.stepitup.ui.AvatarView;
import com.jekz.stepitup.ui.BasePresenter;

import java.util.List;

/**
 * Created by evanalmonte on 12/13/17.
 */

interface FriendMVP {
    interface View extends AvatarView {
        void reloadFriendsList(List<Friend> friends);
    }

    interface Presenter extends BasePresenter<View> {
        void addFriend();

        void removeFriend();
    }
}
