package com.example.mario.pin1_hopet.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mario.pin1_hopet.Manifest;
import com.example.mario.pin1_hopet.R;
import com.example.mario.pin1_hopet.control.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;


public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    private FirebaseAuth autenticacao;

    @Override protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);


        addSlide(new SimpleSlide.Builder()
                .title("Bem-vindo ao Hopet")
                .description("o aplicativo que ajudará você a encontrar um cuidador para seu pet")
                .image(R.drawable.logar)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Encontre cuidadores")
                .description("Aptos e responsáveis, avaliados por nossa equipe para que seu animal tenha total segurança")
                .image(R.drawable.home)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Tenha uma renda extra")
                .description("Ganhe dinheiro cuidando de outros animais em sua própria casa")
                .image(R.drawable.money)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Encontre casas perto de você")
                .description("Com o serviço de localização você encontra locais próximos utilizando o mapa do aplicativo")
                .image(R.drawable.local)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .fragment(R.layout.intro_escolha)
                .canGoForward(false)
                .build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void txtLogar(View view){
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
    }

    public void btCadastrar(View view){
        startActivity(new Intent(IntroActivity.this, CadastroActivity.class));
    }

    public void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        if(autenticacao.getCurrentUser() != null){
            startActivity(new Intent(IntroActivity.this, PrincipalActivity.class));
        }
    }

}
