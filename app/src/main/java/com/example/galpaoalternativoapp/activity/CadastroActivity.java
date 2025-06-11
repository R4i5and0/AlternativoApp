package com.example.galpaoalternativoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.galpaoalternativoapp.R;
import com.example.galpaoalternativoapp.controller.DBHelper;
// Importação necessária para os campos de texto modernos
import com.google.android.material.textfield.TextInputEditText;

public class CadastroActivity extends AppCompatActivity {

    // ALTERADO: Trocando para TextInputEditText para compatibilidade com o layout moderno
    private TextInputEditText edtNome, edtEmail, edtSenha, edtConfirmarSenha;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dbHelper = new DBHelper(this);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmarSenha = findViewById(R.id.edtConfirmarSenha); // NOVO: Encontrando o novo campo

        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString().trim();
        String confirmarSenha = edtConfirmarSenha.getText().toString().trim(); // NOVO: Pegando o valor do novo campo

        // ALTERADO: Adicionado a verificação do novo campo
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // NOVO: Verificação para garantir que as senhas são iguais
        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            edtConfirmarSenha.setError("As senhas precisam ser iguais"); // Dica visual do erro
            return; // Para o cadastro aqui
        }

        // Se tudo estiver certo, o resto do código continua igual
        long resultado = dbHelper.cadastrarUsuario(nome, email, senha);

        if (resultado == -1) {
            Log.e("CadastroActivity", "Erro ao cadastrar usuário: Email já existente.");
            Toast.makeText(this, "Email já cadastrado! Use outro email.", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("CadastroActivity", "Usuário cadastrado com sucesso: " + email);
            Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();
            finish(); // Volta para a tela de login sem criar novas telas
        }
    }
}