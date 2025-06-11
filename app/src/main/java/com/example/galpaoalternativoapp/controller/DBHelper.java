package com.example.galpaoalternativoapp.controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.galpaoalternativoapp.model.Avaliacao;
import com.example.galpaoalternativoapp.model.ItemCardapio;
import com.example.galpaoalternativoapp.model.MensagemMural; // Adicionar este import
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "galpao_alternativo.db";
    private static final int VERSAO_BANCO = 7; // Mantenha ou INCREMENTE este número para forçar onUpgrade()

    // Tabelas e colunas
    private static final String TABELA_USUARIOS = "usuarios";
    private static final String COLUNA_ID = "id";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_EMAIL = "email";
    private static final String COLUNA_SENHA = "senha";

    private static final String TABELA_PRODUTOS = "produtos";
    private static final String COLUNA_PRODUTO_ID = "id";
    private static final String COLUNA_NOME_PRODUTO = "nome";
    private static final String COLUNA_DESCRICAO = "descricao";
    private static final String COLUNA_PRECO = "preco";
    private static final String COLUNA_CATEGORIA = "categoria";

    private static final String TABELA_PEDIDOS = "pedidos";
    private static final String COLUNA_PEDIDO_ID = "id";
    private static final String COLUNA_USUARIO_ID = "usuario_id";
    private static final String COLUNA_ITENS_PEDIDO = "itens";
    private static final String COLUNA_TOTAL = "total";
    private static final String COLUNA_SENHA_PEDIDO = "senha";

    private static final String TABELA_EVENTOS = "eventos";
    private static final String COLUNA_EVENTO_ID = "id";
    private static final String COLUNA_TITULO_EVENTO = "titulo";
    private static final String COLUNA_DATA_EVENTO = "data";
    private static final String COLUNA_DESCRICAO_EVENTO = "descricao";

    private static final String TABELA_AVALIACOES = "avaliacoes";
    private static final String COLUNA_AVALIACAO_ID = "id";
    private static final String COLUNA_USUARIO_AVALIACAO = "usuario_id";
    private static final String COLUNA_TIPO_AVALIACAO = "tipo";
    private static final String COLUNA_NOTA = "nota";
    private static final String COLUNA_COMENTARIO = "comentario";

    private static final String TABELA_MURAL = "mural";
    private static final String COLUNA_MENSAGEM_ID = "id";
    private static final String COLUNA_USUARIO_MURAL = "usuario_id";
    private static final String COLUNA_TEXTO_MURAL = "texto";

    public DBHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABELA_USUARIOS + "("
                + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUNA_NOME + " TEXT NOT NULL,"
                + COLUNA_EMAIL + " TEXT UNIQUE NOT NULL,"
                + COLUNA_SENHA + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABELA_PRODUTOS + "("
                + COLUNA_PRODUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUNA_NOME_PRODUTO + " TEXT NOT NULL,"
                + COLUNA_DESCRICAO + " TEXT,"
                + COLUNA_PRECO + " REAL NOT NULL,"
                + COLUNA_CATEGORIA + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABELA_PEDIDOS + "("
                + COLUNA_PEDIDO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUNA_USUARIO_ID + " INTEGER,"
                + COLUNA_ITENS_PEDIDO + " TEXT NOT NULL,"
                + COLUNA_TOTAL + " REAL NOT NULL,"
                + COLUNA_SENHA_PEDIDO + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABELA_EVENTOS + "("
                + COLUNA_EVENTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUNA_TITULO_EVENTO + " TEXT NOT NULL,"
                + COLUNA_DATA_EVENTO + " TEXT,"
                + COLUNA_DESCRICAO_EVENTO + " TEXT)");

        db.execSQL("CREATE TABLE " + TABELA_AVALIACOES + "("
                + COLUNA_AVALIACAO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUNA_USUARIO_AVALIACAO + " INTEGER,"
                + COLUNA_TIPO_AVALIACAO + " TEXT,"
                + COLUNA_NOTA + " REAL,"
                + COLUNA_COMENTARIO + " TEXT)");

        db.execSQL("CREATE TABLE " + TABELA_MURAL + "("
                + COLUNA_MENSAGEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUNA_USUARIO_MURAL + " INTEGER,"
                + COLUNA_TEXTO_MURAL + " TEXT)");

        Log.d("DBHelper", "Todas as tabelas foram criadas com sucesso!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", "onUpgrade: Deleting all tables. Old version: " + oldVersion + ", New version: " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PRODUTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PEDIDOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_EVENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_AVALIACOES);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_MURAL);
        onCreate(db);
    }


    public int verificarUsuario(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        int userId = -1; // Valor padrão para usuário não encontrado

        try {
            // A query agora seleciona apenas a coluna do ID do usuário
            String query = "SELECT " + COLUNA_ID + " FROM " + TABELA_USUARIOS + " WHERE " +
                    COLUNA_EMAIL + " = ? AND " + COLUNA_SENHA + " = ?";
            cursor = db.rawQuery(query, new String[]{email, senha});

            // Verifica se o cursor retornou algum resultado e se move para o primeiro
            if (cursor != null && cursor.moveToFirst()) {
                // Se encontrou o usuário, obtém o índice da coluna do ID
                int idIndex = cursor.getColumnIndex(COLUNA_ID);

                // Verifica se a coluna COLUNA_ID realmente existe na tabela
                if (idIndex != -1) {
                    userId = cursor.getInt(idIndex); // Obtém o valor inteiro do ID
                } else {
                    Log.e("DBHelper", "Coluna " + COLUNA_ID + " não encontrada na tabela " + TABELA_USUARIOS);
                }
            }

            // Logs de depuração (remova em produção)
            Log.d("DBHelper", "Verificando usuário: Email=" + email + ", Senha=" + senha);
            Log.d("DBHelper", "ID do usuário encontrado: " + userId);

        } catch (Exception e) {
            Log.e("DBHelper", "Erro ao verificar usuário e obter ID: " + e.getMessage());
        } finally {
            // Garante que o cursor e o banco de dados sejam fechados
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return userId; // Retorna o ID do usuário ou -1 se não encontrado
    }

    public long cadastrarUsuario(String nome, String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABELA_USUARIOS + " WHERE " + COLUNA_EMAIL + " = ?", new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            Log.e("DBHelper", "Erro: Email já cadastrado!");
            return -1;
        }
        cursor.close();

        Log.d("DBHelper", "Inserindo usuário: " + nome + " | Email: " + email);

        ContentValues values = new ContentValues();
        values.put(COLUNA_NOME, nome);
        values.put(COLUNA_EMAIL, email);
        values.put(COLUNA_SENHA, senha);

        long resultado = db.insert(TABELA_USUARIOS, null, values);

        if (resultado != -1) {
            Log.d("DBHelper", "Usuário cadastrado com sucesso! ID: " + resultado);
        } else {
            Log.e("DBHelper", "Erro ao cadastrar usuário no banco de dados.");
        }

        db.close();
        return resultado;
    }

    public List<String> getTodosUsuarios() {
        List<String> usuarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUNA_ID + ", " + COLUNA_NOME + ", " + COLUNA_EMAIL + " FROM " + TABELA_USUARIOS, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUNA_ID));
                @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex(COLUNA_NOME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUNA_EMAIL));
                usuarios.add(id + " - " + nome + " (" + email + ")");
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return usuarios;
    }

    public List<String> listarUsuarios() {
        return getTodosUsuarios();
    }

    public void excluirUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_USUARIOS, COLUNA_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public List<MensagemMural> listarMensagensMuralCompleto() {
        List<MensagemMural> mensagens = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT m." + COLUNA_MENSAGEM_ID + ", m." + COLUNA_USUARIO_MURAL + ", m." + COLUNA_TEXTO_MURAL +
                ", u." + COLUNA_NOME +
                " FROM " + TABELA_MURAL + " m " +
                " LEFT JOIN " + TABELA_USUARIOS + " u ON m." + COLUNA_USUARIO_MURAL + " = u." + COLUNA_ID +
                " ORDER BY m." + COLUNA_MENSAGEM_ID + " ASC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int idMensagem = cursor.getInt(cursor.getColumnIndex(COLUNA_MENSAGEM_ID));
                @SuppressLint("Range") int usuarioId = cursor.getInt(cursor.getColumnIndex(COLUNA_USUARIO_MURAL));
                @SuppressLint("Range") String texto = cursor.getString(cursor.getColumnIndex(COLUNA_TEXTO_MURAL));
                @SuppressLint("Range") String nomeUsuario = cursor.getString(cursor.getColumnIndex(COLUNA_NOME));

                if (nomeUsuario == null || nomeUsuario.isEmpty()) {
                    nomeUsuario = "Anônimo";
                }

                MensagemMural mensagem = new MensagemMural(idMensagem, usuarioId, nomeUsuario, texto);
                mensagens.add(mensagem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return mensagens;
    }


    public void excluirMensagemMural(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_MURAL, COLUNA_MENSAGEM_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public long adicionarMensagemMural(String texto, int usuarioId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_TEXTO_MURAL, texto);
        values.put(COLUNA_USUARIO_MURAL, usuarioId);

        long resultado = db.insert(TABELA_MURAL, null, values);
        db.close();
        return resultado;
    }

    public int atualizarMensagemMural(int idMensagem, String novoTexto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_TEXTO_MURAL, novoTexto);
        int rowsAffected = db.update(TABELA_MURAL, values, COLUNA_MENSAGEM_ID + " = ?",
                new String[]{String.valueOf(idMensagem)});
        db.close();
        return rowsAffected;
    }

    public long adicionarAvaliacao(int usuarioId, String tipo, float nota, String comentario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_USUARIO_AVALIACAO, usuarioId);
        values.put(COLUNA_TIPO_AVALIACAO, tipo);
        values.put(COLUNA_NOTA, nota);
        values.put(COLUNA_COMENTARIO, comentario);
        long id = db.insert(TABELA_AVALIACOES, null, values);
        db.close();
        return id;
    }

    public List<Avaliacao> listarAvaliacoes() {
        List<Avaliacao> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.id, a.usuario_id, a.tipo, a.nota, a.comentario, u.nome FROM "
                + TABELA_AVALIACOES + " a LEFT JOIN " + TABELA_USUARIOS + " u ON a."
                + COLUNA_USUARIO_AVALIACAO + " = u." + COLUNA_ID + " ORDER BY a.id DESC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int usuarioId = cursor.getInt(cursor.getColumnIndex("usuario_id"));
                @SuppressLint("Range") String tipo = cursor.getString(cursor.getColumnIndex("tipo"));
                @SuppressLint("Range") float nota = cursor.getFloat(cursor.getColumnIndex("nota"));
                @SuppressLint("Range") String comentario = cursor.getString(cursor.getColumnIndex("comentario"));
                @SuppressLint("Range") String nomeUsuario = cursor.getString(cursor.getColumnIndex("nome"));

                if (nomeUsuario == null || nomeUsuario.isEmpty()) nomeUsuario = "Anônimo";

                Avaliacao avaliacao = new Avaliacao(id, usuarioId, nomeUsuario, tipo, nota, comentario);
                lista.add(avaliacao);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public int atualizarAvaliacao(int id, String tipo, float nota, String comentario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_TIPO_AVALIACAO, tipo);
        values.put(COLUNA_NOTA, nota);
        values.put(COLUNA_COMENTARIO, comentario);
        return db.update(TABELA_AVALIACOES, values, COLUNA_AVALIACAO_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void excluirAvaliacao(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_AVALIACOES, COLUNA_AVALIACAO_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int salvarPedido(List<ItemCardapio> itens, double total, int usuarioId) {
        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder itensComoTexto = new StringBuilder();
        for (ItemCardapio item : itens) {
            itensComoTexto.append(String.format(Locale.getDefault(), "%s (R$ %.2f)\n", item.getNome(), item.getPreco()));
        }

        int senhaGerada = new Random().nextInt(9000) + 1000;

        ContentValues values = new ContentValues();
        values.put(COLUNA_USUARIO_ID, usuarioId);
        values.put(COLUNA_ITENS_PEDIDO, itensComoTexto.toString());
        values.put(COLUNA_TOTAL, total);
        values.put(COLUNA_SENHA_PEDIDO, senhaGerada);

        long idPedido = db.insert(TABELA_PEDIDOS, null, values);

        db.close();

        if (idPedido != -1) {
            Log.d("DBHelper", "Pedido salvo com sucesso! Senha: " + senhaGerada + " ID: " + idPedido);
            return senhaGerada;
        } else {
            Log.e("DBHelper", "Erro ao salvar o pedido.");
            return -1;
        }
    }
    public String getUltimoPedidoDoUsuario(int usuarioId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] colunas = {COLUNA_ITENS_PEDIDO, COLUNA_TOTAL, COLUNA_SENHA_PEDIDO};

        Log.d("DBHelperPedido", "Buscando último pedido para o usuário ID: " + usuarioId); // Log 1

        Cursor cursor = db.query(TABELA_PEDIDOS, colunas, COLUNA_USUARIO_ID + " = ?",
                new String[]{String.valueOf(usuarioId)}, null, null, COLUNA_PEDIDO_ID + " DESC", "1");

        String detalhesPedido = null;

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String itens = cursor.getString(cursor.getColumnIndex(COLUNA_ITENS_PEDIDO));
            @SuppressLint("Range") double total = cursor.getDouble(cursor.getColumnIndex(COLUNA_TOTAL));
            @SuppressLint("Range") int senha = cursor.getInt(cursor.getColumnIndex(COLUNA_SENHA_PEDIDO));

            detalhesPedido = String.format(
                    Locale.getDefault(),
                    "Itens do seu último pedido:\n%s\n" +
                            "Total: R$ %.2f\n" +
                            "Senha do Pedido: %d",
                    itens, total, senha
            );
            Log.d("DBHelperPedido", "Pedido encontrado: " + detalhesPedido); // Log 2
        } else {
            Log.d("DBHelperPedido", "Nenhum pedido encontrado para o usuário ID: " + usuarioId); // Log 3
        }

        cursor.close();
        db.close();
        return detalhesPedido;
    }
}