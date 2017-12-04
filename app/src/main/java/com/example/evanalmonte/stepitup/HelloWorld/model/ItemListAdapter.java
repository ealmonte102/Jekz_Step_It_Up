package com.example.evanalmonte.stepitup.HelloWorld.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evanalmonte.stepitup.R;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
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
        holder.itemImage.setY(yPosition);
        holder.itemImage.setImageResource(item.getId());
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText(String.format(Locale.US, "x%d", item.getPrice()));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface ShopItemListener {
        void onItemClicked(Item item);
    }

    final class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shop_item_image)
        ImageView itemImage;

        @BindView(R.id.shop_item_name)
        TextView itemName;

        @BindView(R.id.shop_item_price)
        TextView itemPrice;

        @BindView(R.id.shop_item_footprint)
        ImageView footprint;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.buy_button)
        void itemClicked() {
            Item item = itemList.get(getAdapterPosition());
            listener.onItemClicked(item);
        }
    }
}
