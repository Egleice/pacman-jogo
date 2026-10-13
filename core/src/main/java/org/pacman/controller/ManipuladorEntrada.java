package org.pacman.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.pacman.model.Direcao;

public class ManipuladorEntrada {

    public Direcao lerDirecao() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            return Direcao.CIMA;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            return Direcao.BAIXO;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            return Direcao.ESQUERDA;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            return Direcao.DIREITA;
        }
        return Direcao.NENHUMA;
    }
}
