package com.josealex.granadacontributions.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.GlobalInformation;

import java.util.ArrayList;
import java.util.List;


public abstract class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private List<User> mValues;
    private boolean isAdmin = false;

    public UsersRecyclerAdapter(List<User> items) {
        mValues = items;
    }

    public UsersRecyclerAdapter(List<User> items, boolean isAdmin) {
        mValues = items;
        this.isAdmin = isAdmin;
    }

    public void update(ArrayList<User> usersList) {
        mValues = usersList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.start(mValues.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView imageURL;
        public final ImageButton deleteUserBtn;
        private User mItem;
        private int position;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_user_name);
            mContentView = view.findViewById(R.id.item_user_correo);
            imageURL = view.findViewById(R.id.item_user_image);
            deleteUserBtn = view.findViewById(R.id.delete_user_button);
        }

        public void start(User mItem, int position) {
            this.mItem = mItem;
            mIdView.setText(mItem.getNombre());
            mContentView.setText(mItem.getCorreo());
            this.position = position;

            Glide.with(GlobalInformation.mainActivity)
                    .load( mItem.getFotoURL() )
                    .error(R.drawable.ic_launcher_foreground)
                    .circleCrop()
                    .into( imageURL );

            if(isAdmin) {
                deleteUserBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteButtonClick(mItem);
                    }
                });
            }
            else {
                System.out.println("max with to 0");
                deleteUserBtn.setMaxWidth(0);
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public abstract void onDeleteButtonClick(User mItem);
}