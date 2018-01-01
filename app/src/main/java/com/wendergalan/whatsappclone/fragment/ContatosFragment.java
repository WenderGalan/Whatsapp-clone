package com.wendergalan.whatsappclone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wendergalan.whatsappclone.R;
import com.wendergalan.whatsappclone.activity.ConversaActivity;
import com.wendergalan.whatsappclone.adapter.ContatoAdapter;
import com.wendergalan.whatsappclone.config.ConfiguracaoFirebase;
import com.wendergalan.whatsappclone.helper.Preferencias;
import com.wendergalan.whatsappclone.model.Contato;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerContatos);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_contatos, container, false);


        //instacniar os objetos
        contatos = new ArrayList<>();

        //monta o list view e adapter
        listView = view.findViewById(R.id.lv_contatos);

        /*adapter = new ArrayAdapter(
                getActivity(),
                R.layout.lista_contatos,
                contatos
        );*/

        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);


        //recuperar os dados do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();
        firebase = ConfiguracaoFirebase.getFirebase().child("contatos").child(identificadorUsuarioLogado);

        valueEventListenerContatos = new ValueEventListener() {
            //quando os doados forem alterados
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //limpar os dados
                contatos.clear();

                //percorrer os dados
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);
                }
                //avisa o adapter que mudou os dados
                adapter.notifyDataSetChanged();
            }

            //
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //recuperar os dados o usuario
                Contato contato = contatos.get(i);

                //enviar dados
                intent.putExtra("nome", contato.getNome());
                intent.putExtra("email", contato.getEmail());


                startActivity(intent);
            }
        });

        return view;
    }

}
