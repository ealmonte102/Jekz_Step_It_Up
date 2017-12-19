package com.jekz.stepitup.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jekz.stepitup.R;
import com.jekz.stepitup.adapter.SettingsListRecylerAdapter;
import com.jekz.stepitup.data.SharedPrefsManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.recylerview_settings)
    RecyclerView recyclerView;

    SettingsContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        SettingsPresenter presenter = new SettingsPresenter(SharedPrefsManager.getInstance
                (getApplicationContext()));
        this.presenter = presenter;
        SettingsListRecylerAdapter recylerAdapter = new SettingsListRecylerAdapter(presenter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recylerAdapter);
    }
}
