package com.wendergalan.whatsappclone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wendergalan.whatsappclone.R;

public class MainActivity extends AppCompatActivity {

    //pega o acesso ao banco de dados
    private DatabaseReference referenciadatabase = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
}
