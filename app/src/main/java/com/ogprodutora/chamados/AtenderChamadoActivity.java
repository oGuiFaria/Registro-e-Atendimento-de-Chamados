package com.ogprodutora.chamados;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AtenderChamadoActivity extends AppCompatActivity {

    private TextView tvTitulo, tvInfo, tvDescricao;
    private Spinner spinnerStatus;
    private EditText etSolucao;
    private Button btnSalvar;
    private ImageView ivImagem;

    private DatabaseHelper db;
    private Chamado chamado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atender_chamado);

        db = new DatabaseHelper(this);

        tvTitulo = findViewById(R.id.tv_detalhe_titulo);
        tvInfo = findViewById(R.id.tv_detalhe_info);
        tvDescricao = findViewById(R.id.tv_detalhe_descricao);
        spinnerStatus = findViewById(R.id.spinner_status_atendimento);
        etSolucao = findViewById(R.id.et_solucao);
        btnSalvar = findViewById(R.id.btn_salvar_atendimento);
        ivImagem = findViewById(R.id.iv_detalhe_imagem);

        ImageButton btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(v -> finish());

        String[] statusArray = new String[]{"Aberto", "Em andamento", "Concluído"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusArray);
        spinnerStatus.setAdapter(adapter);

        chamado = (Chamado) getIntent().getSerializableExtra("CHAMADO");

        if (chamado != null) {
            tvTitulo.setText(chamado.getTitulo());
            String info = "Data: " + chamado.getData() + " | Local: " + chamado.getLocal() + " | Tipo: " + chamado.getTipo();
            tvInfo.setText(info);
            tvDescricao.setText(chamado.getDescricao());
            etSolucao.setText(chamado.getSolucao());

            if (chamado.getImagePath() != null && !chamado.getImagePath().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(chamado.getImagePath());
                if (bitmap != null) {
                    ivImagem.setImageBitmap(bitmap);
                    ivImagem.setVisibility(View.VISIBLE);
                }
            }

            String statusAtual = chamado.getStatus();
            if (statusAtual != null) {
                for (int i = 0; i < statusArray.length; i++) {
                    if (statusAtual.equals(statusArray[i])) {
                        spinnerStatus.setSelection(i);
                        break;
                    }
                }
            }
        }

        btnSalvar.setOnClickListener(v -> atualizarChamado());
    }

    private void atualizarChamado() {
        if (chamado == null) return;

        String novoStatus = spinnerStatus.getSelectedItem().toString();
        String novaSolucao = etSolucao.getText().toString().trim();

        chamado.setStatus(novoStatus);
        chamado.setSolucao(novaSolucao);

        int rows = db.updateChamado(chamado);
        if (rows > 0) {
            Toast.makeText(this, "Atendimento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao atualizar atendimento.", Toast.LENGTH_SHORT).show();
        }
    }
}
