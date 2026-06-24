package com.ogprodutora.chamados;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListarChamadosActivity extends BaseDrawerActivity {

    private RecyclerView recyclerView;
    private ChamadoAdapter adapter;
    private DatabaseHelper db;
    private List<Chamado> todosChamados;
    private EditText etFiltroData;
    private Spinner spinnerFiltroStatus;
    private Button btnFiltrar, btnLimparFiltros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_chamados);
        setTitle(getString(R.string.listar_chamados));

        db = new DatabaseHelper(this);

        etFiltroData = findViewById(R.id.et_filtro_data);
        spinnerFiltroStatus = findViewById(R.id.spinner_filtro_status);
        btnFiltrar = findViewById(R.id.btn_filtrar);
        btnLimparFiltros = findViewById(R.id.btn_limpar_filtros);
        recyclerView = findViewById(R.id.recycler_chamados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        String[] statusFiltro = new String[]{"Todos", "Aberto", "Em andamento", "Concluído"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusFiltro);
        spinnerFiltroStatus.setAdapter(spinnerAdapter);

        carregarChamados();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicarFiltros();
            }
        });

        btnLimparFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFiltroData.setText("");
                spinnerFiltroStatus.setSelection(0);
                adapter.updateList(todosChamados);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarChamados();
    }

    private void carregarChamados() {
        todosChamados = db.getAllChamados();
        adapter = new ChamadoAdapter(this, todosChamados, new ChamadoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chamado chamado) {
                Intent intent = new Intent(ListarChamadosActivity.this, AtenderChamadoActivity.class);
                intent.putExtra("CHAMADO", chamado);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void aplicarFiltros() {
        String dataFiltro = etFiltroData.getText().toString().trim();
        String statusFiltro = spinnerFiltroStatus.getSelectedItem().toString();

        List<Chamado> listaFiltrada = new ArrayList<>();

        for (Chamado c : todosChamados) {
            boolean matchData = dataFiltro.isEmpty() || c.getData().contains(dataFiltro);
            boolean matchStatus = statusFiltro.equals("Todos") || c.getStatus().equals(statusFiltro);

            if (matchData && matchStatus) {
                listaFiltrada.add(c);
            }
        }

        adapter.updateList(listaFiltrada);
    }
}
