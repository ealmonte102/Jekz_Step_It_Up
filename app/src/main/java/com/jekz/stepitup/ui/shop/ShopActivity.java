package com.jekz.stepitup.ui.shop;

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
import android.widget.TextView;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.jekz.stepitup.R;
import com.jekz.stepitup.model.Item;
import com.jekz.stepitup.model.ItemInteractor;
import com.jekz.stepitup.model.ItemListAdapter;

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

    @BindView(R.id.text_currency)
    TextView currencyText;

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
        shopPresenter = new ShopPresenter(this, ItemInteractor.getInstance(getResources()));
        setContentView(R.layout.activity_shop_layout);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopPresenter.reloadAvatar();
        int checked = categoryRadioGroup.getCheckedRadioButtonId();
        categoryRadioGroup.clearCheck();
        categoryRadioGroup.check(checked);
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

    }

    @Override
    public int getResourceForItem(Item item) {
        return shopPresenter.resourceRequested(item);
    }

    @Override
    public void showItems(List<Item> itemList) {
        itemsListAdapater.replaceData(itemList);
    }

    @Override
    public void setAvatarImage(int id) {
        avatarImage.setImageResource(id);
    }

    @Override
    public void setHatImage(int id) {
        hatImage.setImageResource(id);
        equippedHat.setImageResource(id);
    }

    @Override
    public void setPantsImage(int id) {
        pantsImage.setImageResource(id);
        equippedPants.setImageResource(id);
    }

    @Override
    public void setShirtImage(int id) {
        shirtImage.setImageResource(id);
        equippedShirt.setImageResource(id);
    }

    @Override
    public void setShoesImage(int id) {
        shoesImage.setImageResource(id);
        equippedShoes.setImageResource(id);
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
}
