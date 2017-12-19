package com.jekz.stepitup.ui;

import com.jekz.stepitup.customview.AvatarImage;

/**
 * Created by evanalmonte on 12/13/17.
 */

public interface AvatarView {
    void setAvatarImagePart(AvatarImage.AvatarPart part, int id);

    void animateAvatarImagePart(AvatarImage.AvatarPart part, boolean shouldAnimate);
}
