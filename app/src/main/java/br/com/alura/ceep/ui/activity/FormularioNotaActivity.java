package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR_INSERE = "Insere nota";
    public static final String TITULO_APPBAR_ALTERA = "Altera nota";

    // atributo de classe para podermos alterá-la em retornaNota()
    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        setTitle(TITULO_APPBAR_INSERE);

        inicializaCampos();

        // verifica se recebeu os dados da ListaNotasActivity
        Intent dadosRecebidos = getIntent();
        if (dadosRecebidos.hasExtra(CHAVE_NOTA)) {

            setTitle(TITULO_APPBAR_ALTERA);

            // Serializable pq o objeto "nota" transita entre as classes
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);

            // -1 indica que a informação esperada não veio, pois "-1" não é índice de vetor
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

            preencheCampos(notaRecebida);
        }
    }



    private void preencheCampos(Nota notaRecebida) {

        // coloca o título da "nota" na nossa view
        titulo.setText(notaRecebida.getTitulo());

        //coloca a descrição da "nota" na nossa view
        descricao.setText(notaRecebida.getDescricao());
    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    // método para colocar o nosso símbolo de "ok" no nosso menu (appbar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // método para finalizar a activity quando o botão for clicado
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // verifica se o item clicado é o botao do menu
        if(ehMenuSalvaNota(item)){
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish(); // finaliza a activity
        }

        return super.onOptionsItemSelected(item);
    }

    // retorna a nota para a activity anterior com base no código de requisição
    private void retornaNota(Nota nota) {
        // instanciamos uma intent
        Intent resultadoInsercao = new Intent();

        // informamos que uma nota será enviada para a activity
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);

        // informados a posição da nota a ser alterada para a ativity
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);

        // enviamos para a nossa activity que estava esperando a informação
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private Nota criaNota() {
        // instanciamos uma nota e colocamos as informações digitadas na view
        return new Nota(titulo.getText().toString(),
                descricao.getText().toString());
    }

    private boolean ehMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}