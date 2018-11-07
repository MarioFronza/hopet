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
import com.example.mario.pin1_hopet.model.Animal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class CadastroAnimalActivity extends AppCompatActivity {

    private ImageView imagemPostagem;
    private TextView textAlterarImagem;
    private EditText editNome, editIdade, editSexo, editRaca, editDescricao;
    private static final int SELECAO_GALERIA = 200;
    private String idUsuarioLogado;
    private Animal animal;
    private Bitmap imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal);

        Toolbar toolbar = findViewById(R.id.toolbarCadastroAnimal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cadastrar Animal");


        imagemPostagem = findViewById(R.id.imagePet);
        textAlterarImagem = findViewById(R.id.txtSelecionarImagemPet);
        editNome = findViewById(R.id.editNomePet);
        editIdade = findViewById(R.id.editIdadePet);
        editRaca = findViewById(R.id.editRacaPet);
        editSexo = findViewById(R.id.editSexoPet);
        editDescricao = findViewById(R.id.editDescricaoPet);
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

    public void cadastrarAnimal(View view){
        animal = new Animal();
        animal.setIdUsuario(idUsuarioLogado);
        animal.setNome(editNome.getText().toString());
        animal.setIdade(Integer.parseInt(editIdade.getText().toString()));
        animal.setRaca(editRaca.getText().toString());
        animal.setSexo(editSexo.getText().toString());
        animal.setDescricao(editDescricao.getText().toString());

        if(!animal.getNome().isEmpty()){
            if(animal.getIdade() != 0){
                if(!animal.getRaca().isEmpty()){
                    if(!animal.getSexo().isEmpty()){
                        if(!animal.getDescricao().isEmpty()){
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                            byte[] dadosImagem = baos.toByteArray();

                            StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
                            StorageReference imagemRef = storageReference
                                    .child("imagens")
                                    .child("animais")
                                    .child(animal.getIdAnimal()+".jpeg");

                            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CadastroAnimalActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Uri url = taskSnapshot.getDownloadUrl();
                                    animal.setFoto( url.toString() );
                                    if(animal.salvar()){
                                        Toast.makeText(CadastroAnimalActivity.this, "Sucesso ao salvar animal", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    Toast.makeText(CadastroAnimalActivity.this, "Sucesso ao fazer upload da animal", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(CadastroAnimalActivity.this, "Preencha a descrição completa do animal", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroAnimalActivity.this, "Preencha o sexo", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroAnimalActivity.this, "Preencha a raça", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(CadastroAnimalActivity.this, "Preencha a idade", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroAnimalActivity.this, "Preencha o nome", Toast.LENGTH_SHORT).show();
        }
    }
}
