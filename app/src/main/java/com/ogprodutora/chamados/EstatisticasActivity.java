package com.ogprodutora.chamados;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class EstatisticasActivity extends BaseDrawerActivity {

    private TextView tvTotal, tvAbertos, tvAndamento, tvConcluidos;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);
        setTitle(getString(R.string.estatisticas));

        db = new DatabaseHelper(this);

        tvTotal = findViewById(R.id.tv_total_valor);
        tvAbertos = findViewById(R.id.tv_abertos_valor);
        tvAndamento = findViewById(R.id.tv_andamento_valor);
        tvConcluidos = findViewById(R.id.tv_concluidos_valor);



        carregarEstatisticas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
        int total = db.getTotalChamados();
        int abertos = db.getChamadosPorStatus("Aberto");
        int andamento = db.getChamadosPorStatus("Em andamento");
        int concluidos = db.getChamadosPorStatus("Concluído");

        tvTotal.setText(String.valueOf(total));
        tvAbertos.setText(String.valueOf(abertos));
        tvAndamento.setText(String.valueOf(andamento));
        tvConcluidos.setText(String.valueOf(concluidos));
    }
}
