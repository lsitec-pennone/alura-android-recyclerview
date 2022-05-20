package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

    }

    // método para colocar o nosso símbolo de "ok" no nosso menu (appbar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    private void retornaNota(Nota nota) {
        // instanciamos uma intent
        Intent resultadoInsercao = new Intent();

        // informamos que uma nota será enviada para a activity
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);

        // enviamos para a nossa activity que estava esperando a informação
        setResult(CODIGO_RESULTADO_NOTA_CRIADA, resultadoInsercao);
    }

    private Nota criaNota() {
        // findViewVyId para pegarmos a nossa view
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);

        // instanciamos uma nota e colocamos as informações digitadas na view
        return new Nota(titulo.getText().toString(),
                descricao.getText().toString());
    }

    private boolean ehMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_ic_salva;
    }
}