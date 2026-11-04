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

    private static final float TEMPO_APOS_PAUSA = 0.3f;
    private static final float TEMPO_APOS_PERDER_VIDA = 2f;

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
    private float tempoRetomada = 0f;

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

        // Evita tempos negativos
        delta = Math.max(0f, delta);

        // Iniciar o jogo com Enter
        if (estado == EstadoJogo.MENU) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                estado = EstadoJogo.JOGANDO;
            }

            return;
        }

        // Reiniciar apos Game Over ou Vitoria
        if (estado == EstadoJogo.GAME_OVER
                || estado == EstadoJogo.VITORIA) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                reiniciarJogo();
            }

            return;
        }

        // Pausar e continuar com P ou Esc
        boolean teclaPausa =
            Gdx.input.isKeyJustPressed(Input.Keys.P)
            || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);

        if (teclaPausa) {

            if (estado == EstadoJogo.JOGANDO) {

                estado = EstadoJogo.PAUSADO;
                return;

            } else if (estado == EstadoJogo.PAUSADO) {

                estado = EstadoJogo.JOGANDO;
                tempoRetomada = TEMPO_APOS_PAUSA;
                return;
            }
        }

        // Enquanto pausado, nada avanca
        if (estado != EstadoJogo.JOGANDO) {
            return;
        }

        // Espera apos voltar da pausa
        if (tempoRetomada > 0f) {

            tempoRetomada =
                Math.max(0f, tempoRetomada - delta);

            return;
        }

        // Espera apos perder uma vida
        if (tempoProtecao > 0f) {

            tempoProtecao =
                Math.max(0f, tempoProtecao - delta);

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

        // Conferir se venceu
        verificarVitoria();
    }

    private void comerPonto() {

        int linha = pacman.getLinha();
        int coluna = pacman.getColuna();

        int pontos = mapa.pontosDoItem(linha, coluna);

        if (pontos <= 0) {
            return;
        }

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

    // Chamado quando o Pac-Man perde uma vida
    public void perderVida() {

        if (estado != EstadoJogo.JOGANDO) {
            return;
        }

        if (tempoProtecao > 0f
                || tempoRetomada > 0f) {
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

            tempoProtecao = TEMPO_APOS_PERDER_VIDA;
        }
    }

    // Reiniciar a partida completa
    public void reiniciarJogo() {

        vidas = 3;

        pontuacao.zerar();

        mapa.reiniciarItens();

        pacman.reiniciarPosicao(
            mapa.getLinhaInicialPacman(),
            mapa.getColunaInicialPacman()
        );

        tempoProtecao = 0f;
        tempoRetomada = 0f;

        estado = EstadoJogo.JOGANDO;
    }

    public void dispose() {

        somComer.dispose();
        somEspecial.dispose();
        somFruta.dispose();
    }
}
