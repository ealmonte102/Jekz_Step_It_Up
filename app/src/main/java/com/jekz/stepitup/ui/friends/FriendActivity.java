package com.jekz.stepitup.ui.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jekz.stepitup.R;
import com.jekz.stepitup.adapter.FriendsListRecyclerAdapter;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.ui.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class FriendActivity extends AppCompatActivity implements FriendMVP.View {
    @BindView(R.id.recycler_view_friends)
    RecyclerView friendsRecyclerView;

    @BindView(R.id.avatar_image)
    AvatarImage avatarImage;

    @BindView(R.id.radio_group_friends)
    RadioGroup radioGroupFreinds;

    @BindView(R.id.search_friends_radio_button)
    RadioButton searchRadio;

    FriendMVP.Presenter presenter;

    FriendsListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_layout);
        ButterKnife.bind(this);

        FriendPresenter presenter = new FriendPresenter(
                new RemoteLoginModel(SharedPrefsManager.getInstance(getApplicationContext())),
                ItemInteractor.getInstance(getResources()));
        this.presenter = presenter;
        adapter = new FriendsListRecyclerAdapter(presenter);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
        radioGroupFreinds.check(R.id.friends_radio_button);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDetached();
    }

    @Override
    public void reloadFriendsList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAddedFriend(int pos) {
        adapter.notifyItemInserted(pos);
    }

    @Override
    public void showRemovedFriend(int x) {
        adapter.notifyItemRemoved(x);
    }


    @Override
    public void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSearch(boolean show) {
        if (show) {
            searchRadio.setVisibility(View.VISIBLE);
            radioGroupFreinds.check(R.id.search_friends_radio_button);
        } else {
            searchRadio.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAvatar(boolean show) {
        if (show) {
            avatarImage.setVisibility(View.VISIBLE);
        } else {
            avatarImage.setVisibility(View.INVISIBLE);
        }
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


    /**
     * Implementation intentionally empty to disable back button
     */
    @Override
    public void onBackPressed() {
    }

    @OnClick(R.id.search_button)
    public void searchButtonClicked(View view) {
        presenter.searchUser();
    }


    @OnCheckedChanged({R.id.friends_radio_button, R.id.pending_friends_radio_button})
    public void onRadioButtonChanged(CompoundButton button, boolean checked) {
        if (!checked) { return; }
        switch (button.getId()) {
            case R.id.friends_radio_button:
                presenter.loadFriends();
                break;
            case R.id.pending_friends_radio_button:
                presenter.loadPending();
                break;
        }
    }
}
