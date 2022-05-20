package br.com.alura.ceep.model;

import java.io.Serializable;

// modelo de uma nota
// Serializable para que o objeto possa transitar entre as activities
public class Nota implements Serializable {

    private final String titulo;
    private final String descricao;

    // construtor
    public Nota(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    // getters
    public String getTitulo() {
        return titulo;
    }
    public String getDescricao() {
        return descricao;
    }

}