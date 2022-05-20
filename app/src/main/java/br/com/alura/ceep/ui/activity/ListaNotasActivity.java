package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
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
                vaiParaFormularioNotaActivity();
            }
        });
    }

    private void vaiParaFormularioNotaActivity() {
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

        // verifica condições
        if (ehResultadoComNota(requestCode, resultCode, data)){
            Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            adiciona(notaRecebida);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota); // salvamos a nota na DAO
        // para que salvar nessa instancia da DAO?????
        // aqui está inserindo na própria DAO, segundo o professor
        // mas esse "new" não é uma nova instancia????
        // se é uma nova instancia, como que altera a classe em si?
        adapter.adiciona(nota); // adiciona a nota recebida à View
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                ehCodigoResultadoNotaCriada(resultCode) &&
                temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        // findViewById para referenciarmos nossa View
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);

        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        // integra a nossa lista de notas copiada da DAO à nossa view usando o Adapter
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }
}