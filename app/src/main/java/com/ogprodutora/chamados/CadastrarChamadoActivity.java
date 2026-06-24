package com.ogprodutora.chamados;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastrarChamadoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;

    private EditText etTitulo, etData, etLocal, etDescricao;
    private Spinner spinnerTipo, spinnerStatus;
    private Button btnSalvar, btnCamera;
    private ImageView ivPreview;
    private DatabaseHelper db;
    private String currentImagePath;

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
        spinnerStatus = findViewById(R.id.spinner_status);
        btnSalvar = findViewById(R.id.btn_salvar);
        btnCamera = findViewById(R.id.btn_camera);
        ivPreview = findViewById(R.id.iv_preview);

        if (savedInstanceState != null) {
            currentImagePath = savedInstanceState.getString("currentImagePath");
            if (currentImagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
                if (bitmap != null) {
                    ivPreview.setImageBitmap(bitmap);
                    ivPreview.setVisibility(View.VISIBLE);
                    btnCamera.setText("Alterar Imagem");
                }
            }
        }

        ImageButton btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        etData.setText(currentDate);

        String[] tipos = new String[]{"Infraestrutura", "TI"};
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(tipoAdapter);

        String[] statusArray = new String[]{"Aberto", "Em andamento", "Concluído"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusArray);
        spinnerStatus.setAdapter(statusAdapter);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarChamado();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentImagePath", currentImagePath);
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                File storageDir = new File(getFilesDir(), "images");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName = "IMG_" + timeStamp + "_";
                imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
                currentImagePath = imageFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao criar arquivo de imagem", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        imageFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(this, "Câmera não disponível neste dispositivo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Permissão da câmera necessária para capturar imagem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (currentImagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath);
                if (bitmap != null) {
                    ivPreview.setImageBitmap(bitmap);
                    ivPreview.setVisibility(View.VISIBLE);
                    btnCamera.setText("Alterar Imagem");
                }
            }
        }
    }

    private void salvarChamado() {
        String titulo = etTitulo.getText().toString().trim();
        String data = etData.getText().toString().trim();
        String local = etLocal.getText().toString().trim();
        String descricao = etDescricao.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if (titulo.isEmpty() || data.isEmpty() || local.isEmpty() || descricao.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentImagePath == null) {
            Toast.makeText(this, "Capture uma imagem do problema!", Toast.LENGTH_SHORT).show();
            return;
        }

        Chamado chamado = new Chamado();
        chamado.setTitulo(titulo);
        chamado.setData(data);
        chamado.setLocal(local);
        chamado.setDescricao(descricao);
        chamado.setTipo(tipo);
        chamado.setStatus(status);
        chamado.setSolucao("");
        chamado.setImagePath(currentImagePath);

        final long id = db.addChamado(chamado);
        if (id > 0) {
            chamado.setId((int) id);
            sincronizarComNuvem(chamado);
            Toast.makeText(this, "Chamado salvo com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar o chamado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sincronizarComNuvem(Chamado chamado) {
        ChamadoParse chamadoParse = new ChamadoParse();
        chamadoParse.setTitulo(chamado.getTitulo());
        chamadoParse.setDescricao(chamado.getDescricao());
        chamadoParse.setLocal(chamado.getLocal());
        chamadoParse.setStatus(chamado.getStatus());
        chamadoParse.setData(chamado.getData());

        String nomeArquivo = chamado.getImagePath();
        if (nomeArquivo != null) {
            nomeArquivo = nomeArquivo.substring(nomeArquivo.lastIndexOf("/") + 1);
        }
        chamadoParse.setImagemNome(nomeArquivo);

        chamadoParse.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
    }
}
