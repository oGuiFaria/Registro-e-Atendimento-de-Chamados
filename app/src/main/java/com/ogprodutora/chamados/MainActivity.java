package com.ogprodutora.chamados;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSouUsuario = findViewById(R.id.btn_sou_usuario);
        Button btnSouTecnico = findViewById(R.id.btn_sou_tecnico);

        btnSouUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastrarChamadoActivity.class);
                startActivity(intent);
            }
        });

        btnSouTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListarChamadosActivity.class);
                startActivity(intent);
            }
        });
    }
}
