package com.jekz.stepitup.model.friend;

import com.jekz.stepitup.model.avatar.Avatar;

/**
 * Created by evanalmonte on 12/13/17.
 */

public class Friend {
    private final int id;
    private final String username;
    private Avatar avatar;
    private FriendType friendType;

    public Friend(String username, int id, FriendType type) {
        this.username = username;
        this.id = id;
        this.friendType = type;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public FriendType getFriendType() { return friendType; }

    public void setFriendType(FriendType friendType) {
        this.friendType = friendType;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + username.hashCode();
        result = prime * result + (avatar != null ? avatar.hashCode() : 0);
        result = prime * result + friendType.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Friend)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        final Friend o = (Friend) obj;
        boolean isEqual = o.username.equals(username) &&
                          o.id == id &&
                          (o.friendType.equals(friendType));
        return avatar != null ? avatar.equals(o.avatar) : isEqual;
    }

    public enum FriendType {
        PENDING, SEARCHED, CONFIRMED
    }
}
