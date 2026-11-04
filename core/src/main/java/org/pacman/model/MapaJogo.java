package org.pacman.model;

public class MapaJogo {

    private final char[][] celulas;
    private final char[][] celulasIniciais;

    private final int linhaInicialPacman;
    private final int colunaInicialPacman;

    public MapaJogo(char[][] celulas,
                    int linhaInicialPacman,
                    int colunaInicialPacman) {

        this.celulas = celulas;
        this.linhaInicialPacman = linhaInicialPacman;
        this.colunaInicialPacman = colunaInicialPacman;

        // Guarda uma copia do mapa original
        celulasIniciais = new char[celulas.length][];

        for (int i = 0; i < celulas.length; i++) {
            celulasIniciais[i] = celulas[i].clone();
        }
    }

    public int getLinhas() {
        return celulas.length;
    }

    public int getColunas() {
        return celulas[0].length;
    }

    public int getLinhaInicialPacman() {
        return linhaInicialPacman;
    }

    public int getColunaInicialPacman() {
        return colunaInicialPacman;
    }

    private boolean foraDoMapa(int linha, int coluna) {
        return linha < 0
            || linha >= getLinhas()
            || coluna < 0
            || coluna >= celulas[linha].length;
    }

    public boolean ehParede(int linha, int coluna) {

        if (foraDoMapa(linha, coluna)) {
            return true;
        }

        return celulas[linha][coluna] == '#';
    }

    public boolean temPonto(int linha, int coluna) {

        if (foraDoMapa(linha, coluna)) {
            return false;
        }

        char c = celulas[linha][coluna];

        return c == '.' || c == 'o' || c == 'F';
    }

    public void removerPonto(int linha, int coluna) {

        if (temPonto(linha, coluna)) {
            celulas[linha][coluna] = ' ';
        }
    }

    public int pontosDoItem(int linha, int coluna) {

        if (foraDoMapa(linha, coluna)) {
            return 0;
        }

        char item = celulas[linha][coluna];

        if (item == '.') {
            return 10;
        }

        if (item == 'o') {
            return 50;
        }

        if (item == 'F') {
            return 100;
        }

        return 0;
    }

    public boolean temFruta(int linha, int coluna) {

        if (foraDoMapa(linha, coluna)) {
            return false;
        }

        return celulas[linha][coluna] == 'F';
    }

    // NOVO: restaura o mapa para uma nova partida
    public void reiniciarItens() {

        for (int linha = 0;
             linha < celulas.length;
             linha++) {

            System.arraycopy(
                celulasIniciais[linha],
                0,
                celulas[linha],
                0,
                celulas[linha].length
            );
        }
    }
}
