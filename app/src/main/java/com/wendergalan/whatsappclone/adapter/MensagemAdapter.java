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
import com.wendergalan.whatsappclone.helper.Preferencias;
import com.wendergalan.whatsappclone.model.Mensagem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wender Galan Gamer on 01/01/2018.
 */

public class MensagemAdapter extends ArrayAdapter<Mensagem>{

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context c, int resource, ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         View view = null;

         //verificar se a lista esta poreenchida
        if (mensagens != null){

            //recuperar dados do usuario remetente
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRemetente = preferencias.getIdentificador();

            //inicializar o objeto para montar a view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //recuperar mensagens
            Mensagem mensagem = mensagens.get(position);

            //monta a view a partir do xml
            if (idUsuarioRemetente.equals(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
            }else{
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
            }

            //recuperar o elemento
            TextView textoMensagem = view.findViewById(R.id.tv_mensagem);
            textoMensagem.setText(mensagem.getMensagem());

        }
        return view;
    }
}
