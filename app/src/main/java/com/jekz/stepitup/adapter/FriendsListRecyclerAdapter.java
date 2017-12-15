package com.jekz.stepitup.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jekz.stepitup.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by evanalmonte on 12/15/17.
 */

public class FriendsListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final FriendsListPresenter presenter;
    int selectedPosition = -1;

    public FriendsListRecyclerAdapter(FriendsListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        FriendType type = FriendType.values()[viewType];
        switch (type) {
            case PENDING:
                itemView = inflater.inflate(R.layout.friend_pending_row_layout, parent, false);
                return new PendingFriendViewHolder(itemView);
            case CONFIRMED:
                itemView = inflater.inflate(R.layout.friend_confirmed_row_layout, parent, false);
                return new ConfirmedFriendViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        presenter.onBindFriendsRowViewAtPosition(position, (FriendRowView) holder,
                selectedPosition);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getItemViewType(position);
    }


    public enum FriendType {
        PENDING, CONFIRMED
    }

    public interface FriendRowView {
        void setBackgroundColor(int color);

        void setUsername(String username);
    }

    //Interface Definitions

    public interface PendingFriendRowView extends FriendRowView {
        void addButtonListener(FriendsListPresenter.PendingFriendButtonListener listener);
    }

    public interface FriendsListPresenter {
        int getItemCount();

        int getItemViewType(int position);

        void onBindFriendsRowViewAtPosition(int position, FriendRowView rowView, int
                selectedPosition);

        interface PendingFriendButtonListener {
            void onConfirm(int position);

            void onDeny(int position);
        }

    }

    //View Holder Definitions
    class ConfirmedFriendViewHolder extends RecyclerView.ViewHolder implements
            FriendRowView {

        @BindView(R.id.text_username)
        TextView usernameText;

        ConfirmedFriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int oldPosition = selectedPosition;
                    selectedPosition = getLayoutPosition();
                    notifyItemChanged(oldPosition);
                    notifyItemChanged(selectedPosition);
                }
            });
        }

        @Override
        public void setUsername(String username) {
            usernameText.setText(username);
        }

        @Override
        public void setBackgroundColor(int colorId) {
            itemView.setBackgroundResource(colorId);
        }
    }

    class PendingFriendViewHolder extends RecyclerView.ViewHolder implements PendingFriendRowView {

        @BindView(R.id.text_username)
        TextView usernameText;

        FriendsListPresenter.PendingFriendButtonListener listener;

        PendingFriendViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setUsername(String username) {
            usernameText.setText(username);
        }

        @Override
        public void setBackgroundColor(int color) {
            itemView.setBackgroundColor(color);
        }

        @Override
        public void addButtonListener(FriendsListPresenter.PendingFriendButtonListener listener) {
            this.listener = listener;
        }

        @OnClick({R.id.button_confirm, R.id.button_deny})
        void modifyButtonClicked(View view) {
            switch (view.getId()) {
                case R.id.button_confirm:
                    listener.onConfirm(getLayoutPosition());
                    break;
                case R.id.button_deny:
                    listener.onDeny(getLayoutPosition());
                    break;
            }
        }
    }
}
