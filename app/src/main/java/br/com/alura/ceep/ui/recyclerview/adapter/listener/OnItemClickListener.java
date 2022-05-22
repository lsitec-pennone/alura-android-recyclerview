package br.com.alura.ceep.ui.recyclerview.adapter.listener;

import br.com.alura.ceep.model.Nota;

// interface criada para permitir a implementação de um "clique" de elemento
public interface OnItemClickListener {

    // informa que o comportamento esperado é o clique de item
    void onItemClick(Nota nota, int posicao);
}
