package com.jekz.stepitup.ui.home;

import com.jekz.stepitup.ui.BasePresenter;

/**
 * Created by evanalmonte on 12/10/17.
 */

interface HomeMVP {
    interface View {

        void setAvatarImage(int id);

        void setHatImage(int id);

        void setPantsImage(int id);

        void setShirtImage(int id);

        void setShoesImage(int id);

        void displayMessage(String message);

        void navigateToShop();

        void navigateToGraphs();

        void navigateToLoginScreen();

        void navigateToFriendsScreen();
    }

    interface Presenter extends BasePresenter<HomeMVP.View> {
        void loadAvatar();

        void accessShop();

        void accessLoginScreen();

        void accessGraphs();

        void onViewAttached(HomeMVP.View view);

        void onViewDetached();

        void accessFriends();
    }

    interface Model {

    }
}
