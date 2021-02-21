package com.josealex.granadacontributions.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.josealex.granadacontributions.R;
import com.josealex.granadacontributions.adapters.UsersRecyclerAdapter;
import com.josealex.granadacontributions.firebase.FirebaseDBManager;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.Consulta;
import com.josealex.granadacontributions.utils.GlobalInformation;

import java.util.ArrayList;

import static com.josealex.granadacontributions.utils.GlobalInformation.USERS;


public class UserFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    Button filter;
    EditText nameuser;
    View root;
    private RecyclerView viewRCWuser;
    private UsersRecyclerAdapter usersRecyclerAdapter = new UsersRecyclerAdapter(USERS) {

        @Override
        public void onButtonClick(User mItem) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            final View inflate = inflater.inflate(R.layout.dialog_add_salary, null);
            builder.setView(inflate)
                    .setPositiveButton(R.string.apply_button_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            EditText salary = inflate.findViewById(R.id.editSalaryadd);
                           try {

                               mItem.setSaldo(mItem.getSaldo()+Integer.valueOf( salary.getText().toString()));
                               FirebaseDBManager.saveUserData(mItem);
                           }catch (Exception e){
                                Toast.makeText(getContext(),R.string.salaryerror,Toast.LENGTH_LONG).show();
                           }
                        }
                    })
                    .setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
    };

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_user, container, false);
        filter = root.findViewById(R.id.buttonfilteruser);

        viewRCWuser = root.findViewById(R.id.includeUser);
        nameuser = root.findViewById(R.id.edituserfiltertext);
        viewRCWuser.setLayoutManager(new LinearLayoutManager(getContext()));

        viewRCWuser.setAdapter(usersRecyclerAdapter);


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeFilter();
            }
        });
        GlobalInformation.userFragment = this;
        return root;
    }

    public void update() {
        usersRecyclerAdapter.update(GlobalInformation.USERS);
    }

    public void makeFilter() {

        String catselect = nameuser.getText().toString();
        System.out.println(catselect);
        ArrayList<User> listuser = Consulta.getUsersWhere(new Consulta<User>() {
            @Override
            public boolean comprueba(User o) {
                System.out.println(o.getNombre());
                return !o.getNombre().isEmpty() && o.getNombre().contains(catselect);

            }
        });

        usersRecyclerAdapter.update(listuser);
        viewRCWuser.setAdapter(usersRecyclerAdapter);
    }
}