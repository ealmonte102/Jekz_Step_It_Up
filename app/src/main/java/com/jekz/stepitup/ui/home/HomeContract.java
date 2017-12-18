package com.jekz.stepitup.ui.home;

import com.jekz.stepitup.ui.AvatarView;
import com.jekz.stepitup.ui.BasePresenter;

/**
 * Created by evanalmonte on 12/10/17.
 */

interface HomeContract {
    interface View extends AvatarView {
        void setCurrency(String currency);

        void setUsername(String username);

        void displayMessage(String message);

        void navigateToShop();

        void navigateToGraphs();

        void navigateToLoginScreen();

        void navigateToFriendsScreen();

        void showMessage(String s);

        void disableSession();

        void enableSession();
    }

    interface Presenter extends BasePresenter<HomeContract.View> {
        void loadAvatar();

        void accessShop();

        void accessLoginScreen();

        void accessGraphs();

        void onViewAttached(HomeContract.View view);

        void onViewDetached();

        void accessFriends();

        void logout();

        void login();

        void endSession();

        void startSession();
    }

    interface Model {

    }
}
