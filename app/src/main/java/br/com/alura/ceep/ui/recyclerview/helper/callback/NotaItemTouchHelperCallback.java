package br.com.alura.ceep.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter) {
        this.adapter = adapter;
    }

    // método que define as ações que serão permitidas
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; // define marcações
        int marcacoesDeArrastar = ItemTouchHelper.DOWN
                | ItemTouchHelper.UP
                | ItemTouchHelper.LEFT
                | ItemTouchHelper.RIGHT; // define marcações
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize); // cria o movimento com base nelas
    }

    // método que funciona como uma chamada para quando o elemento for arrastado no RecyclerView
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        trocaNotas(posicaoInicial, posicaoFinal);
        return true;
    }

    private void trocaNotas(int posicaoInicial, int posicaoFinal) {
        new NotaDAO().troca(posicaoInicial, posicaoFinal); // troca com base na posição
        adapter.troca(posicaoInicial, posicaoFinal);
    }

    // método que funciona como uma chamada para quando o elemento for deslizado no recyclerview
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getAdapterPosition();
        removeNota(posicaoDaNotaDeslizada);
    }

    private void removeNota(int posicao) {
        new NotaDAO().remove(posicao); // remove com base na posição
        adapter.remove(posicao);
    }
}
