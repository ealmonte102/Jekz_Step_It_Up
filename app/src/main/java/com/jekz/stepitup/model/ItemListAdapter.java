package com.jekz.stepitup.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (listener.isItemOwned(itemList.get(position))) {
            return ItemOwned.OWNED.ordinal();
        }
        return ItemOwned.NOT_OWNED.ordinal();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public enum ItemOwned {
        OWNED, NOT_OWNED
    }

    public interface ShopItemListener {
        void buyItem(Item item);

        void equipItem(Item item);

        boolean isItemOwned(Item item);
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
            float yPosition = 0;
            switch (item.getType()) {
                case HAT:
                    yPosition = 0;
                    break;
                case SHIRT:
                    yPosition = -100;
                    break;
                case SHOES:
                    yPosition = -230;
                    break;
                case PANTS:
                    yPosition = -180;
                    break;
            }
            itemImage.setY(yPosition);
            itemImage.setImageResource(item.getId());
            itemName.setText(item.getName());
        }
    }

    class OwnedItemViewHolder extends ItemViewHolder {
        OwnedItemViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.equip_button)
        void itemClicked() {
            Item item = itemList.get(getAdapterPosition());
            listener.equipItem(item);
        }

        @Override
        public void bind(int position) {
            super.bind(position);
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
            itemPrice.setText(String.format(Locale.US, String.valueOf(item.getPrice())));
        }
    }
}
