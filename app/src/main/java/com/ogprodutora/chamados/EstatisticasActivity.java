package com.ogprodutora.chamados;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class EstatisticasActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvTotal, tvAbertos, tvEmAndamento, tvConcluidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        db = new DatabaseHelper(this);

        tvTotal = findViewById(R.id.tv_total_chamados);
        tvAbertos = findViewById(R.id.tv_abertos);
        tvEmAndamento = findViewById(R.id.tv_em_andamento);
        tvConcluidos = findViewById(R.id.tv_concluidos);

        ImageButton btnVoltar = findViewById(R.id.btn_voltar_estatisticas);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calcularEstatisticas();
    }

    private void calcularEstatisticas() {
        List<Chamado> chamados = db.getAllChamados();

        int total = chamados.size();
        int abertos = 0;
        int emAndamento = 0;
        int concluidos = 0;

        for (Chamado c : chamados) {
            if ("Aberto".equals(c.getStatus())) {
                abertos++;
            } else if ("Em atendimento".equals(c.getStatus()) || "Em andamento".equals(c.getStatus())) {
                emAndamento++;
            } else if ("Concluído".equals(c.getStatus())) {
                concluidos++;
            }
        }

        tvTotal.setText(String.valueOf(total));
        tvAbertos.setText(String.valueOf(abertos));
        tvEmAndamento.setText(String.valueOf(emAndamento));
        tvConcluidos.setText(String.valueOf(concluidos));
    }
}
