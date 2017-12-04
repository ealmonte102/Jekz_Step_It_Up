package com.example.evanalmonte.stepitup.HelloWorld.ui.shop;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
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

    @BindView(R.id.avatar)
    ImageView avatarImage;

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

    public void setHatImage(int id, boolean animateable) {
        hatImage.setImageResource(id);
        if (animateable) {
            ((AnimationDrawable) hatImage.getDrawable()).start();
        }
    }

    public void setShoesImage(int id, boolean animateable) {
        shoesImage.setImageResource(id);
        if (animateable) {
            ((AnimationDrawable) shoesImage.getDrawable()).start();
        }
    }

    public void setShirtImage(int id, boolean animateable) {
        shirtImage.setImageResource(id);
        if (animateable) {
            ((AnimationDrawable) avatarImage.getDrawable()).stop();
            ((AnimationDrawable) shirtImage.getDrawable()).start();
            ((AnimationDrawable) avatarImage.getDrawable()).start();
        }
    }

    public void setPantsImage(int id, boolean animateable) {
        pantsImage.setImageResource(id);
        if (animateable) {
            ((AnimationDrawable) pantsImage.getDrawable()).start();
        }
    }

    @Override
    public void onItemClicked(Item item) {
        shopPresenter.shopItemClicked(item);
    }

    @Override
    public void showItems(List<Item> itemList) {
        if (itemList != null) {
            itemsListAdapater.replaceData(itemList);
        }
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
}
