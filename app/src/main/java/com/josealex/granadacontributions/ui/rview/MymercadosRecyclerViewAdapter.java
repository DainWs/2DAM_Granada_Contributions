package com.josealex.granadacontributions.ui.rview;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.Consulta;


import java.util.ArrayList;
import java.util.List;


public class MymercadosRecyclerViewAdapter extends RecyclerView.Adapter<MymercadosRecyclerViewAdapter.ViewHolder> {

    public static final String BUNDLE_MERCADO_ID = "mercado";

    private List<Mercado> mValuesMercados;
    private  ArrayList<Productos> mValuesPr = new ArrayList<>() ;
    private FragmentActivity activity;
    private Context context;


    public MymercadosRecyclerViewAdapter(ArrayList<Mercado> mercadoslista, Context context, FragmentActivity activity) {
        mValuesMercados = mercadoslista;
        this.context = context;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itemmercado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.start(mValuesMercados.get(position));
    }

    @Override
    public int getItemCount() {
        return mValuesMercados.size();
    }

    public void update(ArrayList<Mercado> mercadosList) {
        mValuesMercados = mercadosList;
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
            mName = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        public void start(Mercado mItem) {
            this.mItem = mItem;

            ArrayList<User> ownersUsers = Consulta.getUsersWhere(new Consulta<User>() {
                @Override
                public boolean comprueba(User o) {
                    return (mItem.getUidOwner().equals(o.getUid()));
                }
            });

            String ownersNames = context.getResources().getString(R.string.owners_text);
            if(ownersUsers.size() > 0)
                ownersNames += ownersUsers.get(0).getNombre();

            mName.setText(mItem.getNombre());
            mContentView.setText(ownersNames);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mValuesPr = mItem.getProductos();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_MERCADO_ID, mItem);

                    NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
                    navController.navigate(R.id.action_nav_home_to_productosFragment,bundle);

                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}