package com.wendergalan.whatsappclone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wendergalan.whatsappclone.R;
import com.wendergalan.whatsappclone.adapter.MensagemAdapter;
import com.wendergalan.whatsappclone.config.ConfiguracaoFirebase;
import com.wendergalan.whatsappclone.helper.Base64Custom;
import com.wendergalan.whatsappclone.helper.Preferencias;
import com.wendergalan.whatsappclone.model.Conversa;
import com.wendergalan.whatsappclone.model.Mensagem;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensgaem;

    //dados do destinatario
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;


    //dados do remetente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = findViewById(R.id.tb_conversa);
        editMensagem = findViewById(R.id.edit_mensagem);
        btMensagem = findViewById(R.id.bt_enviar);
        listView = findViewById(R.id.lv_conversas);

        //dados do usuario logado
        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        nomeUsuarioRemetente = preferencias.getNome();
        idUsuarioDestinatario = preferencias.getIdentificador();



        Bundle extra = getIntent().getExtras();

        if (extra != null){
            nomeUsuarioDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioRemetente = Base64Custom.codificarBase64(emailDestinatario);
        }


        //configurar a toolbar
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //montar a list view e adapter
        mensagens = new ArrayList<>();
        //adapter = new ArrayAdapter(ConversaActivity.this, android.R.layout.simple_list_item_1, mensagens);
        adapter = new MensagemAdapter(ConversaActivity.this,0, mensagens);
        listView.setAdapter(adapter);

        //recuperar as mensagens do firebase
        firebase = ConfiguracaoFirebase.getFirebase().child("mensagens").child(idUsuarioRemetente).child(idUsuarioDestinatario);

        //criar o listener para as mensagens
        valueEventListenerMensgaem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar as mensagens
                mensagens.clear();

                //recuperar as mensagens
                for (DataSnapshot dado : dataSnapshot.getChildren()){
                    Mensagem mensagem = dado.getValue( Mensagem.class );
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerMensgaem);

        //enviar menagenm
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoMensagem = editMensagem.getText().toString();
                if (textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this, "Digite uma mensagem para enviar!", Toast.LENGTH_LONG).show();
                }else{

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);

                    //salvar mensagem para o remetente
                    Boolean retornoMensagemRemetente = salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario , mensagem);

                    if (!retornoMensagemRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                    }else{

                        //salvar mensagem para o destinatario
                        Boolean retornoMensagemDestinatario = salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente , mensagem);
                        if (!retornoMensagemDestinatario){
                            Toast.makeText(ConversaActivity.this, "Problema ao salvar mensagem, tente novamente!", Toast.LENGTH_LONG).show();
                        }

                    }

                    //salvar a mensagem para o remetente
                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUsuarioDestinatario);
                    conversa.setNome(nomeUsuarioDestinatario);
                    conversa.setMensagem(textoMensagem);
                    boolean retornoConversaRemetente = salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, conversa);

                    if (!retornoConversaRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema ao salvar a conversa, tente novamente!", Toast.LENGTH_LONG).show();
                    }else{
                        //salvar a conversa para o destinatario
                        conversa = new Conversa();
                        conversa.setIdUsuario(idUsuarioRemetente);
                        conversa.setNome(nomeUsuarioRemetente);
                        conversa.setMensagem(textoMensagem);
                        boolean retornoConversaDestinatario = salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, conversa);

                        if (!retornoConversaDestinatario){
                            Toast.makeText(ConversaActivity.this, "Problema ao salvar a conversa, tente novamente!", Toast.LENGTH_LONG).show();
                        }
                    }






                    editMensagem.setText("");
                }
            }
        });


    }


    private boolean salvarMensagem (String idRemetente, String idDestinatario, Mensagem mensagem){

        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idRemetente).child(idDestinatario).push().setValue(mensagem);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }


    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensgaem);
    }


    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa){
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idRemetente).child(idDestinatario).setValue(conversa);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
