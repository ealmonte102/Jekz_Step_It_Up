package com.jekz.stepitup.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jekz.stepitup.JekzApplication;
import com.jekz.stepitup.R;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.graphtest.GraphActivity;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.model.step.IntervalStepCounter;
import com.jekz.stepitup.model.step.Session;
import com.jekz.stepitup.model.step.StepCounter;
import com.jekz.stepitup.ui.friends.FriendActivity;
import com.jekz.stepitup.ui.login.LoginActivity;
import com.jekz.stepitup.ui.shop.ShopActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeMVP.View, StepCounter
        .StepCounterCallback {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;

    @BindView(R.id.image_avatar_hat_home)
    ImageView hatImage;

    @BindView(R.id.image_avatar_home)
    ImageView avatarImage;

    @BindView(R.id.image_avatar_shirt_home)
    ImageView shirtImage;

    @BindView(R.id.image_avatar_pants_home)
    ImageView pantsImage;

    @BindView(R.id.image_avatar_shoes_home)
    ImageView shoesImage;

    @BindView(R.id.text_username)
    TextView usernameText;

    @BindView(R.id.text_currency)
    TextView currencyText;


    HomeMVP.Presenter presenter;
    IntervalStepCounter stepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stepCounter = ((JekzApplication) (getApplication())).getStepCounter();
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        LoginManager loginManager = new RemoteLoginModel(SharedPrefsManager.getInstance
                (getApplicationContext()));
        presenter = new HomePresenter(ItemInteractor.getInstance(getResources()), loginManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
        stepCounter.startAutoCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        stepCounter.addListener(this);
        presenter.loadAvatar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stepCounter.removeListener(this);
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
            case R.id.profile_menu:
                presenter.accessLoginScreen();
                return true;
            case R.id.shop_menu:
                presenter.accessShop();
                return true;
            case R.id.statistics_menu:
                presenter.accessGraphs();
                return true;
            case R.id.friends_menu:
                presenter.accessFriends();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void setCurrency(String currency) {
        currencyText.setText(currency);
    }

    @Override
    public void setUsername(String username) {
        usernameText.setText(username);
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
    public void onBackPressed() {
        //Implementation intentionally empty to disable back button
    }

    private void navigateToActivity(Context context, Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }


    @Override
    public void onStepDetected(int numOfSteps) {
        Log.i("Autocount Home Step", String.valueOf
                (numOfSteps));
    }

    @Override
    public void onSessionEnded(Session session) {
        Log.i("Autocount Home SESSION", session.toString
                ());
    }
}
