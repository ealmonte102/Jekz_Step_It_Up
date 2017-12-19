package com.jekz.stepitup.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jekz.stepitup.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by evanalmonte on 12/20/17.
 */

public class SettingsListRecylerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final SettingsListPresenter presenter;


    public SettingsListRecylerAdapter(SettingsListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        SettingType type = SettingType.values()[viewType];
        return new SettingsViewHolder(inflater.inflate(R.layout.viewholder_settings, parent,
                false), type);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        presenter.onBindSettingsViewholderAtPosition(position, (SettingsRowView) holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getItemViewType(position);
    }


    public enum SettingType {
        NAME, GENDER, HEIGHT, WEIGHT, STEP_GOAL
    }

    public interface SettingsListPresenter {
        void onSettingsClicked(SettingType type);

        int getItemViewType(int position);

        int getItemCount();

        void onBindSettingsViewholderAtPosition(int position, SettingsRowView rowView);
    }

    public interface SettingsRowView {
        void setTitleText(String text);

        void setDescriptionText(String text);
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder implements SettingsRowView {
        @BindView(R.id.text_settings_viewholder_title)
        TextView titleText;

        @BindView(R.id.text_settings_viewholder_description)
        TextView descriptionText;

        SettingType type;

        public SettingsViewHolder(View itemView, final SettingType type) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.type = type;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onSettingsClicked(type);
                }
            });
        }

        @Override
        public void setTitleText(String text) {
            titleText.setText(text);
        }

        @Override
        public void setDescriptionText(String text) {
            descriptionText.setText(text);
        }
    }
}
