// CarrinhoActivity.java - VERSÃO CORRIGIDA E COM LOGS
package com.example.galpaoalternativoapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log; // Adicione este import

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.adapter.CarrinhoAdapter;
import com.example.galpaoalternativoapp.controller.DBHelper;
import com.example.galpaoalternativoapp.model.CarrinhoSingleton;
import com.example.galpaoalternativoapp.model.ItemCardapio;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarrinhoActivity extends AppCompatActivity {

    private List<ItemCardapio> carrinho;
    private TextView tvTotal;
    private DBHelper dbHelper;
    private int idDoUsuarioLogado = -1; // Para armazenar o ID do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        dbHelper = new DBHelper(this);

        // NOVO: Recebe o ID do usuário do Intent
        idDoUsuarioLogado = getIntent().getIntExtra("ID_DO_USUARIO", -1);
        if (idDoUsuarioLogado == -1) {
            Log.e("CarrinhoActivity", "ID do usuário não recebido na CarrinhoActivity!");
            Toast.makeText(this, "Erro de sessão. Por favor, faça login novamente.", Toast.LENGTH_LONG).show();
            // Opcional: Redirecionar para o login se o ID for essencial para a CarrinhoActivity funcionar
            // startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            Log.d("CarrinhoActivity", "ID do usuário recebido na CarrinhoActivity: " + idDoUsuarioLogado);
        }

        carrinho = CarrinhoSingleton.getInstance().getCarrinho();

        if (carrinho.isEmpty()) {
            Toast.makeText(this, "Seu carrinho está vazio!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCarrinho);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CarrinhoAdapter(carrinho));

        tvTotal = findViewById(R.id.textTotal);
        atualizarTotal();

        findViewById(R.id.btnFinalizarPedido).setOnClickListener(v -> {
            if (carrinho.isEmpty()) {
                Toast.makeText(this, "Carrinho está vazio!", Toast.LENGTH_SHORT).show();
            } else {
                finalizarPedido();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void atualizarTotal() {
        double total = 0;
        for (ItemCardapio item : carrinho) {
            total += item.getPreco();
        }
        tvTotal.setText(String.format("Total: R$ %.2f", total));
    }

    @SuppressLint("DefaultLocale")
    private void finalizarPedido() {
        double total = 0;
        for (ItemCardapio item : carrinho) {
            total += item.getPreco();
        }

        Log.d("CarrinhoActivity", "Iniciando finalização do pedido para ID: " + idDoUsuarioLogado);

        // CORREÇÃO CRÍTICA AQUI: Usa o ID REAL do usuário logado!
        // E remove Math.toIntExact(), pois salvarPedido já retorna int
        int senhaPermanente = dbHelper.salvarPedido(carrinho, total, idDoUsuarioLogado);

        Log.d("CarrinhoActivity", "Retorno de salvarPedido. Senha gerada: " + senhaPermanente);

        if (senhaPermanente == -1) {
            Toast.makeText(this, "Houve um erro ao processar seu pedido.", Toast.LENGTH_SHORT).show();
            Log.e("CarrinhoActivity", "Erro ao salvar o pedido. Senha retornada foi -1.");
            return;
        }

        String mensagem = String.format(
                Locale.getDefault(),
                "Seu pedido foi realizado com sucesso!\n\n" +
                        "🧾 Total: R$ %.2f\n" +
                        "🔐 Sua senha é: %d\n\n" +
                        "Guarde esta senha! Aguarde seu pedido ser chamado no telão.",
                total,
                senhaPermanente
        );

        new AlertDialog.Builder(this)
                .setTitle("✅ Pedido Finalizado")
                .setMessage(mensagem)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    CarrinhoSingleton.getInstance().limpar();
                    finish();
                })
                .show();
    }
}