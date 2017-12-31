package com.wendergalan.whatsappclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wendergalan.whatsappclone.R;
import com.wendergalan.whatsappclone.adapter.TabAdapter;
import com.wendergalan.whatsappclone.config.ConfiguracaoFirebase;
import com.wendergalan.whatsappclone.helper.Base64Custom;
import com.wendergalan.whatsappclone.helper.Preferencias;
import com.wendergalan.whatsappclone.helper.SlidingTabLayout;
import com.wendergalan.whatsappclone.model.Contato;
import com.wendergalan.whatsappclone.model.Usuario;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private FirebaseAuth usuarioFirebase;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String identificadorContato;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioFirebase = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);

        slidingTabLayout = findViewById(R.id.stl_tabs);
        viewPager = findViewById(R.id.vp_pagina);

        //configurar o slading tab layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccente));

        //configurar o adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager( viewPager );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_configuracoes:

                return true;
            case R.id.item_adicionar:
                abrirCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void abrirCadastroContato(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //configurando a dialog
        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        //Configurar os botoes
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String emailContato = editText.getText().toString();

                //valida o email
                if (emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this, "Preencha o e-mail", Toast.LENGTH_LONG).show();
                }else{
                    //verifica se existe no banco
                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    //recuperar  a instancia do FIREBASE
                    firebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(identificadorContato);
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){

                                //recuperar dados do contato a ser adicionado
                                //retorna um obejto ja populado
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                usuarioFirebase.getCurrentUser().getEmail();
                                firebase = ConfiguracaoFirebase.getFirebase();
                                firebase = firebase.child("contatos")
                                                   .child(identificadorUsuarioLogado).child(identificadorContato);
                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());
                                firebase.setValue(contato);

                            }else{
                                Toast.makeText(MainActivity.this, "Este usuário não possui cadastro", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    private void deslogarUsuario(){
        usuarioFirebase.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

