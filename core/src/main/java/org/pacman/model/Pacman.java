package org.pacman.model;

public class Pacman {

    private static final float INTERVALO_MOVIMENTO = 0.15f;

    private int linha;
    private int coluna;

    private Direcao direcaoAtual = Direcao.NENHUMA;
    private Direcao direcaoDesejada = Direcao.NENHUMA;

    private float tempoAcumulado = 0f;

    public Pacman(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setDirecaoDesejada(Direcao direcao) {
        this.direcaoDesejada = direcao;
    }

    public void atualizar(float delta, MapaJogo mapa) {

        tempoAcumulado += delta;

        if (tempoAcumulado < INTERVALO_MOVIMENTO) {
            return;
        }

        tempoAcumulado -= INTERVALO_MOVIMENTO;

        if (podeIr(direcaoDesejada, mapa)) {
            direcaoAtual = direcaoDesejada;
        }

        if (podeIr(direcaoAtual, mapa)) {
            linha += direcaoAtual.getDeltaLinha();
            coluna += direcaoAtual.getDeltaColuna();
        }
    }

    private boolean podeIr(Direcao direcao, MapaJogo mapa) {

        if (direcao == Direcao.NENHUMA) {
            return false;
        }

        return !mapa.ehParede(
            linha + direcao.getDeltaLinha(),
            coluna + direcao.getDeltaColuna()
        );
    }

    // Volta para a posição inicial após perder uma vida
    public void reiniciarPosicao(int linhaInicial, int colunaInicial) {

        linha = linhaInicial;
        coluna = colunaInicial;

        direcaoAtual = Direcao.NENHUMA;
        direcaoDesejada = Direcao.NENHUMA;

        tempoAcumulado = 0f;
    }
}
