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
                itemView = inflater.inflate(R.layout.viewholder_friend_pending, parent, false);
                return new PendingFriendViewHolder(itemView);
            case CONFIRMED:
                itemView = inflater.inflate(R.layout.viewholder_friend_confirmed, parent, false);
                return new ConfirmedFriendViewHolder(itemView);
            case SEARCHED:
                itemView = inflater.inflate(R.layout.viewholder_friend_search, parent, false);
                return new SearchFriendViewHolder(itemView);
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


    //Interface Definitions
    public interface FriendRowView {
        void setBackgroundColor(int color);

        void setUsername(String username);
    }

    public interface FriendsListPresenter {
        int getItemCount();

        int getItemViewType(int position);

        void onBindFriendsRowViewAtPosition(int position, FriendRowView rowView, int
                selectedPosition);

        void requestFriend(int position);

        void confirmFriend(int position);

        void denyFriend(int position);

        void removeFriend(int position);

        void friendSelected(int position);

    }

    //View Holder Definitions
    class ConfirmedFriendViewHolder extends RecyclerView.ViewHolder implements
            FriendRowView {

        @BindView(R.id.text_username)
        TextView usernameText;

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
                    presenter.friendSelected(selectedPosition);
                }
            });
        }

        @OnClick(R.id.button_remove)
        void removeFriendClicked(View view) {
            presenter.removeFriend(getAdapterPosition());
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

    class PendingFriendViewHolder extends RecyclerView.ViewHolder implements FriendRowView {

        @BindView(R.id.text_username)
        TextView usernameText;

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
        }

        @OnClick({R.id.button_confirm, R.id.button_deny})
        void modifyButtonClicked(View view) {
            switch (view.getId()) {
                case R.id.button_confirm:
                    presenter.confirmFriend(getLayoutPosition());
                    break;
                case R.id.button_deny:
                    presenter.denyFriend(getLayoutPosition());
                    break;
            }
        }
    }

    class SearchFriendViewHolder extends RecyclerView.ViewHolder implements
            FriendRowView {

        @BindView(R.id.text_username)
        TextView usernameText;

        SearchFriendViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setBackgroundColor(int color) { // Intentionally left empty
        }

        @Override
        public void setUsername(String username) {
            usernameText.setText(username);
        }

        @OnClick(R.id.button_request)
        void requestClicked(View view) {presenter.requestFriend(getLayoutPosition()); }
    }

}
