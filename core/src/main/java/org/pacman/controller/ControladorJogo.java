package org.pacman.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;

import org.pacman.model.Direcao;
import org.pacman.model.EstadoJogo;
import org.pacman.model.MapaJogo;
import org.pacman.model.Pacman;
import org.pacman.model.Pontuacao;

public class ControladorJogo {

    private final MapaJogo mapa;
    private final Pacman pacman;

    private final Pontuacao pontuacao = new Pontuacao();

    private final Sound somComer =
        Gdx.audio.newSound(Gdx.files.internal("sounds/comer.mp3"));

    private final Sound somEspecial =
        Gdx.audio.newSound(Gdx.files.internal("sounds/especial.mp3"));

    private final Sound somFruta =
        Gdx.audio.newSound(Gdx.files.internal("sounds/fruta.mp3"));

    private final ManipuladorEntrada entrada =
        new ManipuladorEntrada();

    private EstadoJogo estado = EstadoJogo.MENU;

    private int vidas = 3;

    private float tempoProtecao = 0f;

    public ControladorJogo(MapaJogo mapa, Pacman pacman) {
        this.mapa = mapa;
        this.pacman = pacman;
    }

    public Pontuacao getPontuacao() {
        return pontuacao;
    }

    public EstadoJogo getEstado() {
        return estado;
    }

    public int getVidas() {
        return vidas;
    }

    public void atualizar(float delta) {

        // Iniciar com Enter
        if (estado == EstadoJogo.MENU) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                estado = EstadoJogo.JOGANDO;
            }
            return;
        }

        // Pausar ou continuar
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            if (estado == EstadoJogo.JOGANDO) {
                estado = EstadoJogo.PAUSADO;
            } else if (estado == EstadoJogo.PAUSADO) {
                estado = EstadoJogo.JOGANDO;
            }
        }

        if (estado != EstadoJogo.JOGANDO) {
            return;
        }

        // Esperar dois segundos depois de perder vida
        if (tempoProtecao > 0f) {
            tempoProtecao -= delta;

            if (tempoProtecao < 0f) {
                tempoProtecao = 0f;
            }

            return;
        }

        // Movimentação
        Direcao direcao = entrada.lerDirecao();

        if (direcao != Direcao.NENHUMA) {
            pacman.setDirecaoDesejada(direcao);
        }

        pacman.atualizar(delta, mapa);

        // Coletar itens
        comerPonto();

        // Verificar vitória
        verificarVitoria();
    }

    private void comerPonto() {

        int linha = pacman.getLinha();
        int coluna = pacman.getColuna();

        int pontos = mapa.pontosDoItem(linha, coluna);

        if (pontos > 0) {

            pontuacao.adicionar(pontos);
            mapa.removerPonto(linha, coluna);

            if (pontos == 10) {
                somComer.play();
            } else if (pontos == 50) {
                somEspecial.play();
            } else if (pontos == 100) {
                somFruta.play();
            }
        }
    }

    // Vence quando todas as bolinhas acabarem
    private void verificarVitoria() {

        for (int linha = 0; linha < mapa.getLinhas(); linha++) {

            for (int coluna = 0;
                 coluna < mapa.getColunas();
                 coluna++) {

                int pontos = mapa.pontosDoItem(linha, coluna);

                if (pontos == 10 || pontos == 50) {
                    return;
                }
            }
        }

        estado = EstadoJogo.VITORIA;
    }

    // Perder vida e voltar ao começo
    public void perderVida() {

        if (estado != EstadoJogo.JOGANDO) {
            return;
        }

        if (tempoProtecao > 0f) {
            return;
        }

        vidas--;

        if (vidas <= 0) {
            vidas = 0;
            estado = EstadoJogo.GAME_OVER;
        } else {

            pacman.reiniciarPosicao(
                mapa.getLinhaInicialPacman(),
                mapa.getColunaInicialPacman()
            );

            tempoProtecao = 2f;
        }
    }

    public void dispose() {
        somComer.dispose();
        somEspecial.dispose();
        somFruta.dispose();
    }
}
