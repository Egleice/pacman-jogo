package org.pacman.controller;

import org.pacman.model.Direcao;
import org.pacman.model.MapaJogo;
import org.pacman.model.Pacman;

public class ControladorJogo {
    private final MapaJogo mapa;
    private final Pacman pacman;
    private final ManipuladorEntrada entrada = new ManipuladorEntrada();

    public ControladorJogo(MapaJogo mapa, Pacman pacman) {
        this.mapa = mapa;
        this.pacman = pacman;
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
        if (mapa.temPonto(pacman.getLinha(), pacman.getColuna())) {
            mapa.removerPonto(pacman.getLinha(), pacman.getColuna());
        }
    }
}
