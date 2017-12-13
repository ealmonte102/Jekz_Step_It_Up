package com.jekz.stepitup.model.friend;

import com.jekz.stepitup.model.avatar.Avatar;

/**
 * Created by evanalmonte on 12/13/17.
 */

public class Friend {
    private final Avatar avatar;
    private final String username;

    public Friend(Avatar avatar, String username) {
        this.avatar = avatar;
        this.username = username;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

}
