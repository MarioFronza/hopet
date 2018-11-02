package com.example.mario.pin1_hopet.model;

import com.example.mario.pin1_hopet.control.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.List;

/**
 * @author mario
 * @version 1.0
 * @created 01-Nov-2018 7:33:32 PM
 */
public class Usuario {

	private String idUsuario;
	private boolean cuidadorApto;
	private String email;
	private String  localizacao;
	private String nome;
	private String foto;
	private String senha;
	private List<Postagem> postagens;
	private List<Animal> animais;

	public Usuario(){

	}

	public void salvar(){
		DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
		DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getIdUsuario());
		usuariosRef.setValue(this);
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

	public boolean isCuidadorApto() {
		return cuidadorApto;
	}

	public void setCuidadorApto(boolean cuidadorApto) {
		this.cuidadorApto = cuidadorApto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Exclude
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Postagem> getPostagens() {
		return postagens;
	}

	public void setPostagens(List<Postagem> postagens) {
		this.postagens = postagens;
	}

	public List<Animal> getAnimais() {
		return animais;
	}

	public void setAnimais(List<Animal> animais) {
		this.animais = animais;
	}
}