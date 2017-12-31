package com.wendergalan.whatsappclone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wendergalan.whatsappclone.R;
import com.wendergalan.whatsappclone.model.Contato;

import java.util.ArrayList;

/**
 * Created by Wender Galan Gamer on 30/12/2017.
 */

public class ContatoAdapter extends ArrayAdapter<Contato>{

    private ArrayList<Contato> contatos;
    private Context context;


    public ContatoAdapter(Context c, ArrayList<Contato> objects) {
        super(c,0, objects);
        this.contatos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        //verifica se possui contatos

        if (contatos != null){
            //inicializa o objeto
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montar a view a partir do XML
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            TextView nomeContato = view.findViewById(R.id.tv_nome);
            TextView emailContato = view.findViewById(R.id.tv_email);

            //pega o item da lista
            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            emailContato.setText(contato.getEmail());


        }

        return view;
    }
}
