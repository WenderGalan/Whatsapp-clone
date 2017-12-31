package com.wendergalan.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Wender Galan Gamer on 28/12/2017.
 */

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "whatsapp.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";

    public Preferencias (Context contextParametro){
        contexto = contextParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }

    public void salvarDados(String identificadorUsuario){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.commit();
    }

   /* public HashMap<String, String> getDadosUsuario(){

        HashMap<String, String> dadosUsuario = new HashMap<>();

        dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadosUsuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE, null));
        dadosUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));

        return dadosUsuario;
    }*/

   public String getIdentificador(){
       return preferences.getString(CHAVE_IDENTIFICADOR, null);
   }


}
