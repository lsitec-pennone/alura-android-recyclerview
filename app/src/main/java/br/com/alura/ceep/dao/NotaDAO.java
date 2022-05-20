package br.com.alura.ceep.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.alura.ceep.model.Nota;

// lista estática de notas
public class NotaDAO {

    // instancia um array de notas
    private final static ArrayList<Nota> notas = new ArrayList<>();

    // método que retorna uma cópia da lista
    public List<Nota> todos() {
        return (List<Nota>) notas.clone();
    }


    public void insere(Nota... notas) {
        NotaDAO.notas.addAll(Arrays.asList(notas));
    }

    public void altera(int posicao, Nota nota) {
        notas.set(posicao, nota);
    }

    public void remove(int posicao) {
        notas.remove(posicao);
    }

    public void troca(int posicaoInicio, int posicaoFim) {
        Collections.swap(notas, posicaoInicio, posicaoFim);
    }

    public void removeTodos() {
        notas.clear();
    }
}
