package org.pacman.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Pontuacao {

    private int pontos;
    private int recorde;

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
    }
}
