package com.wendergalan.whatsappclone.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wender Galan Gamer on 28/12/2017.
 */

public class Permissao {


    public static boolean validaPermissoes(int requestCode, Activity activity, String [] permissoes){

        if (Build.VERSION.SDK_INT >= 23){
            /**
             * PERCORRE AS PERMISSOES
             * **/
            List<String> listaPermissoes = new ArrayList<String>();

            for (String permissao : permissoes){
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;

                if (!validaPermissao) listaPermissoes.add(permissao);
            }

            //caso esteja vazia
            if (listaPermissoes.isEmpty()) return true;

            String [] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //Solicita a permissao
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }

        return true;
    }
}
