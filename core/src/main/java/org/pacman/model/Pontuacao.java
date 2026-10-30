package org.pacman.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Pontuacao {

    private int pontos;
    private int recorde;
    private int fantasmasComidos = 0;

    public Pontuacao() {
        pontos = 0;

        Preferences preferencias =
            Gdx.app.getPreferences("pacman");

        recorde = preferencias.getInteger("recorde", 0);
    }

    public void adicionar(int quantidade) {
        pontos += quantidade;

        if (pontos > recorde) {
            recorde = pontos;

            Preferences preferencias =
                Gdx.app.getPreferences("pacman");

            preferencias.putInteger("recorde", recorde);
            preferencias.flush();
        }
    }

    public int getPontos() {
        return pontos;
    }

    public int getRecorde() {
        return recorde;
    }

    public void zerar() {
        pontos = 0;
        reiniciarComboFantasmas();
    }

    public int comerFantasma() {
        int[] valores = {200, 400, 800, 1600};

        int indice = Math.min(fantasmasComidos, 3);
        int pontosGanhos = valores[indice];

        adicionar(pontosGanhos);
        fantasmasComidos++;

        return pontosGanhos;
    }

    public void reiniciarComboFantasmas() {
        fantasmasComidos = 0;
    }
}
