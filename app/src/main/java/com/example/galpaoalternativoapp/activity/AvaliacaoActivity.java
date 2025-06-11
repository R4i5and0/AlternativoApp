// Arquivo: activity/AvaliacaoActivity.java - VERSÃO FINAL COMPLETA
package com.example.galpaoalternativoapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.controller.DBHelper;
import com.example.galpaoalternativoapp.model.Avaliacao;
import java.util.List;

public class AvaliacaoActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerTipoAutoComplete;
    private RatingBar ratingBar;
    private EditText etComentario;
    private Button btnEnviar, btnVerAvaliacoes;
    private TextView tvTitulo;
    private DBHelper dbHelper;
    private int idDoUsuarioLogado = -1;
    private int idAvaliacaoParaEditar = -1; // -1 significa que é uma nova avaliação

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        dbHelper = new DBHelper(this);
        idDoUsuarioLogado = getIntent().getIntExtra("ID_DO_USUARIO", -1);

        // Encontrando os componentes pelo ID
        spinnerTipoAutoComplete = findViewById(R.id.spinnerTipoAutoComplete);
        ratingBar = findViewById(R.id.ratingBar);
        etComentario = findViewById(R.id.etComentario);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnVerAvaliacoes = findViewById(R.id.btnVerAvaliacoes);
        // Para este ID funcionar, adicione no seu activity_avaliacao.xml: android:id="@+id/tvTituloAvaliacao"
        // no TextView "AVALIE NOSSA NOITE"
        tvTitulo = findViewById(R.id.tvTituloAvaliacao);

        // Configurando o adaptador para o menu de seleção
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.avaliacao_tipos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoAutoComplete.setAdapter(adapter);

        // --- LÓGICA PARA VERIFICAR SE É UMA EDIÇÃO ---
        if (getIntent().hasExtra("EDIT_AVALIACAO_ID")) {
            preencherCamposParaEdicao();
        }

        btnEnviar.setOnClickListener(v -> enviarOuAtualizarAvaliacao());

        btnVerAvaliacoes.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaAvaliacoesActivity.class);
            intent.putExtra("ID_DO_USUARIO", idDoUsuarioLogado);
            startActivity(intent);
        });
    }

    private void preencherCamposParaEdicao() {
        tvTitulo.setText("EDITE SUA AVALIAÇÃO");
        idAvaliacaoParaEditar = getIntent().getIntExtra("EDIT_AVALIACAO_ID", -1);
        String tipo = getIntent().getStringExtra("EDIT_TIPO");
        float nota = getIntent().getFloatExtra("EDIT_NOTA", 0);
        String comentario = getIntent().getStringExtra("EDIT_COMENTARIO");

        spinnerTipoAutoComplete.setText(tipo, false);
        ratingBar.setRating(nota);
        etComentario.setText(comentario);
        btnEnviar.setText("ATUALIZAR AVALIAÇÃO");
    }

    private void enviarOuAtualizarAvaliacao() {
        String tipoSelecionado = spinnerTipoAutoComplete.getText().toString();
        float estrelas = ratingBar.getRating();
        String comentario = etComentario.getText().toString().trim();

        // Validações...
        if (tipoSelecionado.isEmpty() || tipoSelecionado.equals("Selecione o tipo de avaliação")) {
            Toast.makeText(this, "Selecione o tipo de avaliação.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (estrelas == 0) {
            Toast.makeText(this, "Por favor, dê uma nota.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Decide se vai criar um novo ou atualizar um existente
        if (idAvaliacaoParaEditar == -1) {
            // CRIANDO uma nova avaliação
            long resultado = dbHelper.adicionarAvaliacao(idDoUsuarioLogado, tipoSelecionado, estrelas, comentario);
            if (resultado != -1) {
                Toast.makeText(this, "✅ Obrigado pela sua avaliação!", Toast.LENGTH_SHORT).show();
                // Abre a lista de avaliações após o envio
                Intent intent = new Intent(this, ListaAvaliacoesActivity.class);
                intent.putExtra("ID_DO_USUARIO", idDoUsuarioLogado);
                startActivity(intent);
                finish(); // Fecha a tela de avaliação
            } else {
                Toast.makeText(this, "❌ Erro ao enviar avaliação.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // ATUALIZANDO uma avaliação existente
            int resultado = dbHelper.atualizarAvaliacao(idAvaliacaoParaEditar, tipoSelecionado, estrelas, comentario);
            if (resultado > 0) {
                Toast.makeText(this, "✅ Avaliação atualizada!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // Avisa a tela anterior (ListaAvaliacoesActivity) que a edição foi um sucesso
                finish(); // Fecha a tela de edição e volta para a lista
            } else {
                Toast.makeText(this, "❌ Erro ao atualizar avaliação.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}