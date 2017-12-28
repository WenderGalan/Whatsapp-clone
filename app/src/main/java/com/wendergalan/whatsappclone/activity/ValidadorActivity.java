/*package com.wendergalan.whatsappclone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.wendergalan.whatsappclone.R;
import com.wendergalan.whatsappclone.helper.Preferencias;

import java.util.HashMap;

public class ValidadorActivity extends AppCompatActivity {


    private EditText codigoValidacao;
    private Button validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_use);

        codigoValidacao = findViewById(R.id.edit_cod_validacao);
        validar = findViewById(R.id.bt_validar);

        //MASCARA PARA O CODIGO
        SimpleMaskFormatter simpleMaskCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher(codigoValidacao, simpleMaskCodigoValidacao);
        codigoValidacao.addTextChangedListener(mascaraCodigoValidacao);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Recuperar Dados das preferencias do usuario
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                String tokenGerado = usuario.get("token");
                String tokenDigitado = codigoValidacao.getText().toString();

                if (tokenDigitado.equals(tokenGerado)){
                    Toast.makeText(ValidadorActivity.this, "Token VALIDADO", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(ValidadorActivity.this, "Token NÃO VALIDADO", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}*/
