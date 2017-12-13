package com.jekz.stepitup.ui;

/**
 * Created by evanalmonte on 12/13/17.
 */

public interface AvatarView {
    void setAvatarImage(int id);

    void setHatImage(int id);

    void setPantsImage(int id);

    void setShirtImage(int id);

    void setShoesImage(int id);

    void animateHat(boolean animate);

    void animateShirt(boolean animate);

    void animateShoes(boolean animate);

    void animatePants(boolean animate);

    void animateAvatar(boolean animate);
}
