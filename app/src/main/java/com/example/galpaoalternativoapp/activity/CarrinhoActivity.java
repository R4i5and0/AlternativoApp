package com.example.galpaoalternativoapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.adapter.CarrinhoAdapter;
import com.example.galpaoalternativoapp.controller.DBHelper;
import com.example.galpaoalternativoapp.model.CarrinhoSingleton;
import com.example.galpaoalternativoapp.model.ItemCarrinho;
import java.util.List;


public class CarrinhoActivity extends AppCompatActivity implements CarrinhoAdapter.OnCarrinhoInteractionListener {

    private List<ItemCarrinho> itensDoCarrinho;
    private TextView tvTotal;
    private DBHelper dbHelper;
    private CarrinhoAdapter adapter;
    private RecyclerView recyclerView;
    private int idDoUsuarioLogado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        dbHelper = new DBHelper(this);
        idDoUsuarioLogado = getIntent().getIntExtra("ID_DO_USUARIO", -1);

        if (idDoUsuarioLogado == -1) {
            Log.e("CarrinhoActivity", "ID do usuário não foi recebido via Intent!");
            Toast.makeText(this, "Erro de sessão. Por favor, faça login novamente.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (CarrinhoSingleton.getInstance().isEmpty()) {
            Toast.makeText(this, "Seu carrinho está vazio!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewCarrinho);
        tvTotal = findViewById(R.id.textTotal);

        setupRecyclerView();

        findViewById(R.id.btnFinalizarPedido).setOnClickListener(v -> {
            if (CarrinhoSingleton.getInstance().isEmpty()) {
                Toast.makeText(this, "O carrinho ficou vazio!", Toast.LENGTH_SHORT).show();
            } else {
                finalizarPedido();
            }
        });
    }

    private void setupRecyclerView() {
        itensDoCarrinho = CarrinhoSingleton.getInstance().getItensDoCarrinho();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarrinhoAdapter(itensDoCarrinho, this);
        recyclerView.setAdapter(adapter);
        atualizarTotal();
    }

    @SuppressLint("DefaultLocale")
    private void atualizarTotal() {
        double total = 0;
        for (ItemCarrinho item : CarrinhoSingleton.getInstance().getItensDoCarrinho()) {
            total += (item.getPreco() * item.getQuantidade());
        }
        tvTotal.setText(String.format("Total: R$ %.2f", total));

        if (CarrinhoSingleton.getInstance().isEmpty()) {
            Toast.makeText(this, "Carrinho esvaziado!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onAumentarClick(int position) {
        ItemCarrinho item = itensDoCarrinho.get(position);
        item.incrementarQuantidade();
        adapter.notifyItemChanged(position);
        atualizarTotal();
    }

    @Override
    public void onDiminuirClick(int position) {
        ItemCarrinho item = itensDoCarrinho.get(position);
        if (item.getQuantidade() > 1) {
            item.setQuantidade(item.getQuantidade() - 1);
            adapter.notifyItemChanged(position);
        } else {
            onRemoverClick(position);
        }
        atualizarTotal();
    }

    @Override
    public void onRemoverClick(int position) {
        ItemCarrinho itemParaRemover = itensDoCarrinho.get(position);
        CarrinhoSingleton.getInstance().removerItem(itemParaRemover);
        setupRecyclerView();
    }

    // --- LÓGICA DE FINALIZAÇÃO DO PEDIDO (MÉTODO ATUALIZADO) ---

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void finalizarPedido() {
        List<ItemCarrinho> itensParaSalvar = CarrinhoSingleton.getInstance().getItensDoCarrinho();
        double total = 0;
        for (ItemCarrinho item : itensParaSalvar) {
            total += item.getPreco() * item.getQuantidade();
        }

        Log.d("CarrinhoActivity", "Finalizando pedido para usuário ID: " + idDoUsuarioLogado);

        int senhaPermanente = dbHelper.salvarPedido(itensParaSalvar, total, idDoUsuarioLogado);

        if (senhaPermanente == -1) {
            Toast.makeText(this, "Erro ao processar o pedido.", Toast.LENGTH_SHORT).show();
            Log.e("CarrinhoActivity", "dbHelper.salvarPedido retornou -1.");
            return;
        }

        // 1. Infla (carrega) o layout customizado que você criou
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pedido_finalizado, null);

        // 2. Cria o AlertDialog usando o seu layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        // 3. Remove o fundo padrão para que os cantos arredondados apareçam
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // 4. Pega os componentes de dentro do seu layout
        TextView tvTitle = dialogView.findViewById(R.id.dialog_title_finalizado);
        TextView tvMsgPrincipal = dialogView.findViewById(R.id.dialog_message_principal);
        TextView tvMsgDetalhes = dialogView.findViewById(R.id.dialog_message_detalhes);
        TextView tvMsgAviso = dialogView.findViewById(R.id.dialog_message_aviso);
        Button btnOk = dialogView.findViewById(R.id.dialog_button_ok_finalizado);

        // 5. Monta as mensagens com os emojis e define nos TextViews
        tvTitle.setText("✅ Pedido Finalizado");
        tvMsgPrincipal.setText("Seu pedido foi realizado com sucesso!");
        tvMsgDetalhes.setText(String.format("🧾 Total: R$ %.2f\n🔐 Sua senha é: %d", total, senhaPermanente));
        String textoAvisoHtml = "<font color='#FFEB3B'><big>" +
                "A forma de <b>PAGAMENTO</b> é escolhida no <b>CAIXA</b>." +
                "<br><br>" +
                "<b>Guarde sua SENHA</b> e fique de olho no <b>TELÃO</b>!" +
                "</big></font>";

        // Código para garantir compatibilidade com todas as versões do Android
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvMsgAviso.setText(Html.fromHtml(textoAvisoHtml, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvMsgAviso.setText(Html.fromHtml(textoAvisoHtml));
        }

        // 6. Define a ação do botão OK
        btnOk.setOnClickListener(view -> {
            dialog.dismiss(); // Fecha o diálogo
            CarrinhoSingleton.getInstance().limpar(); // Limpa o carrinho
            finish(); // Fecha a CarrinhoActivity
        });

        // 7. Mostra o diálogo!
        dialog.show();
    }
}
