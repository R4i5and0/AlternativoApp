package com.example.galpaoalternativoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // Adicionado para Log.e
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.controller.DBHelper;
import com.example.galpaoalternativoapp.model.MensagemMural;
// Importe seu modelo Usuario aqui se você tiver um:
// import com.example.galpaoalternativoapp.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private RecyclerView recyclerViewUsuarios;
    private RecyclerView recyclerViewMensagens;

    // >>>>> INÍCIO DAS ALTERAÇÕES NECESSÁRIAS NA ADMINACTIVITY <<<<<
    private SimpleStringAdapter usuarioAdapter; // DECLARE O ADAPTER PARA USUARIOS AQUI
    private SimpleStringAdapter mensagemAdapter; // DECLARE O ADAPTER PARA MENSAGENS AQUI
    // >>>>> FIM DAS ALTERAÇÕES NECESSÁRIAS NA ADMINACTIVITY <<<<<

    private int selectedUsuarioId = -1;
    private int selectedMensagemId = -1;
    private List<MensagemMural> mensagensMuralList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        dbHelper = new DBHelper(this);

        // --- Inicializando os componentes de UI (Views) ---
        recyclerViewUsuarios = findViewById(R.id.listUsuarios);
        recyclerViewMensagens = findViewById(R.id.listMensagens);
        Button btnExcluirUsuario = findViewById(R.id.btnExcluirUsuario);
        Button btnExcluirMensagem = findViewById(R.id.btnExcluirMensagem);
        // btnLogoutAdmin está sendo inicializado aqui, após a remoção do static
        // Removido 'static' do btnLogoutAdmin, pois não é necessário e pode causar problemas de contexto
        // Agora é um membro de instância normal
        Button btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);

        // Inicializa a lista de mensagens do mural
        mensagensMuralList = new ArrayList<>();

        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMensagens.setLayoutManager(new LinearLayoutManager(this));

        // 1. Configuração para a lista de Usuários
        SimpleStringAdapter.OnItemClickListener usuarioClickListener = (itemText, position) -> {
            selectedUsuarioId = extrairId(itemText);
            Toast.makeText(AdminActivity.this, "Usuário selecionado: " + itemText, Toast.LENGTH_SHORT).show();
        };

        // >>>>> INÍCIO DAS ALTERAÇÕES NO ONCREATE <<<<<
        // Inicializa o adapter de usuário e o associa ao RecyclerView
        usuarioAdapter = new SimpleStringAdapter(dbHelper.listarUsuarios(), usuarioClickListener);
        recyclerViewUsuarios.setAdapter(usuarioAdapter);
        // >>>>> FIM DAS ALTERAÇÕES NO ONCREATE <<<<<

        // 2. Configuração para a lista de Mensagens
        SimpleStringAdapter.OnItemClickListener mensagemClickListener = (itemText, position) -> {
            if (position >= 0 && position < mensagensMuralList.size()) {
                MensagemMural mensagemSelecionada = mensagensMuralList.get(position);
                selectedMensagemId = mensagemSelecionada.getId();
                Toast.makeText(AdminActivity.this, "Mensagem selecionada: " + mensagemSelecionada.getTexto(), Toast.LENGTH_SHORT).show();
            } else {
                selectedMensagemId = -1;
                Toast.makeText(AdminActivity.this, "Erro na seleção da mensagem.", Toast.LENGTH_SHORT).show();
            }
        };

        // >>>>> INÍCIO DAS ALTERAÇÕES NO ONCREATE <<<<<
        // Inicializa o adapter de mensagem (pode ser com lista vazia inicialmente, será preenchido por carregarMensagens)
        mensagemAdapter = new SimpleStringAdapter(new ArrayList<>(), mensagemClickListener);
        recyclerViewMensagens.setAdapter(mensagemAdapter);
        // >>>>> FIM DAS ALTERAÇÕES NO ONCREATE <<<<<
        carregarMensagens(mensagemClickListener); // Chama o método para popular a lista e o adapter

        // --- Listeners para os Botões de Ação ---

        btnExcluirUsuario.setOnClickListener(v -> {
            if (selectedUsuarioId != -1) {
                dbHelper.excluirUsuario(selectedUsuarioId);
                carregarUsuarios(usuarioClickListener);
                // >>>>> INÍCIO DAS ALTERAÇÕES NO ONCLICKLISTENER <<<<<
                usuarioAdapter.clearSelection(); // Limpa a seleção visual após a exclusão
                // >>>>> FIM DAS ALTERAÇÕES NO ONCLICKLISTENER <<<<<
                Toast.makeText(this, "Usuário excluído com sucesso!", Toast.LENGTH_SHORT).show();
                selectedUsuarioId = -1;
            } else {
                Toast.makeText(this, "Selecione um usuário para excluir.", Toast.LENGTH_SHORT).show();
            }
        });

        btnExcluirMensagem.setOnClickListener(v -> {
            if (selectedMensagemId != -1) {
                dbHelper.excluirMensagemMural(selectedMensagemId);
                carregarMensagens(mensagemClickListener);
                // >>>>> INÍCIO DAS ALTERAÇÕES NO ONCLICKLISTENER <<<<<
                mensagemAdapter.clearSelection(); // Limpa a seleção visual após a exclusão
                // >>>>> FIM DAS ALTERAÇÕES NO ONCLICKLISTENER <<<<<
                Toast.makeText(this, "Mensagem excluída com sucesso!", Toast.LENGTH_SHORT).show();
                selectedMensagemId = -1;
            } else {
                Toast.makeText(this, "Selecione uma mensagem para excluir.", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Lógica do Botão de Logout do Admin (Movida para dentro do onCreate) ---
        // Verificação de nulidade já feita no findViewByID, mas um if ainda é bom para segurança
        if (btnLogoutAdmin != null) {
            btnLogoutAdmin.setOnClickListener(v -> {
                // Redireciona para a tela de Login e limpa a pilha de atividades
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish(); // Fecha a AdminActivity
                Toast.makeText(AdminActivity.this, "Logout do Admin realizado com sucesso!", Toast.LENGTH_SHORT).show();
            });
        } else {
            Log.e("AdminActivity", "Botão de Logout (btnLogoutAdmin) não encontrado no layout! Verifique activity_admin.xml");
        }
        // --- Fim da Lógica do Botão de Logout do Admin ---
    }

    private void carregarUsuarios(SimpleStringAdapter.OnItemClickListener listener) {
        List<String> usuarios = dbHelper.listarUsuarios();
        // >>>>> INÍCIO DAS ALTERAÇÕES NO CARREGARUSUARIOS <<<<<
        if (usuarioAdapter != null) {
            usuarioAdapter.updateData(usuarios); // Usa o método updateData do adapter
        } else {
            // Caso raro, se o adapter não foi inicializado (deve ser no onCreate)
            usuarioAdapter = new SimpleStringAdapter(usuarios, listener);
            recyclerViewUsuarios.setAdapter(usuarioAdapter);
        }
        // >>>>> FIM DAS ALTERAÇÕES NO CARREGARUSUARIOS <<<<<
    }

    private void carregarMensagens(SimpleStringAdapter.OnItemClickListener listener) {
        mensagensMuralList.clear();
        mensagensMuralList.addAll(dbHelper.listarMensagensMuralCompleto());

        List<String> mensagensParaExibir = new ArrayList<>();
        for (MensagemMural msg : mensagensMuralList) {
            mensagensParaExibir.add(msg.getId() + " - " + msg.getNomeUsuario() + ": " + msg.getTexto());
        }
        // >>>>> INÍCIO DAS ALTERAÇÕES NO CARREGARMENSAGENS <<<<<
        if (mensagemAdapter != null) {
            mensagemAdapter.updateData(mensagensParaExibir); // Usa o método updateData do adapter
        } else {
            // Caso raro, se o adapter não foi inicializado (deve ser no onCreate)
            mensagemAdapter = new SimpleStringAdapter(mensagensParaExibir, listener);
            recyclerViewMensagens.setAdapter(mensagemAdapter);
        }
        // >>>>> FIM DAS ALTERAÇÕES NO CARREGARMENSAGENS <<<<<
    }

    private int extrairId(String texto) {
        try {
            String[] partes = texto.split(" - ");
            return Integer.parseInt(partes[0].trim());
        } catch (Exception e) {
            // Se o "erro" que aparece for esta mensagem, verifique o formato da string.
            Toast.makeText(this, "Erro ao extrair ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("AdminActivity", "Erro ao extrair ID de: '" + texto + "'", e); // Adicionado Logcat para depuração
            return -1;
        }
    }

    // >>>>> INÍCIO DAS ALTERAÇÕES NA CLASSE SIMPLESTRINGADAPTER <<<<<
    public static class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringAdapter.StringViewHolder> {

        private final List<String> dataList;
        private final OnItemClickListener listener;
        private int selectedPosition = RecyclerView.NO_POSITION; // Adiciona a variável para rastrear a seleção

        public interface OnItemClickListener {
            void onItemClick(String itemText, int position);
        }

        public SimpleStringAdapter(List<String> dataList, OnItemClickListener listener) {
            this.dataList = dataList;
            this.listener = listener;
        }

        @NonNull
        @Override
        public StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_text_white, parent, false);
            return new StringViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
            String currentItem = dataList.get(position);
            holder.textView.setText(currentItem);

            // *** Lógica para aplicar o estado 'selected' ao item ***
            holder.itemView.setSelected(selectedPosition == position); // Isso ativa o 'state_selected' do selector

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    // Se o item clicado já era o selecionado, deseleciona
                    if (selectedPosition == holder.getAdapterPosition()) {
                        selectedPosition = RecyclerView.NO_POSITION; // Nenhuma seleção
                    } else {
                        // Desseleciona o item anterior, se houver
                        int oldSelectedPosition = selectedPosition;
                        selectedPosition = holder.getAdapterPosition();
                        if (oldSelectedPosition != RecyclerView.NO_POSITION) {
                            notifyItemChanged(oldSelectedPosition); // Redesenha o item antigo para remover o destaque
                        }
                    }
                    notifyItemChanged(selectedPosition); // Redesenha o item atual para aplicar/remover o destaque

                    // Chama o listener externo da Activity
                    listener.onItemClick(currentItem, holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        // Método para atualizar os dados e resetar a seleção (IMPORTANTE!)
        public void updateData(List<String> newData) {
            this.dataList.clear();
            this.dataList.addAll(newData);
            selectedPosition = RecyclerView.NO_POSITION; // Reseta a seleção quando a lista é atualizada
            notifyDataSetChanged();
        }

        // Método para limpar a seleção de fora do adapter (útil após excluir um item)
        public void clearSelection() {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                int oldSelected = selectedPosition;
                selectedPosition = RecyclerView.NO_POSITION;
                notifyItemChanged(oldSelected); // Notifica apenas o item que estava selecionado
            }
        }

        public static class StringViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public StringViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textItem);
            }
        }
    }

}