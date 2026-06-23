package com.ogprodutora.chamados;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastrarChamadoActivity extends AppCompatActivity {

    private EditText etTitulo, etData, etLocal, etDescricao;
    private Spinner spinnerTipo;
    private Button btnSalvar, btnCapturarFoto;
    private ImageView ivFoto;
    private DatabaseHelper db;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private String currentPhotoPath;

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
        btnCapturarFoto = findViewById(R.id.btn_capturar_foto);
        ivFoto = findViewById(R.id.iv_foto);

        ImageButton btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Preencher Data Atual
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        etData.setText(currentDate);

        // Configurar Spinner Tipo
        String[] tipos = new String[]{"Infraestrutura", "TI"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(adapter);

        btnCapturarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CadastrarChamadoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CadastrarChamadoActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarChamado();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Erro ao criar arquivo de imagem", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Permissão da câmera é necessária.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setPic() {
        if (currentPhotoPath != null) {
            ivFoto.setVisibility(View.VISIBLE);
            Glide.with(this)
                 .load(currentPhotoPath)
                 .centerCrop()
                 .into(ivFoto);
        }
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

        if (currentPhotoPath == null || currentPhotoPath.isEmpty()) {
            Toast.makeText(this, "É obrigatório registrar uma foto para o chamado.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Chamado chamado = new Chamado();
        chamado.setTitulo(titulo);
        chamado.setData(data);
        chamado.setLocal(local);
        chamado.setDescricao(descricao);
        chamado.setTipo(tipo);
        chamado.setStatus("Aberto"); // Status padrão
        chamado.setSolucao("");
        chamado.setCaminhoImagem(currentPhotoPath);

        long id = db.addChamado(chamado);
        if (id > 0) {
            salvarNoParse(chamado);
            Toast.makeText(this, "Chamado salvo localmente e enviado para nuvem!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela de cadastro
        } else {
            Toast.makeText(this, "Erro ao salvar o chamado.", Toast.LENGTH_SHORT).show();
        }
    }

    private void salvarNoParse(Chamado chamado) {
        ParseObject chamadoParse = new ParseObject("Chamado");
        chamadoParse.put("titulo", chamado.getTitulo());
        chamadoParse.put("descricao", chamado.getDescricao());
        chamadoParse.put("local", chamado.getLocal());
        chamadoParse.put("status", chamado.getStatus());
        chamadoParse.put("data_cadastro", chamado.getData());

        if (chamado.getCaminhoImagem() != null) {
            new Thread(() -> {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(chamado.getCaminhoImagem());
                    if (bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] imageBytes = stream.toByteArray();

                        ParseFile parseFile = new ParseFile("foto_chamado.jpg", imageBytes);
                        chamadoParse.put("imagem", parseFile);
                    }
                    chamadoParse.saveInBackground(e -> {
                        if (e != null) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            chamadoParse.saveInBackground(e -> {
                if (e != null) {
                    e.printStackTrace();
                }
            });
        }
    }
}
