package com.udea.daniel.practicafinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Inicio extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public void Administrador(View view){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void Usuario(View view){
        Intent i = new Intent(this, ClaseUsuario.class);
        startActivity(i);
    }
}