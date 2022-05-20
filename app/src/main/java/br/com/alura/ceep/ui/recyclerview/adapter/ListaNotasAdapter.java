package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

// Adapter para a RecyclerView
// informamos que usaremos apenas a nossa classe NotaViewHolder:
// <ListaNotasAdaptr.NotaViewHolder>
public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    // final pois são costantes
    private final List<Nota> notas;
    private final Context context;

    // Context para fazer o inflate e List<Nota> para utilizarmos nos métodos
    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    // método que cria os containers de views que ficarão visíveis
    // serão criados poucos ViewHolders, pois serão reaproveitados confrome fazemos scroll
    // os viewHolder são reaproveitados e o processo de bind é feito para a nova nota que aparece
    @NonNull
    @Override
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // pega um xml como entrada e constrói os objetos da View a partir dele
        // ou seja, vincula os elementos do xml à activity, integra o visual à lógica
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_nota, parent, false);

        // NotaViewHolder seria cada uma das notas dos nossos ViewHolders,
        // Cada nota ficará em um ViewHolder
        return new NotaViewHolder(viewCriada);
    }

    // onBindViewHolder serve para pegarmos as informações de cada Nota copiada da DAO
    // e colocarmos na nossa ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ListaNotasAdapter.NotaViewHolder holder, int position) {
        Nota nota = notas.get(position); // pega a Nota com base em sua posição na lista
        holder.vincula(nota);
    }

    // retorna o tamanho da lista
    @Override
    public int getItemCount() {
        return notas.size();
    }

    // criamos uma classe interna que será usada apenas pelo nooso Adapter atual: ListaNotasAdapter
    // a classe representa cada nota da nossa lista
    class NotaViewHolder extends RecyclerView.ViewHolder{

        private final TextView titulo;
        private final TextView descricao;

        // construtor do super
        public NotaViewHolder(@NonNull View itemView) {
            super(itemView); // para mandar o item para o ViewHolder
            // findViewById para referenciarmos nossa View
            // fazemos o findViewById dentro da classe para melhorarmos o desempenho
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
        }

        // vinculamos as informações da nota às nossas views
        // o vínculo é feito uma única vez e o bind se repete com base no vínculo feito
        public void vincula(Nota nota) {
            preencheCampos(nota);
        }

        private void preencheCampos(Nota nota) {
            titulo.setText(nota.getTitulo()); // coloca o título da nota na nossa view
            descricao.setText(nota.getDescricao()); // coloca a descrição da nota na nossa view
        }
    }

    // adiciona uma nota na lista copiada
    public void adiciona(Nota nota){
        notas.add(nota); // adiciona
        notifyDataSetChanged(); // verifica mudança para que nada seja sobreescrito
    }
}
