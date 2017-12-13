package com.jekz.stepitup.ui.friends;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.jekz.stepitup.R;
import com.jekz.stepitup.model.friend.Friend;
import com.jekz.stepitup.ui.home.HomeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity extends AppCompatActivity implements FriendMVP.View {
    @BindView(R.id.recycler_view_shop)
    RecyclerView friendsRecyclerView;

    @BindView(R.id.avatar)
    ImageView avatarImage;

    @BindView(R.id.avatar_hat)
    ImageView hatImage;

    @BindView(R.id.avatar_shirt)
    ImageView shirtImage;

    @BindView(R.id.avatar_pants)
    ImageView pantsImage;

    @BindView(R.id.avatar_shoes)
    ImageView shoesImage;

    //TODO create FriendsListAdapter for recycler view
    //FriendsListAdapter friendsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_layout);
        ButterKnife.bind(this);
    }

    /**
     * Implementation intentionally empty to disable back button
     */
    @Override
    public void onBackPressed() {
    }

    @Override
    public void setAvatarImage(int id) {
        avatarImage.setImageResource(id);
    }

    @Override
    public void setHatImage(int id) {
        hatImage.setImageResource(id);
    }

    @Override
    public void setPantsImage(int id) {
        pantsImage.setImageResource(id);
    }

    @Override
    public void setShirtImage(int id) {
        shirtImage.setImageResource(id);
    }

    @Override
    public void setShoesImage(int id) {
        shoesImage.setImageResource(id);
    }

    @Override
    public void animateHat(boolean animate) {
        AnimationDrawable animationDrawable = (AnimationDrawable) hatImage.getDrawable();
        if (animate) {
            restartAnimation(animationDrawable);
        } else {
            animationDrawable.stop();
        }
    }

    @Override
    public void animateShirt(boolean animate) {
        AnimationDrawable animationDrawable = (AnimationDrawable) shirtImage.getDrawable();
        if (animate) {
            restartAnimation(animationDrawable);
        } else {
            animationDrawable.stop();
        }
    }

    @Override
    public void animateShoes(boolean animate) {
        AnimationDrawable animationDrawable = (AnimationDrawable) shoesImage.getDrawable();
        if (animate) {
            restartAnimation(animationDrawable);
        } else {
            animationDrawable.stop();
        }
    }

    @Override
    public void animatePants(boolean animate) {
        AnimationDrawable animationDrawable = (AnimationDrawable) pantsImage.getDrawable();
        if (animate) {
            restartAnimation(animationDrawable);
        } else {
            animationDrawable.stop();
        }
    }

    @Override
    public void animateAvatar(boolean animate) {
        AnimationDrawable animationDrawable = (AnimationDrawable) avatarImage.getDrawable();
        if (animate) {
            restartAnimation(animationDrawable);
        } else {
            animationDrawable.stop();
        }
    }

    private void restartAnimation(AnimationDrawable animation) {
        animation.stop();
        animation.start();
    }

    @Override
    public void reloadFriendsList(List<Friend> friends) {
        //TODO update friendsListAdapter's data with argument.
    }

    @OnClick(R.id.button_shop_back)
    public void backButtonClicked() {
        Toast.makeText(this, "Back Button Pressed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
