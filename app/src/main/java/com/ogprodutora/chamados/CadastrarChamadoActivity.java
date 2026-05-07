package com.ogprodutora.chamados;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastrarChamadoActivity extends AppCompatActivity {

    private EditText etTitulo, etData, etLocal, etDescricao;
    private Spinner spinnerTipo;
    private Button btnSalvar;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_chamado);

        db = new DatabaseHelper(this);

        etTitulo = findViewById(R.id.et_titulo);
        etData = findViewById(R.id.et_data);
        etLocal = findViewById(R.id.et_local);
        etDescricao = findViewById(R.id.et_descricao);
        spinnerTipo = findViewById(R.id.spinner_tipo);
        btnSalvar = findViewById(R.id.btn_salvar);

        // Preencher Data Atual
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        etData.setText(currentDate);

        // Configurar Spinner Tipo
        String[] tipos = new String[]{"Infraestrutura", "TI"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(adapter);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarChamado();
            }
        });
    }

    private void salvarChamado() {
        String titulo = etTitulo.getText().toString().trim();
        String data = etData.getText().toString().trim();
        String local = etLocal.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();

        if (titulo.isEmpty() || data.isEmpty() || local.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        Chamado chamado = new Chamado();
        chamado.setTitulo(titulo);
        chamado.setData(data);
        chamado.setLocal(local);
        chamado.setDescricao(descricao);
        chamado.setTipo(tipo);
        chamado.setStatus("Aberto"); // Status padrão
        chamado.setSolucao("");

        long id = db.addChamado(chamado);
        if (id > 0) {
            Toast.makeText(this, "Chamado salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela de cadastro
        } else {
            Toast.makeText(this, "Erro ao salvar o chamado.", Toast.LENGTH_SHORT).show();
        }
    }
}
