package com.ogprodutora.chamados;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Service Desk Corp");

        Button btnSouUsuario = findViewById(R.id.btn_sou_usuario);
        Button btnSouTecnico = findViewById(R.id.btn_sou_tecnico);

        btnSouUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastrarChamadoActivity.class));
            }
        });

        btnSouTecnico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListarChamadosActivity.class));
            }
        });
    }
}
