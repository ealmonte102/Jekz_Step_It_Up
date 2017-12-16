package com.jekz.stepitup.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jekz.stepitup.R;
import com.jekz.stepitup.model.friend.Friend;

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
        Friend.FriendType type = Friend.FriendType.values()[viewType];
        switch (type) {
            case PENDING:
                itemView = inflater.inflate(R.layout.friend_pending_row_layout, parent, false);
                return new PendingFriendViewHolder(itemView);
            case CONFIRMED:
                itemView = inflater.inflate(R.layout.friend_confirmed_row_layout, parent, false);
                return new ConfirmedFriendViewHolder(itemView);
            case SEARCHED:
                //itemView = inflater.inflate(R.layout.friend_search_row_layout, parent, false);
                //return new SearchFriendViewHolder(itemView);
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

    public interface FriendRowView {
        void setBackgroundColor(int color);

        void setUsername(String username);

        void addFriendClickListener(FriendsListPresenter.FriendClickListener listener);
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

        interface FriendClickListener {
            void onFriendClicked(int position);
        }

    }

    //View Holder Definitions
    class ConfirmedFriendViewHolder extends RecyclerView.ViewHolder implements
            FriendRowView {

        @BindView(R.id.text_username)
        TextView usernameText;

        private FriendsListPresenter.FriendClickListener listener;

        ConfirmedFriendViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int oldPosition = selectedPosition;
                    selectedPosition = getLayoutPosition();
                    notifyItemChanged(oldPosition);
                    notifyItemChanged(selectedPosition);
                    listener.onFriendClicked(selectedPosition);
                }
            });
        }

        @Override
        public void setUsername(String username) {
            usernameText.setText(username);
        }

        @Override
        public void addFriendClickListener(FriendsListPresenter.FriendClickListener listener) {
            this.listener = listener;
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
        public void addFriendClickListener(FriendsListPresenter.FriendClickListener listener) {
            //Intentionally empty
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
