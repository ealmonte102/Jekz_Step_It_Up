package com.jekz.stepitup.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jekz.stepitup.JekzApplication;
import com.jekz.stepitup.R;
import com.jekz.stepitup.customview.AvatarImage;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.graphtest.GraphActivity;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.model.step.ManualStepCounter;
import com.jekz.stepitup.ui.friends.FriendActivity;
import com.jekz.stepitup.ui.login.LoginActivity;
import com.jekz.stepitup.ui.settings.SettingsActivity;
import com.jekz.stepitup.ui.shop.ShopActivity;
import com.jekz.stepitup.util.SessionSaver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jekz.stepitup.customview.AvatarImage.AvatarPart;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;

    @BindView(R.id.avatar_image_home)
    AvatarImage avatarImage;

    @BindView(R.id.text_username)
    TextView usernameText;

    @BindView(R.id.text_currency)
    TextView currencyText;

    @BindView(R.id.button_logout)
    Button logoutButton;

    @BindView(R.id.button_session)
    ToggleButton sessionButton;

    @BindView(R.id.text_steps)
    TextView stepText;

    HomeContract.Presenter presenter;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton
            .OnCheckedChangeListener() {


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                presenter.startSession();
            } else {
                presenter.endSession();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ManualStepCounter stepCounter = ((JekzApplication) (getApplication())).getStepCounter();
        SessionSaver sessionSaver = ((JekzApplication) (getApplication())).getSessionSaver();
        LoginManager loginManager = new RemoteLoginModel(SharedPrefsManager.getInstance
                (getApplicationContext()));
        presenter = new HomePresenter(ItemInteractor.getInstance(getResources()),
                loginManager,
                stepCounter,
                sessionSaver);
        sessionButton.setOnCheckedChangeListener(checkedChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadAvatar();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDetached();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shop_menu:
                presenter.accessShop();
                return true;
            case R.id.statistics_menu:
                presenter.accessGraphs();
                return true;
            case R.id.friends_menu:
                presenter.accessFriends();
                return true;
            case R.id.profile_menu:
                presenter.accessProfile();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setCurrency(String currency) {
        currencyText.setText(currency);
    }

    @Override
    public void setUsername(String username) {
        usernameText.setText(username);
    }

    @Override
    public void setSteps(String stepString) {
        stepText.setText(stepString);
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToShop() {
        navigateToActivity(this, ShopActivity.class);
    }

    @Override
    public void navigateToGraphs() {
        navigateToActivity(this, GraphActivity.class);
    }

    @Override
    public void navigateToLoginScreen() {
        navigateToActivity(this, LoginActivity.class);
    }

    @Override
    public void navigateToFriendsScreen() {
        navigateToActivity(this, FriendActivity.class);
    }

    @Override
    public void navigateToProfile() {
        navigateToActivity(this, SettingsActivity.class);
    }

    private void navigateToActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void disableSession() {
        sessionButton.setOnCheckedChangeListener(null);
        sessionButton.setChecked(true);
        sessionButton.setOnCheckedChangeListener(checkedChangeListener);
    }

    @Override
    public void enableSession() {
        sessionButton.setOnCheckedChangeListener(null);
        sessionButton.setChecked(false);
        sessionButton.setOnCheckedChangeListener(checkedChangeListener);
    }

    @Override
    public void onBackPressed() {
        //Implementation intentionally empty to disable back button
    }

    @Override
    public void setAvatarImagePart(AvatarPart part, int id) {
        avatarImage.setAvatarPartImage(part, id);
    }

    @Override
    public void animateAvatarImagePart(AvatarPart part, boolean shouldAnimate) {
        avatarImage.animatePart(part, shouldAnimate);
    }

    @OnClick({R.id.button_logout, R.id.button_session})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_logout:
                presenter.logout();
                break;
        }
    }
}
