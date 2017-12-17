package com.jekz.stepitup.ui.home;

import com.jekz.stepitup.ui.AvatarView;
import com.jekz.stepitup.ui.BasePresenter;

/**
 * Created by evanalmonte on 12/10/17.
 */

interface HomeMVP {
    interface View extends AvatarView {
        void setCurrency(String currency);

        void setUsername(String username);

        void displayMessage(String message);

        void navigateToShop();

        void navigateToGraphs();

        void navigateToLoginScreen();

        void navigateToFriendsScreen();

        void resetAvatar();

        void showLogin();

        void hideLogin();

        void showMessage(String s);
    }

    interface Presenter extends BasePresenter<HomeMVP.View> {
        void loadAvatar();

        void accessShop();

        void accessLoginScreen();

        void accessGraphs();

        void onViewAttached(HomeMVP.View view);

        void onViewDetached();

        void accessFriends();

        void logout();

        void login();
    }

    interface Model {

    }
}
