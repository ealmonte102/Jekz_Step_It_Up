package com.jekz.stepitup.ui.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jekz.stepitup.R;
import com.jekz.stepitup.ui.home.HomeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_layout);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        //Implementation intentionally empty to disable back button
    }

    @OnClick(R.id.button_shop_back)
    public void backButtonClicked() {
        Toast.makeText(this, "Back Button Pressed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}
