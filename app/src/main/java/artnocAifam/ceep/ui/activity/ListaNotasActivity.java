package artnocAifam.ceep.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import artnocAifam.ceep.R;
import artnocAifam.ceep.dao.NotaDAO;
import artnocAifam.ceep.model.Nota;
import artnocAifam.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import artnocAifam.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import artnocAifam.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static artnocAifam.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static artnocAifam.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static artnocAifam.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static artnocAifam.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static artnocAifam.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITLE_APPBAR = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITLE_APPBAR);

        List<Nota> todasNotas = pegaTodasNotas();

        configuraRecyclerView(todasNotas);

        configuraBotaoInsereNota();

    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaActivityInserir();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInserir() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);

        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
//                registerForActivityResult(iniciaFormularioNota, 1);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for (int i = 0; i <10; i++){
            dao.insere(new Nota("Título "+(i+1), "Descrição " +(i+1)));
        }
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ehResultadoInsereNota(requestCode, data)) {

            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }

        }

        if(ehResultadoAlteraNota(requestCode, data)){
            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if(ehPosicaoValida(posicaoRecebida)){
                    altera(notaRecebida, posicaoRecebida);
                } else {
                    Toast.makeText(this,
                            "Ocorreu um problema na alteração da nota",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode ==CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data);
    }

    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

/*    private List<Nota> notasDeExemplo() {
        NotaDAO dao = new NotaDAO();
        dao.insere(new Nota("Primeiro Título", "Primeira descrição"),
                new Nota("Segundo Título",
                        "O vento do bosque roxo vem vermelho quando pisca o inconsciente do javali mágico") );
//        for(int i = 1; i <= 10000; i++){
//            dao.insere(new Nota("Título "+i, "Descrição "+i));
//        }

        List<Nota> todasNotas = dao.todos();
        return todasNotas;
    }*/

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
        // o layout Manager foi agora configurado no xml da view
//        configuraLayoutManager(listaNotas);

        configuraItemTouchHelper(listaNotas);

    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraLayoutManager(RecyclerView listaNotas) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaNotas.setLayoutManager(layoutManager);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        //        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNotas));

        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
//                Toast.makeText(ListaNotasActivity.this, nota.getTitulo(), Toast.LENGTH_SHORT).show();
                vaiParaFormularioNotaActivityAlterar(nota, posicao);

            }
        });
    }

    private void vaiParaFormularioNotaActivityAlterar(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

/*    @Override
    protected void onResume() {
//        NotaDAO dao = new NotaDAO();
////        List<Nota> todasNotas = dao.todos();
////        configuraRecyclerView(todasNotas);
//
////        configuraRecyclerView(new NotaDAO().todos());
//todasNotas.clear();
//todasNotas.addAll(dao.todos());
//        adapter.notifyDataSetChanged();
        super.onResume();

    }*/


}