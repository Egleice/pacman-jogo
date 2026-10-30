package org.pacman.controller;

import org.pacman.model.Direcao;
import org.pacman.model.MapaJogo;
import org.pacman.model.Pacman;
import org.pacman.model.Pontuacao;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class ControladorJogo {
    private final MapaJogo mapa;
    private final Pacman pacman;
    private final Pontuacao pontuacao = new Pontuacao();
    private final Sound somComer =
        Gdx.audio.newSound(Gdx.files.internal("sounds/comer.mp3"));
    private final ManipuladorEntrada entrada = new ManipuladorEntrada();

    public ControladorJogo(MapaJogo mapa, Pacman pacman) {
        this.mapa = mapa;
        this.pacman = pacman;
    }

    public Pontuacao getPontuacao() {
        return pontuacao;
    }

    public void atualizar(float delta) {
        Direcao direcao = entrada.lerDirecao();
        if (direcao != Direcao.NENHUMA) {
            pacman.setDirecaoDesejada(direcao);
        }
        pacman.atualizar(delta, mapa);
        comerPonto();
    }

    private void comerPonto() {
        int linha = pacman.getLinha();
        int coluna = pacman.getColuna();

        int pontos = mapa.pontosDoItem(linha, coluna);

        if (pontos > 0) {
            pontuacao.adicionar(pontos);
            mapa.removerPonto(linha, coluna);
            somComer.play();
        }
    }

    public void dispose() {
        somComer.dispose();;;;;;
    }
}
