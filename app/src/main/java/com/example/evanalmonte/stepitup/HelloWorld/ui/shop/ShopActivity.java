package com.example.evanalmonte.stepitup.HelloWorld.ui.shop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.evanalmonte.stepitup.HelloWorld.model.Item;
import com.example.evanalmonte.stepitup.HelloWorld.model.ItemInteractor;
import com.example.evanalmonte.stepitup.HelloWorld.model.ItemListAdapter;
import com.example.evanalmonte.stepitup.R;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

import static android.content.ContentValues.TAG;


public class ShopActivity extends Activity implements ItemListAdapter.ShopItemListener, ShopView {
    @BindView(R.id.recycler_view_shop)
    RecyclerView itemsRecyclerView;

    @BindView(R.id.categories)
    RadioGroup categoryRadioGroup;

    @BindView(R.id.avatar_shoes)
    ImageView shoesImage;

    @BindView(R.id.avatar_hat)
    ImageView hatImage;

    @BindView(R.id.avatar_shirt)
    ImageView shirtImage;

    @BindView(R.id.avatar_pants)
    ImageView pantsImage;

    ItemListAdapter itemsListAdapater;
    ShopPresenter shopPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopPresenter = new ShopPresenter(this, ItemInteractor.getInstance(getResources()));
        setContentView(R.layout.activity_shop_layout);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int checked = categoryRadioGroup.getCheckedRadioButtonId();
        categoryRadioGroup.clearCheck();
        categoryRadioGroup.check(checked);
    }

    private void initRecyclerView() {
        itemsListAdapater = new ItemListAdapter(this);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setAdapter(itemsListAdapater);
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(itemsRecyclerView);
    }

    @OnCheckedChanged({R.id.hat, R.id.shirts, R.id.pants, R.id.shoes})
    public void onRadioButtonChanged(CompoundButton button, boolean checked) {
        if (!checked) { return; }
        switch (button.getId()) {
            case R.id.hat:
                shopPresenter.loadHats();
                break;
            case R.id.shirts:
                shopPresenter.loadShirts();
                break;
            case R.id.pants:
                shopPresenter.loadPants();
                break;
            case R.id.shoes:
                shopPresenter.loadShoes();
                break;
        }
    }

    @Override
    public void onItemClicked(Item item) {
        int id = item.getId();
        switch (item.getType()) {
            case PANTS:
                pantsImage.setImageResource(id);
                break;
            case SHOES:
                shoesImage.setImageResource(id);
                break;
            case SHIRT:
                shirtImage.setImageResource(id);
                break;
            case HAT:
                hatImage.setImageResource(id);
                break;
        }
    }

    @Override
    public void showItems(List<Item> itemList) {
        if (itemList != null) {
            Log.d(TAG, itemList.toString());
            itemsListAdapater.replaceData(itemList);
        } else {
            Log.d(TAG, "Item List is Null");
        }
    }
}
