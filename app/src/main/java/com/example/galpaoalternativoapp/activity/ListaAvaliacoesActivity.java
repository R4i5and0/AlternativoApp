// Arquivo: activity/ListaAvaliacoesActivity.java - VERSÃO FINAL E CORRIGIDA
package com.example.galpaoalternativoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.adapter.AvaliacaoAdapter;
import com.example.galpaoalternativoapp.controller.DBHelper;
import com.example.galpaoalternativoapp.model.Avaliacao;
import java.util.ArrayList;
import java.util.List;

// A Activity implementa a nossa interface para "ouvir" os cliques do Adapter
public class ListaAvaliacoesActivity extends AppCompatActivity implements AvaliacaoAdapter.OnItemActionClickListener {

    private RecyclerView recyclerView;
    private AvaliacaoAdapter adapter;
    private DBHelper dbHelper;
    private List<Avaliacao> listaDeAvaliacoes = new ArrayList<>();
    private int idUsuarioLogado;

    // Launcher para atualizar a lista quando voltar da tela de edição
    private final ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    carregarAvaliacoes(); // Recarrega a lista para mostrar a edição
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_avaliacoes);

        dbHelper = new DBHelper(this);
        idUsuarioLogado = getIntent().getIntExtra("ID_DO_USUARIO", -1);

        recyclerView = findViewById(R.id.recyclerViewAvaliacoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carregarAvaliacoes();
    }

    private void carregarAvaliacoes() {
        listaDeAvaliacoes.clear();
        // A chamada ao DBHelper que retorna List<Avaliacao>
        listaDeAvaliacoes.addAll(dbHelper.listarAvaliacoes());

        // Passando 'this' como o terceiro parâmetro (o listener)
        adapter = new AvaliacaoAdapter(listaDeAvaliacoes, idUsuarioLogado, this);
        recyclerView.setAdapter(adapter);
    }

    // --- MÉTODOS DA INTERFACE (AÇÕES DOS BOTÕES) ---

    @Override
    public void onEditClick(Avaliacao avaliacao) {
        // Abre a tela de avaliação, passando os dados do item a ser editado
        Intent intent = new Intent(this, AvaliacaoActivity.class);
        intent.putExtra("ID_DO_USUARIO", idUsuarioLogado);
        intent.putExtra("EDIT_AVALIACAO_ID", avaliacao.getId());
        intent.putExtra("EDIT_TIPO", avaliacao.getTipo());
        intent.putExtra("EDIT_NOTA", avaliacao.getNota());
        intent.putExtra("EDIT_COMENTARIO", avaliacao.getComentario());
        editLauncher.launch(intent); // Usa o launcher para aguardar um resultado
    }

    @Override
    public void onDeleteClick(int position, int avaliacaoId) {
        // Cria um pop-up de confirmação antes de apagar
        new AlertDialog.Builder(this)
                .setTitle("Excluir Avaliação")
                .setMessage("Tem certeza que deseja excluir sua avaliação?")
                .setPositiveButton("Sim, Excluir", (dialog, which) -> {
                    // Apaga do banco de dados
                    dbHelper.excluirAvaliacao(avaliacaoId);
                    // Remove da lista na tela
                    listaDeAvaliacoes.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, listaDeAvaliacoes.size());
                    Toast.makeText(this, "Avaliação excluída!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}