// Arquivo: CardapioActivity.java - VERSÃO CORRIGIDA
package com.example.galpaoalternativoapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.adapter.CardapioAdapter;
import com.example.galpaoalternativoapp.model.CarrinhoSingleton;
import com.example.galpaoalternativoapp.model.ItemCardapio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class CardapioActivity extends AppCompatActivity implements CardapioAdapter.OnItemClickListener {

    private int idDoUsuarioLogado = -1; // Declara a variável aqui

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        // NOVO: Recebe o ID do usuário da HomeActivity
        idDoUsuarioLogado = getIntent().getIntExtra("ID_DO_USUARIO", -1);
        if (idDoUsuarioLogado == -1) {
            Log.e("CardapioActivity", "ID do usuário não recebido na CardapioActivity!");
            // Considerar tratar este erro, talvez redirecionar para o login
            // Toast.makeText(this, "Erro de sessão. Faça login novamente.", Toast.LENGTH_LONG).show();
            // finish();
            // return;
        } else {
            Log.d("CardapioActivity", "ID do usuário recebido na CardapioActivity: " + idDoUsuarioLogado);
        }

        // Encontra o botão flutuante no novo layout
        FloatingActionButton btnVerCarrinho = findViewById(R.id.btnVerCarrinho);
        btnVerCarrinho.setOnClickListener(v -> {
            Intent intent = new Intent(CardapioActivity.this, CarrinhoActivity.class);
            intent.putExtra("ID_DO_USUARIO", idDoUsuarioLogado); // PASSA O ID PARA O CARRINHO
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCardapio);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sua lista de itens do cardápio com as imagens
        List<ItemCardapio> itens = new ArrayList<>();
        itens.add(new ItemCardapio(1, "Hambúrguer Rock'n'Roll", "Hambúrguer com cheddar, bacon e molho secreto", 25.90, ItemCardapio.Categoria.COMIDA, R.drawable.hamburguer_rock));
        itens.add(new ItemCardapio(2, "Batata Louca", "Porção de batatas fritas com cheddar e bacon", 18.50, ItemCardapio.Categoria.COMIDA, R.drawable.batata_louca));
        itens.add(new ItemCardapio(3, "Petisco Punk", "Iscas crocantes de frango com molho picante", 20.00, ItemCardapio.Categoria.COMIDA, R.drawable.petisco_punk));
        itens.add(new ItemCardapio(4, "Cerveja do Galpão", "Long neck artesanal gelada", 12.00, ItemCardapio.Categoria.BEBIDA, R.drawable.cerveja_galpao));
        itens.add(new ItemCardapio(5, "Refrigerante Rocksteady", "Lata 350ml, várias opções", 7.00, ItemCardapio.Categoria.BEBIDA, R.drawable.refrigerante));
        itens.add(new ItemCardapio(6, "Suco do Mosh", "Suco natural de laranja com gengibre", 10.00, ItemCardapio.Categoria.BEBIDA, R.drawable.suco_laranja));
        itens.add(new ItemCardapio(7, "Brownie do Baú", "Brownie com sorvete de creme", 15.00, ItemCardapio.Categoria.SOBREMESA, R.drawable.brownie));
        itens.add(new ItemCardapio(8, "Gelatina de Guitarra", "Gelatina colorida em formato de guitarra", 8.00, ItemCardapio.Categoria.SOBREMESA, R.drawable.gelatina_guitarra));
        itens.add(new ItemCardapio(9, "Churros do Rock", "Churros com doce de leite e canela", 12.50, ItemCardapio.Categoria.SOBREMESA, R.drawable.churros));

        // Configura o Adapter e a RecyclerView
        CardapioAdapter adapter = new CardapioAdapter(itens, this);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onAdicionarClick(ItemCardapio item) {
        CarrinhoSingleton.getInstance().adicionarItem(item);
        Log.d("CardapioActivity", "Item adicionado ao Singleton: " + item.getNome());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_item_adicionado, null);
        TextView TxtMensagem = dialogView.findViewById(R.id.dialog_message);
        Button btnVerCarrinho = dialogView.findViewById(R.id.dialog_button_ver_carrinho);

        TxtMensagem.setText("✔️ " + item.getNome() + "\nfoi adicionado ao carrinho!");
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // CORREÇÃO: Passar o ID do usuário ao ir para o Carrinho a partir do Dialog
        btnVerCarrinho.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(CardapioActivity.this, CarrinhoActivity.class);
            intent.putExtra("ID_DO_USUARIO", idDoUsuarioLogado); // Passa o ID aqui também
            startActivity(intent);
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 3000);
    }
}