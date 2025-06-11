package com.example.galpaoalternativoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.adapter.MuralAdapter;
import com.example.galpaoalternativoapp.controller.DBHelper;
import com.example.galpaoalternativoapp.model.MensagemMural;

import java.util.ArrayList;
import java.util.List;

public class MuralActivity extends AppCompatActivity {

    private List<MensagemMural> mensagens = new ArrayList<>();
    private MuralAdapter muralAdapter;
    private DBHelper dbHelper;
    private RecyclerView recyclerViewMural;
    private int usuarioLogadoId; // VARIÁVEL DE CLASSE PARA ARMAZENAR O ID DO USUÁRIO LOGADO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mural);

        dbHelper = new DBHelper(this);

        // 1. RECEBENDO O ID DO USUÁRIO LOGADO DA INTENT
        usuarioLogadoId = getIntent().getIntExtra("USUARIO_ID", -1);
        if (usuarioLogadoId == -1) {
            Toast.makeText(this, "Erro: ID do usuário não encontrado. Faça login novamente.", Toast.LENGTH_LONG).show();
            // Opcional: Redirecionar para a tela de login
            // startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        recyclerViewMural = findViewById(R.id.recyclerViewMural);
        recyclerViewMural.setLayoutManager(new LinearLayoutManager(this));

        // 2. ATUALIZANDO O CONSTRUTOR DO MURALADAPTER
        muralAdapter = new MuralAdapter(mensagens, usuarioLogadoId); // AGORA PASSA O ID!
        recyclerViewMural.setAdapter(muralAdapter);

        carregarMensagensDoBanco();


        muralAdapter.setOnItemLongClickListener(position -> {
            MensagemMural mensagemParaApagar = mensagens.get(position);

            if (mensagemParaApagar.getUsuarioId() == usuarioLogadoId) {
                new AlertDialog.Builder(this)
                        .setTitle("Apagar sua Mensagem")
                        .setMessage("Tem certeza que deseja apagar SUA mensagem?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            dbHelper.excluirMensagemMural(mensagemParaApagar.getId());
                            Toast.makeText(this, "Sua mensagem apagada com sucesso!", Toast.LENGTH_SHORT).show();
                            carregarMensagensDoBanco();
                        })
                        .setNegativeButton("Não", null)
                        .show();
            } else {
                Toast.makeText(this, "Você só pode apagar suas próprias mensagens.", Toast.LENGTH_SHORT).show();
            }
            return true; // <--- Este 'true' agora será compatível
        });

        // 4. IMPLEMENTANDO A NOVA INTERFACE DO ADAPTER PARA EDITAR/EXCLUIR BOTÕES
        muralAdapter.setOnItemActionListener(new MuralAdapter.OnItemActionListener() {
            @Override
            public void onEditClick(int position) {
                MensagemMural mensagemParaEditar = mensagens.get(position);
                mostrarDialogoEdicaoMensagem(mensagemParaEditar);
            }

            @Override
            public void onDeleteClick(int position) {
                MensagemMural mensagemParaApagar = mensagens.get(position);
                confirmarExclusaoMensagem(mensagemParaApagar);
            }
        });

        Button btnNovaMensagem = findViewById(R.id.btnNovaMensagem);
        btnNovaMensagem.setOnClickListener(v -> mostrarDialogNovaMensagem());
    }

    // MÉTODOS AUXILIARES

    private void carregarMensagensDoBanco() {
        mensagens.clear();
        // CHAMANDO O NOVO MÉTODO DO DBHelper que retorna MensagemMural completa
        mensagens.addAll(dbHelper.listarMensagensMuralCompleto());
        muralAdapter.notifyDataSetChanged();
        // Rola para a última mensagem se houver mensagens
        if (!mensagens.isEmpty()) {
            recyclerViewMural.scrollToPosition(mensagens.size() - 1);
        }
    }

    private void mostrarDialogNovaMensagem() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_nova_mensagem, null);
        EditText editMensagem = dialogView.findViewById(R.id.editTextMensagem);

        new AlertDialog.Builder(this)
                .setTitle("Nova Mensagem")
                .setView(dialogView)
                .setPositiveButton("Enviar", (dialog, which) -> {
                    String msg = editMensagem.getText().toString().trim();

                    if (msg.isEmpty()) {
                        Toast.makeText(this, "Digite a mensagem para enviar!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // AQUI O ID DO USUÁRIO LOGADO É PASSADO AO SALVAR A MENSAGEM
                    long resultado = dbHelper.adicionarMensagemMural(msg, usuarioLogadoId);

                    if (resultado != -1) {
                        Toast.makeText(this, "Mensagem enviada com sucesso!", Toast.LENGTH_SHORT).show();
                        carregarMensagensDoBanco();
                    } else {
                        Toast.makeText(this, "Erro ao enviar mensagem.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // NOVOS MÉTODOS PARA EDIÇÃO E EXCLUSÃO
    private void mostrarDialogoEdicaoMensagem(MensagemMural mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Mensagem");

        final EditText input = new EditText(this);
        input.setText(mensagem.getTexto());
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String novoTexto = input.getText().toString().trim();
            if (!novoTexto.isEmpty()) {
                int linhasAfetadas = dbHelper.atualizarMensagemMural(mensagem.getId(), novoTexto);
                if (linhasAfetadas > 0) {
                    Toast.makeText(MuralActivity.this, "Mensagem atualizada!", Toast.LENGTH_SHORT).show();
                    carregarMensagensDoBanco();
                } else {
                    Toast.makeText(MuralActivity.this, "Erro ao atualizar mensagem.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MuralActivity.this, "A mensagem não pode estar vazia.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void confirmarExclusaoMensagem(MensagemMural mensagem) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Mensagem")
                .setMessage("Tem certeza que deseja excluir sua mensagem?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    dbHelper.excluirMensagemMural(mensagem.getId());
                    Toast.makeText(MuralActivity.this, "Mensagem excluída!", Toast.LENGTH_SHORT).show();
                    carregarMensagensDoBanco();
                })
                .setNegativeButton("Não", null)
                .show();
    }
}