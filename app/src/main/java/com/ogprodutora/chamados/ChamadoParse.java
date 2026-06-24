package com.ogprodutora.chamados;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Chamado")
public class ChamadoParse extends ParseObject {

    public static final String KEY_TITULO = "titulo";
    public static final String KEY_DESCRICAO = "descricao";
    public static final String KEY_LOCAL = "local";
    public static final String KEY_STATUS = "status";
    public static final String KEY_DATA = "data";
    public static final String KEY_IMAGEM_NOME = "imagemNome";

    public ChamadoParse() {
    }

    public String getTitulo() {
        return getString(KEY_TITULO);
    }

    public void setTitulo(String titulo) {
        put(KEY_TITULO, titulo);
    }

    public String getDescricao() {
        return getString(KEY_DESCRICAO);
    }

    public void setDescricao(String descricao) {
        put(KEY_DESCRICAO, descricao);
    }

    public String getLocal() {
        return getString(KEY_LOCAL);
    }

    public void setLocal(String local) {
        put(KEY_LOCAL, local);
    }

    public String getStatus() {
        return getString(KEY_STATUS);
    }

    public void setStatus(String status) {
        put(KEY_STATUS, status);
    }

    public String getData() {
        return getString(KEY_DATA);
    }

    public void setData(String data) {
        put(KEY_DATA, data);
    }

    public String getImagemNome() {
        return getString(KEY_IMAGEM_NOME);
    }

    public void setImagemNome(String imagemNome) {
        put(KEY_IMAGEM_NOME, imagemNome);
    }
}
