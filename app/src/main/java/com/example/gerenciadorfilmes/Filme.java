package com.example.gerenciadorfilmes;

public class Filme {

    private String id;
    private String nome;
    private String tipo;
    private String genero;
    private int anoLancamento;
    private float notaPessoal;
    private boolean assistido;

    public Filme() {
    }

    public Filme(String nome, String tipo, String genero, int anoLancamento, float notaPessoal, boolean assistido) {
        this.nome = nome;
        this.tipo = tipo;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.notaPessoal = notaPessoal;
        this.assistido = assistido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public float getNotaPessoal() {
        return notaPessoal;
    }

    public void setNotaPessoal(float notaPessoal) {
        this.notaPessoal = notaPessoal;
    }

    public boolean isAssistido() {
        return assistido;
    }

    public void setAssistido(boolean assistido) {
        this.assistido = assistido;
    }
}
