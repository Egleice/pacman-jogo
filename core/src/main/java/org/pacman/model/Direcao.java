package org.pacman.model;

public enum Direcao {
    CIMA(-1, 0),
    BAIXO(1, 0),
    ESQUERDA(0, -1),
    DIREITA(0, 1),
    NENHUMA(0, 0);

    private final int deltaLinha;
    private final int deltaColuna;

    Direcao(int deltaLinha, int deltaColuna) {
        this.deltaLinha = deltaLinha;
        this.deltaColuna = deltaColuna;
    }

    public int getDeltaLinha() { return deltaLinha; }
    public int getDeltaColuna() { return deltaColuna; }
}
