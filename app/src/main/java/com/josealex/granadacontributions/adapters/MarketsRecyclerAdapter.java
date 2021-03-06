package com.josealex.granadacontributions.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.ResourceManager;

import java.util.ArrayList;
import java.util.List;


public abstract class MarketsRecyclerAdapter extends RecyclerView.Adapter<MarketsRecyclerAdapter.ViewHolder> {

    public static final String BUNDLE_MERCADO_ID = "mercado";

    private List<Mercado> mValues;

    public MarketsRecyclerAdapter(ArrayList<Mercado> marketList) {
        mValues = marketList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.start(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void update(ArrayList<Mercado> marketList) {
        mValues = marketList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mContentView;
        public Mercado mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = view.findViewById(R.id.item_id_linea);
            mContentView = view.findViewById(R.id.item_nombre_linea);
        }

        public void start(Mercado mItem) {
            this.mItem = mItem;

            ArrayList<User>  ownersUsers = Consulta.getUsersWhere(new Consulta<User>() {
                    @Override
                    public boolean comprueba(User o) {
                        return (mItem.getUidOwner().equals(o.getUid()));
                    }
            });

            String ownersNames = ResourceManager.getString(R.string.owners_text);
            if(ownersUsers.size() > 0)
                ownersNames += ownersUsers.get(0).getNombre();

            mName.setText(mItem.getNombre());
            mContentView.setText(ownersNames);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(mItem);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public abstract void onItemClick(Mercado mercado);
}