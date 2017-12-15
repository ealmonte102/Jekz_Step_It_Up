package com.jekz.stepitup.model.friend;

import com.jekz.stepitup.model.avatar.Avatar;

/**
 * Created by evanalmonte on 12/13/17.
 */

public class Friend {
    private final int id;
    private final String username;
    private Avatar avatar;
    private boolean isPending = false;

    public Friend(String username, int id, boolean isPending) {
        this.username = username;
        this.id = id;
        this.isPending = isPending;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {

    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean isPending) {
        this.isPending = isPending;
    }
}
