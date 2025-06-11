package com.example.galpaoalternativoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Importar Button
import android.widget.LinearLayout; // Importar LinearLayout
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.model.MensagemMural;

import java.util.List;

public class MuralAdapter extends RecyclerView.Adapter<MuralAdapter.MuralViewHolder> {

    private final List<MensagemMural> listaMensagens;
    private OnItemLongClickListener longClickListener;
    private OnItemActionListener itemActionListener;

    // NOVO: Adicionar o ID do usuário logado
    private final int usuarioLogadoId;

    // SUA INTERFACE ORIGINAL PARA O LONG CLICK
    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }



    // MINHA NOVA INTERFACE para editar/excluir
    public interface OnItemActionListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    // NOVO: Setter para o ItemActionListener
    public void setOnItemActionListener(OnItemActionListener listener) {
        this.itemActionListener = listener;
    }

    // CONSTRUTOR ATUALIZADO: Recebe o usuarioId
    public MuralAdapter(List<MensagemMural> listaMensagens, int usuarioLogadoId) {
        this.listaMensagens = listaMensagens;
        this.usuarioLogadoId = usuarioLogadoId; // Atribui o ID do usuário logado
    }

    @NonNull
    @Override
    public MuralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mural, parent, false);
        // Passar o itemActionListener e longClickListener para o ViewHolder
        return new MuralViewHolder(view, longClickListener, itemActionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MuralViewHolder holder, int position) {
        MensagemMural mensagem = listaMensagens.get(position);
        holder.nomeUsuario.setText(mensagem.getNomeUsuario());
        holder.textoMensagem.setText(mensagem.getTexto());
        if (mensagem.getUsuarioId() == usuarioLogadoId) {
            holder.layoutBotoesMural.setVisibility(View.VISIBLE);
        } else {
            holder.layoutBotoesMural.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaMensagens.size();
    }

    static class MuralViewHolder extends RecyclerView.ViewHolder {
        TextView nomeUsuario;
        TextView textoMensagem;
        LinearLayout layoutBotoesMural; // NOVO
        Button btnEditarMural; // NOVO
        Button btnExcluirMural; // NOVO

        // CONSTRUTOR DO VIEWHOLDER ATUALIZADO
        public MuralViewHolder(@NonNull View itemView, OnItemLongClickListener longClickListener, OnItemActionListener itemActionListener) {
            super(itemView);
            nomeUsuario = itemView.findViewById(R.id.textNomeUsuario);
            textoMensagem = itemView.findViewById(R.id.textMensagem);
            layoutBotoesMural = itemView.findViewById(R.id.layoutBotoesMural); // NOVO
            btnEditarMural = itemView.findViewById(R.id.btnEditarMural); // NOVO
            btnExcluirMural = itemView.findViewById(R.id.btnExcluirMural); // NOVO

            // Já existe: Long click para exclusão
            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        longClickListener.onItemLongClick(position);
                        return true;
                    }
                }
                return false;
            });

            // NOVO: Listeners para os botões Editar e Excluir
            btnEditarMural.setOnClickListener(v -> {
                if (itemActionListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemActionListener.onEditClick(position);
                    }
                }
            });

            btnExcluirMural.setOnClickListener(v -> {
                if (itemActionListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemActionListener.onDeleteClick(position);
                    }
                }
            });
        }
    }
}