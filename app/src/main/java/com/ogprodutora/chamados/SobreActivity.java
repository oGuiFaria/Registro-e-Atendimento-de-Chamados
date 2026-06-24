package com.ogprodutora.chamados;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class SobreActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        setTitle(getString(R.string.sobre_sistema));



        TextView tvAppNome = findViewById(R.id.tv_sobre_app_nome);
        TextView tvIntegrantes = findViewById(R.id.tv_sobre_integrantes);
        TextView tvTurma = findViewById(R.id.tv_sobre_turma);
        TextView tvDescricao = findViewById(R.id.tv_sobre_descricao);

        tvAppNome.setText(getString(R.string.app_name));
        tvIntegrantes.setText(getString(R.string.integrantes));
        tvTurma.setText(getString(R.string.turma));
        tvDescricao.setText(getString(R.string.app_descricao));
    }
}
