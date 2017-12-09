package com.jekz.stepitup.model.item;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jekz.stepitup.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by evanalmonte on 12/2/17.
 */

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
    private ShopItemListener listener;
    private List<Item> itemList = new ArrayList<>();

    public ItemListAdapter(ShopItemListener listener) {
        this.listener = listener;
    }

    public void replaceData(List<Item> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        ItemOwned type = ItemOwned.values()[viewType];
        switch (type) {
            case OWNED:
                itemView = inflater.inflate(R.layout.layout_owned_item, parent, false);
                return new OwnedItemViewHolder(itemView);
            case NOT_OWNED:
                itemView = inflater.inflate(R.layout.layout_unowned_item, parent, false);
                return new UnownedItemViewHolder(itemView);
            case EQUIPPED:
                itemView = inflater.inflate(R.layout.layout_equipped_item, parent, false);
                return new EquippedItemViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemViewType(int position) {
        Item item = itemList.get(position);
        if (listener.isItemEquipped(item)) {
            return ItemOwned.EQUIPPED.ordinal();
        }
        if (listener.isItemOwned(item)) {
            return ItemOwned.OWNED.ordinal();
        }
        return ItemOwned.NOT_OWNED.ordinal();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public enum ItemOwned {
        OWNED, NOT_OWNED, EQUIPPED
    }

    public interface ShopItemListener {
        void buyItem(Item item);

        void equipItem(Item item);

        boolean isItemOwned(Item item);

        void unequipItem(Item item);

        int getResourceForItem(Item item);

        boolean isItemEquipped(Item item);
    }

    abstract class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shop_item_image)
        ImageView itemImage;

        @BindView(R.id.shop_item_name)
        TextView itemName;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            Item item = itemList.get(position);
            int topPadding = 0;
            int leftPadding = 0;
            int bottomPadding = 0;
            switch (item.getType()) {
                case HAT:
                    leftPadding = 10;
                    topPadding = 40;
                    if (item.getName().equals("Paper Bag") || item.getName().equals("Snorkel")) {
                        topPadding -= 5;
                    }
                    if (item.getName().equals("Bunny Ears")) {
                        topPadding += 35;
                    }
                    if (item.getName().equals("Cat Ears")) {
                        topPadding += 23;
                    }
                    if (item.getName().equals("Jester Hat")) {
                        topPadding += 20;
                    }
                    if (item.getName().equals("Helihat") || item.getName().equals("Blue Cap") ||
                        item.getName().equals("Cowboy Hat")) {
                        topPadding += 10;
                    }
                    break;
                case SHIRT:
                    bottomPadding = 20;
                    if (item.getName().endsWith("Dress")) {
                        bottomPadding += 20;
                    }
                    break;
                case SHOES:
                    bottomPadding = 95;
                    break;
                case PANTS:
                    bottomPadding = 70;
                    break;
            }
            bottomPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    bottomPadding,
                    itemView.getResources().getDisplayMetrics());
            topPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    topPadding,
                    itemView.getResources().getDisplayMetrics());
            leftPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    leftPadding,
                    itemView.getResources().getDisplayMetrics());

            itemImage.setPadding(leftPadding, topPadding, 0, bottomPadding);
            itemImage.setImageResource(listener.getResourceForItem(item));
            itemName.setText(item.getName());
        }
    }


    class EquippedItemViewHolder extends ItemViewHolder {
        @BindView(R.id.button_unequip_item)
        Button unequipButton;


        EquippedItemViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.button_unequip_item)
        public void buyClicked(View view) {
            Item item = itemList.get(getAdapterPosition());
            listener.unequipItem(item);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
        }
    }

    class OwnedItemViewHolder extends ItemViewHolder {
        @BindView(R.id.button_equip_item)
        Button equipButton;

        OwnedItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
        }

        @OnClick(R.id.button_equip_item)
        public void equipClicked(View view) {
            Item item = itemList.get(getAdapterPosition());
            listener.equipItem(item);
        }
    }

    class UnownedItemViewHolder extends ItemViewHolder {

        @BindView(R.id.shop_item_price)
        TextView itemPrice;

        @BindView(R.id.shop_item_footprint)
        ImageView footprint;

        UnownedItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.buy_button)
        void itemClicked() {
            Item item = itemList.get(getAdapterPosition());
            listener.buyItem(item);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
            Item item = itemList.get(position);
            itemPrice.setText(String.format(Locale.US, "x%d", item.getPrice()));

        }
    }

}
