package com.jekz.stepitup.ui.friends;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.jekz.stepitup.R;
import com.jekz.stepitup.model.item.Item;
import com.jekz.stepitup.ui.shop.ShopPresenter;

/**
 * Created by evanalmonte on 12/13/17.
 */

public class AvatarImage extends ConstraintLayout {
    private static final String TAG = ShopPresenter.class.getName();

    ImageView modelImage;
    ImageView hatImage;
    ImageView shirtImage;
    ImageView pantsImage;
    ImageView shoesImage;

    public AvatarImage(Context context) {
        super(context);
    }

    public AvatarImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.view_avatarimage, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImage);
        int modelId = a.getResourceId(R.styleable.AvatarImage_avatar, 0);
        int avatarHatId = a.getResourceId(R.styleable.AvatarImage_hat, 0);
        int avatarShirtId = a.getResourceId(R.styleable.AvatarImage_shirt, 0);
        int avatarPantsId = a.getResourceId(R.styleable.AvatarImage_pants, 0);
        int avatarShoesId = a.getResourceId(R.styleable.AvatarImage_shoes, 0);

        modelImage = findViewById(R.id.avatar);
        hatImage = findViewById(R.id.avatar_hat);
        shirtImage = findViewById(R.id.avatar_shirt);
        pantsImage = findViewById(R.id.avatar_pants);
        shoesImage = findViewById(R.id.avatar_shoes);

        modelImage.setImageResource(modelId);
        hatImage.setImageResource(avatarHatId);
        shirtImage.setImageResource(avatarShirtId);
        pantsImage.setImageResource(avatarPantsId);
        shoesImage.setImageResource(avatarShoesId);
        a.recycle();
    }

    public AvatarImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static AvatarPart getFromItem(Item.Item_Type type) {
        AvatarPart part = null;
        switch (type) {
            case HAT:
                part = AvatarPart.HAT;
                break;
            case SHIRT:
                part = AvatarPart.SHIRT;
                break;
            case PANTS:
                part = AvatarPart.PANTS;
                break;
            case SHOES:
                part = AvatarPart.SHOES;
        }

        return part;
    }

    public void setAvatarPartImage(AvatarPart part, int partID) {
        ImageView selectedPart = modelImage;
        switch (part) {
            case MODEL:
                selectedPart = modelImage;
                break;
            case HAT:
                selectedPart = hatImage;
                break;
            case SHIRT:
                selectedPart = shirtImage;
                break;
            case PANTS:
                selectedPart = pantsImage;
                break;
            case SHOES:
                selectedPart = shoesImage;
                break;
        }
        selectedPart.setImageResource(partID);
    }

    public void animatePart(AvatarPart part, boolean animate) {
        ImageView selectedPart = modelImage;
        switch (part) {
            case MODEL:
                selectedPart = modelImage;
                break;
            case HAT:
                selectedPart = hatImage;
                break;
            case SHIRT:
                selectedPart = shirtImage;
                break;
            case PANTS:
                selectedPart = pantsImage;
                break;
            case SHOES:
                selectedPart = shoesImage;
                break;
        }
        try {
            AnimationDrawable animationDrawable = (AnimationDrawable) selectedPart.getDrawable();
            if (animate) {
                restartAnimation(animationDrawable);
            } else {
                animationDrawable.stop();
            }
        } catch (ClassCastException exception) {
            Log.d(TAG, selectedPart.toString() + " is not animatable");
        }
    }

    private void restartAnimation(AnimationDrawable animation) {
        animation.stop();
        animation.start();
    }

    public enum AvatarPart {
        MODEL,
        HAT,
        SHIRT,
        PANTS,
        SHOES
    }
}
