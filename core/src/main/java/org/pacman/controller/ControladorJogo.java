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

        // Iniciar a primeira partida
        if (estado == EstadoJogo.MENU) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                estado = EstadoJogo.JOGANDO;
            }

            return;
        }

        // NOVO: reiniciar depois de vencer ou perder
        if (estado == EstadoJogo.GAME_OVER
                || estado == EstadoJogo.VITORIA) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                reiniciarJogo();
            }

            return;
        }

        // Pausar e continuar
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

        // Espera de 2 segundos apos perder vida
        if (tempoProtecao > 0f) {

            tempoProtecao -= delta;

            if (tempoProtecao < 0f) {
                tempoProtecao = 0f;
            }

            return;
        }

        // Movimentacao do Pac-Man
        Direcao direcao = entrada.lerDirecao();

        if (direcao != Direcao.NENHUMA) {
            pacman.setDirecaoDesejada(direcao);
        }

        pacman.atualizar(delta, mapa);

        // Coletar itens
        comerPonto();

        // Verificar vitoria
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

    private void verificarVitoria() {

        for (int linha = 0;
             linha < mapa.getLinhas();
             linha++) {

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

    // Perde uma vida e retorna ao inicio
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

    // NOVO: reinicia toda a partida
    public void reiniciarJogo() {

        // Restaurar as vidas
        vidas = 3;

        // Zerar pontuacao sem apagar o recorde
        pontuacao.zerar();

        // Recolocar bolinhas e frutas
        mapa.reiniciarItens();

        // Voltar o Pac-Man para o inicio
        pacman.reiniciarPosicao(
            mapa.getLinhaInicialPacman(),
            mapa.getColunaInicialPacman()
        );

        // Limpar o tempo de espera
        tempoProtecao = 0f;

        // Comecar uma nova partida
        estado = EstadoJogo.JOGANDO;
    }

    public void dispose() {
        somComer.dispose();
        somEspecial.dispose();
        somFruta.dispose();
    }
}
