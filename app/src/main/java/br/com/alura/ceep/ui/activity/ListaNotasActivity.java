package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR); // coloca o título na appbar

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();

    }

    private void configuraBotaoInsereNota() {
        // findViewById para referenciarmos nossa View
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);

        // listener para transferirmos de tela ao tocarmos na nossa view "botaoInsereNota"
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota =
                new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);

        // trasfere para FormularioNotaActivity
        // e informa que ela espera um resultado da FormularioNotaActivity
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {

        // instancia uma lista de notas
        NotaDAO dao = new NotaDAO();

        return dao.todos();
    }

    // método capaz de identificar se a requisição que pedimos
    // foi respondida conforme o esperado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // verifica condições da nota criada
        if (ehResultadoInsereNota(requestCode, data)){
            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
                adiciona(notaRecebida);
            }
        }

        // verifica condições da nota editada
        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)){
                // recebe a nota editada
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);

                // recebe a posição da nota editada
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

                // só altera se a posição for válida
                if (ehPosicaoValida(posicaoRecebida)) {

                    altera(notaRecebida, posicaoRecebida);

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void altera(Nota nota, int posicao) {
        // altera na DAO a nota recebida na posição correta
        new NotaDAO().altera(posicao, nota);

        adapter.altera(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota); // salvamos a nota na DAO
        adapter.adiciona(nota); // adiciona a nota recebida à View
    }

    private boolean ehResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        // verifica se o usuário desistiu de criar ou alterar a nota
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        // findViewById para referenciarmos nossa View
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);

        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);

    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        // configura o deslize de dedo na nota
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas); // atrela a nossa RecyclerView
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        // integra a nossa lista de notas copiada da DAO à nossa view usando o Adapter
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);

        // listener personalizado
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {

                // colocamos o comportamento que desejamos
                // sem que o adapter fique com essa responsabilidade
                // nesse caso enviamos a nota clicada para ser editada
                // e abrimos a activity de formulário
                vaiParaFormularioNotaActivityAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota); // enviando a nota
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao); // envia posicao da nota
        // abrimos a FormularioNotaActivity aguardando informações de retorno
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}