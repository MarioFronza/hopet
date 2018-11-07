package com.example.mario.pin1_hopet.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mario.pin1_hopet.R;
import com.example.mario.pin1_hopet.control.ConfiguracaoFirebase;
import com.example.mario.pin1_hopet.control.UsuarioFirebase;
import com.example.mario.pin1_hopet.model.Postagem;
import com.example.mario.pin1_hopet.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CadastroCasaActivity extends AppCompatActivity {

    private ImageView imagemPostagem;
    private TextView textAlterarImagem;
    private EditText editDescricao, editTamanho, editCidade, editRua, editTelefone;
    private static final int SELECAO_GALERIA = 200;
    private String idUsuarioLogado;
    private Postagem postagem;
    private Bitmap imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_casa);

        Toolbar toolbar = findViewById(R.id.toolbarCadastroCasa);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Postar Casa");

        imagemPostagem = findViewById(R.id.imageCasa);
        textAlterarImagem = findViewById(R.id.txtSelecionarFoto);
        editDescricao = findViewById(R.id.editDescricao);
        editTamanho = findViewById(R.id.editTamanho);
        editTelefone = findViewById(R.id.editTelefone);
        editCidade = findViewById(R.id.editCidade);
        editRua = findViewById(R.id.editRua);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        textAlterarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            try {
                switch (requestCode){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if(imagem != null){
                    imagemPostagem.setImageBitmap(imagem);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void cadastrarCasa(View view){

        postagem = new Postagem();
        postagem.setIdUsuario(idUsuarioLogado);
        postagem.setDescricao(editDescricao.getText().toString());
        postagem.setTamanho(Float.parseFloat(editTamanho.getText().toString()));
        postagem.setTelefone(editTelefone.getText().toString());
        postagem.setCidade(editCidade.getText().toString());
        postagem.setRua(editRua.getText().toString());

        if(!postagem.getDescricao().isEmpty()){
            if(postagem.getTamanho() != 0){
                if(!postagem.getTelefone().isEmpty()){
                    if(!postagem.getCidade().isEmpty()){
                        if(!postagem.getRua().isEmpty()){

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                            byte[] dadosImagem = baos.toByteArray();

                            StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
                            StorageReference imagemRef = storageReference
                                    .child("imagens")
                                    .child("postagens")
                                    .child(postagem.getId()+".jpeg");

                            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CadastroCasaActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri url = taskSnapshot.getDownloadUrl();
                                    postagem.setFoto( url.toString() );
                                    if(postagem.salvar()){
                                        Toast.makeText(CadastroCasaActivity.this, "Sucesso ao salvar postagem", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    Toast.makeText(CadastroCasaActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            Toast.makeText(CadastroCasaActivity.this, "Preencha a rua", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroCasaActivity.this, "Preencha a cidade", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroCasaActivity.this, "Preencha o telefone", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(CadastroCasaActivity.this, "Preencha o tamanho", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroCasaActivity.this, "Preencha a descrição", Toast.LENGTH_SHORT).show();
        }
    }
}
