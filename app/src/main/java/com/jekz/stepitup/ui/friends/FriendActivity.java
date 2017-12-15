package com.jekz.stepitup.ui.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jekz.stepitup.R;
import com.jekz.stepitup.adapter.FriendsListRecyclerAdapter;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.model.friend.Friend;
import com.jekz.stepitup.ui.home.HomeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity extends AppCompatActivity implements FriendMVP.View {
    @BindView(R.id.recycler_view_shop)
    RecyclerView friendsRecyclerView;

    @BindView(R.id.avatar_image)
    AvatarImage avatarImage;

    FriendMVP.Presenter presenter;

    //TODO create FriendsListAdapter for recycler view
    FriendsListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_layout);
        ButterKnife.bind(this);
        FriendPresenter presenter = new FriendPresenter(
                new RemoteLoginModel(SharedPrefsManager.getInstance(getApplicationContext())));
        this.presenter = presenter;
        adapter = new FriendsListRecyclerAdapter(presenter);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(adapter);
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

    @Override
    public void setAvatarImagePart(AvatarImage.AvatarPart part, int id) {
        avatarImage.setAvatarPartImage(part, id);
    }

    @Override
    public void animateAvatarImagePart(AvatarImage.AvatarPart part, boolean shouldAnimate) {
        avatarImage.animatePart(part, shouldAnimate);
    }

    @OnClick({R.id.add_button, R.id.remove_button})
    public void friendsButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                presenter.addFriend();
                break;
            case R.id.remove_button:
                presenter.removeFriend();
                break;
        }
    }

    /**
     * Implementation intentionally empty to disable back button
     */
    @Override
    public void onBackPressed() {
    }
}
