package com.josealex.granadacontributions.firebase;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.josealex.granadacontributions.MainActivity;
import com.josealex.granadacontributions.modules.Mercado;
import com.josealex.granadacontributions.modules.Productos;
import com.josealex.granadacontributions.modules.User;
import com.josealex.granadacontributions.utils.GlobalInformation;

import java.util.ArrayList;


public class FirebaseDBManager {

    public static final String MARKETS_PATH = "Mercado";
    public static final String USERS_PATH = "Usuarios";

    private static final String VALID_CHARACTERS_REGEX = "[^a-zA-Z0-9]+";
    private static String USER_FIREBASE_DB_PATH = "";

    private static ChildEventListener USERS_EVENTS_LISTENER;
    private static ChildEventListener MERCADOS_EVENTS_LISTENER;

    private static DatabaseReference USER_REF;
    private static DatabaseReference MERCADO_REF;

    private Activity activity;

    public FirebaseDBManager(MainActivity activity, User user) {
        this.activity = activity;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String DB_CONNECTION = makeFirebaseURLPath(user.getCorreo());
        USER_REF = db.getReference(DB_CONNECTION);
        MERCADO_REF = db.getReference(MARKETS_PATH);

        initListeners(user);
    }

    private void initListeners(final User user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //RECOGEMOS TODOS LOS USUARIOS
                Iterable<DataSnapshot> users = snapshot.child(USERS_PATH).getChildren();
                GlobalInformation.USERS.clear();

                for (DataSnapshot userData : users) {
                    User tmpUser = userData.getValue(User.class);

                    if(tmpUser.getCorreo().equals(user.getCorreo()))
                        GlobalInformation.SIGN_IN_USER = tmpUser;
                    else
                        GlobalInformation.USERS.add(tmpUser);
                }
                //El home estara esperando a que la lista de usuarios este lista, cuando lo este,
                //notificamos a home para que recoga la lista

                //TODO(AQUI SE ACTUALIZA TODO LO QUE NECESITE LOS USUARIOS AL COMENZAR)

                //RECOGEMOS LOS CHATS
                Iterable<DataSnapshot> mercados = snapshot.child(MARKETS_PATH).getChildren();

                for (DataSnapshot mercado : mercados) {

                    Mercado newMercado = new Mercado();
                    newMercado.setUid((String) mercado.child("uid").getValue());

                    newMercado.setUidOwner((String) mercado.child("uidOwner").getValue());
                    newMercado.setNombre((String) mercado.child("nombre").getValue());
                    newMercado.setPassword((String) mercado.child("password").getValue());

                    //PILLAMOS LOS GESTORES
                    ArrayList<String> gestores = new ArrayList<>();
                    for (DataSnapshot gestor : mercado.child("gestores").getChildren()) {
                        gestores.add((String) gestor.getValue());
                    }

                    newMercado.setGestores(gestores);

                    //PILLAMOS LOS PRODUCTOS
                    ArrayList<Productos> productos = new ArrayList<>();
                    for (DataSnapshot producto : mercado.child("productos").getChildren()) {
                        productos.add(producto.getValue(Productos.class));
                    }

                    newMercado.setProductos(productos);

                    GlobalInformation.MERCADOS.add(newMercado);
                }

                //En caso de que el chat se haya creado, y este esperando los datos, lo notificamos
                //para que recoga los datos, pero si no se creo no hay problema, cuando se cree el comprobara
                //una vez si hay datos
                //TODO(AQUI SE ACTUALIZA TODO LO QUE NECESITE LOS MERCADOS AL COMENZAR)

                GlobalInformation.mainActivity.update();
                GlobalInformation.home.update();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //CUANDO EL USUARIO CAMBIA SUS DATOS
        USERS_EVENTS_LISTENER = USER_REF.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                User currentUser = GlobalInformation.SIGN_IN_USER;

                if(user.getCorreo().equals(currentUser.getCorreo())) {
                    GlobalInformation.SIGN_IN_USER = user;
                }
                else {
                    int searchedID = -1;

                    ArrayList<User> list = GlobalInformation.USERS;
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getCorreo().equals(user.getCorreo())) {
                            searchedID = i;
                            break;
                        }
                    }

                    if(searchedID >= 0){
                        GlobalInformation.USERS.set(searchedID, user);

                        //TODO (ACTUALIZA AQUI USO DE LISTAS)
                        GlobalInformation.mainActivity.update();
                    }
                }

               GlobalInformation.preferences.update();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        MERCADOS_EVENTS_LISTENER = MERCADO_REF.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mercado mercado = new Mercado();
                mercado.setUid((String) snapshot.child("uid").getValue());
                mercado.setNombre((String) snapshot.child("nombre").getValue());

                //PILLAMOS LOS GESTORES
                ArrayList<String> gestores = new ArrayList<>();
                for (DataSnapshot gestor : snapshot.child("gestores").getChildren()) {
                    gestores.add((String) gestor.getValue());
                }

                mercado.setGestores(gestores);

                //PILLAMOS LOS PRODUCTOS
                ArrayList<Productos> productos = new ArrayList<>();
                for (DataSnapshot producto : snapshot.child("productos").getChildren()) {
                    productos.add(producto.getValue(Productos.class));
                }

                mercado.setProductos(productos);

                int searchedID = -1;

                ArrayList<Mercado> list = GlobalInformation.MERCADOS;
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getUid().equals(user.getUid())) {
                        searchedID = i;
                        break;
                    }
                }

                if(searchedID >= 0){
                    GlobalInformation.MERCADOS.set(searchedID, mercado);

                    //TODO (ACTUALIZA AQUI USO DE LISTAS)
                    GlobalInformation.mainActivity.update();
                }


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    public static String makeFirebaseURLPath(String data) {
        USER_FIREBASE_DB_PATH = USERS_PATH +"/"+ data.replaceAll(VALID_CHARACTERS_REGEX, "_");
        return USER_FIREBASE_DB_PATH;
    }

    public static void saveUserData(User user) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String userPath = FirebaseDBManager.makeFirebaseURLPath(user.getCorreo());
        USER_REF = db.getReference(userPath);
        USER_REF.setValue(user);
    }

    public static void saveMercado(Mercado mercado) {
        if(mercado.getUid().isEmpty()) {
            mercado.setUid(MERCADO_REF.push().getKey());
        }

        MERCADO_REF.child(mercado.getUid()).setValue(mercado);
    }

    public static void restartListeners() {
        if(USER_REF != null && USERS_EVENTS_LISTENER != null) {
            USER_REF.addChildEventListener(USERS_EVENTS_LISTENER);
        }

        if(MERCADO_REF != null && MERCADOS_EVENTS_LISTENER != null) {
            MERCADO_REF.addChildEventListener(MERCADOS_EVENTS_LISTENER);
        }
    }

    public static void stopListeners() {
        if(USER_REF != null && USERS_EVENTS_LISTENER != null) {
            USER_REF.removeEventListener(USERS_EVENTS_LISTENER);
        }

        if(MERCADO_REF != null && MERCADOS_EVENTS_LISTENER != null) {
            MERCADO_REF.removeEventListener(MERCADOS_EVENTS_LISTENER);
        }

    }

}
