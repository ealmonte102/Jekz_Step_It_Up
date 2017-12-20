package com.jekz.stepitup.ui.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.jekz.stepitup.R;
import com.jekz.stepitup.customview.AvatarImage;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.model.item.Item;
import com.jekz.stepitup.model.item.ItemInteractor;
import com.jekz.stepitup.model.item.ItemListAdapter;
import com.jekz.stepitup.ui.home.HomeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class ShopActivity extends Activity implements ItemListAdapter.ShopItemListener, ShopView {
    @BindView(R.id.recycler_view_shop)
    RecyclerView itemsRecyclerView;

    @BindView(R.id.categories)
    RadioGroup categoryRadioGroup;

    @BindView(R.id.text_currency)
    TextView currencyText;

    @BindView(R.id.model)
    AvatarImage avatarImage;

    @BindView(R.id.image_equipped_hat)
    ImageView equippedHat;

    @BindView(R.id.image_equipped_shirt)
    ImageView equippedShirt;

    @BindView(R.id.image_equipped_pants)
    ImageView equippedPants;

    @BindView(R.id.image_equipped_shoes)
    ImageView equippedShoes;

    ItemListAdapter itemsListAdapater;
    ShopPresenter shopPresenter;

    private void initRecyclerView() {
        itemsListAdapater = new ItemListAdapter(this);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setAdapter(itemsListAdapater);
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
        snapHelper.attachToRecyclerView(itemsRecyclerView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefsManager manager = SharedPrefsManager.getInstance(getApplicationContext());
        shopPresenter = new ShopPresenter(
                ItemInteractor.getInstance(getResources()),
                SharedPrefsManager.getInstance(getApplicationContext()));
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        shopPresenter.onViewAttached(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shopPresenter.onViewDetached();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopPresenter.reloadImages();
        shopPresenter.reloadAnimations();
        categoryRadioGroup.check(R.id.hat);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setCurrencyText(String text) {
        currencyText.setText(text);
    }

    @Override
    public void reloadAdapter() {
        itemsListAdapater.notifyDataSetChanged();
    }


    @Override
    public void buyItem(Item item) {
        shopPresenter.buyItem(item);
    }

    @Override
    public void equipItem(Item item) {
        shopPresenter.equipItem(item);
    }

    @Override
    public boolean isItemOwned(Item item) {
        return shopPresenter.checkForItem(item);
    }

    @Override
    public void unequipItem(Item item) {
        shopPresenter.unequipItem(item);
    }

    @Override
    public int getResourceForItem(Item item) {
        return shopPresenter.resourceRequested(item);
    }

    @Override
    public boolean isItemEquipped(Item item) {
        return shopPresenter.isItemEquipped(item);
    }

    @Override
    public void showItems(List<Item> itemList) {
        itemsListAdapater.replaceData(itemList);
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

    @OnClick(R.id.button_change_gender)
    public void genderButtonClicked() {
        shopPresenter.changeGender();
    }

    @OnClick(R.id.button_shop_back)
    public void backButtonClicked() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void setAvatarImagePart(AvatarImage.AvatarPart part, int id) {
        avatarImage.setAvatarPartImage(part, id);
        switch (part) {
            case HAT:
                equippedHat.setImageResource(id);
                break;
            case SHIRT:
                equippedShirt.setImageResource(id);
                break;
            case PANTS:
                equippedPants.setImageResource(id);
                break;
            case SHOES:
                equippedShoes.setImageResource(id);
                break;
            default:
                break;
        }
    }

    @Override
    public void animateAvatarImagePart(AvatarImage.AvatarPart part, boolean shouldAnimate) {
        avatarImage.animatePart(part, shouldAnimate);
    }
}
