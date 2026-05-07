package com.ogprodutora.chamados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chamados.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CHAMADOS = "chamados";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_DESCRICAO = "descricao";
    public static final String COLUMN_LOCAL = "local";
    public static final String COLUMN_TIPO = "tipo";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_SOLUCAO = "solucao";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CHAMADOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITULO + " TEXT,"
                + COLUMN_DATA + " TEXT,"
                + COLUMN_DESCRICAO + " TEXT,"
                + COLUMN_LOCAL + " TEXT,"
                + COLUMN_TIPO + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_SOLUCAO + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMADOS);
        onCreate(db);
    }

    public long addChamado(Chamado chamado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, chamado.getTitulo());
        values.put(COLUMN_DATA, chamado.getData());
        values.put(COLUMN_DESCRICAO, chamado.getDescricao());
        values.put(COLUMN_LOCAL, chamado.getLocal());
        values.put(COLUMN_TIPO, chamado.getTipo());
        values.put(COLUMN_STATUS, chamado.getStatus());
        values.put(COLUMN_SOLUCAO, chamado.getSolucao());

        long id = db.insert(TABLE_CHAMADOS, null, values);
        db.close();
        return id;
    }

    public int updateChamado(Chamado chamado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, chamado.getStatus());
        values.put(COLUMN_SOLUCAO, chamado.getSolucao());

        return db.update(TABLE_CHAMADOS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(chamado.getId())});
    }

    public List<Chamado> getAllChamados() {
        List<Chamado> chamadosList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CHAMADOS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Chamado chamado = new Chamado();
                chamado.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                chamado.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)));
                chamado.setData(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA)));
                chamado.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO)));
                chamado.setLocal(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCAL)));
                chamado.setTipo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO)));
                chamado.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                chamado.setSolucao(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOLUCAO)));
                chamadosList.add(chamado);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chamadosList;
    }
}
