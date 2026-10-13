package org.pacman.model;

public class MapaJogo {
    private final char[][] celulas;
    private final int linhaInicialPacman;
    private final int colunaInicialPacman;

    public MapaJogo(char[][] celulas, int linhaInicialPacman, int colunaInicialPacman) {
        this.celulas = celulas;
        this.linhaInicialPacman = linhaInicialPacman;
        this.colunaInicialPacman = colunaInicialPacman;
    }

    public int getLinhas() { return celulas.length; }
    public int getColunas() { return celulas[0].length; }
    public int getLinhaInicialPacman() { return linhaInicialPacman; }
    public int getColunaInicialPacman() { return colunaInicialPacman; }

    private boolean foraDoMapa(int linha, int coluna) {
        return linha < 0 || linha >= getLinhas() || coluna < 0 || coluna >= getColunas();
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
        return c == '.' || c == 'o';
    }

    public void removerPonto(int linha, int coluna) {
        if (temPonto(linha, coluna)) {
            celulas[linha][coluna] = ' ';
        }
    }
}
