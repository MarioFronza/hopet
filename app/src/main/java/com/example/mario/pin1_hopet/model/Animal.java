package com.example.mario.pin1_hopet.model;

import com.example.mario.pin1_hopet.control.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Animal {

    private String idAnimal;
    private String idUsuario;
    private String foto;
    private String nome;
    private String raca;
    private int idade;
    private String sexo;
    private String descricao;

    public Animal() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference animaisRef = firebaseRef.child("animais");
        String idAnimal = animaisRef.push().getKey();
        setIdAnimal(idAnimal);
    }

    public boolean salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference animalRef = firebaseRef.child("animais")
                .child(getIdUsuario())
                .child(getIdAnimal());
        animalRef.setValue(this);
        return true;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(String idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
