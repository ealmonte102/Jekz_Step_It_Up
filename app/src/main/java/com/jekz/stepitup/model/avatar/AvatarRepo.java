package com.jekz.stepitup.model.avatar;

/**
 * Created by evanalmonte on 12/10/17.
 */

public class AvatarRepo {
    private static final AvatarRepo instance = new AvatarRepo();
    Avatar avatar;

    private AvatarRepo() {
        avatar = new Avatar();
    }

    public static AvatarRepo getInstance() {
        return instance;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void resetAvatar() {
        avatar = new Avatar();
    }

    public void addRepoObserver(Avatar.AvatarObserver observer) {
        avatar.addAvatarObserver(observer);
    }

    public void removeRepoObserver(Avatar.AvatarObserver observer) {
        avatar.removeObserver(observer);
    }
}
