package org.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ScreenUtils;

import org.pacman.controller.ControladorJogo;
import org.pacman.model.MapaJogo;
import org.pacman.model.Pacman;
import org.pacman.util.CarregadorMapa;
import org.pacman.view.RenderizadorJogo;

public class Main extends ApplicationAdapter {

    private MapaJogo mapa;
    private Pacman pacman;

    private ControladorJogo controlador;
    private RenderizadorJogo renderizador;

    private Music musicaFundo;

    @Override
    public void create() {

        musicaFundo = Gdx.audio.newMusic(
            Gdx.files.internal("sounds/fundo.mp3")
        );

        musicaFundo.setLooping(true);
        musicaFundo.setVolume(0.2f);
        musicaFundo.play();

        String texto = Gdx.files.internal(
            "maps/mapa1.txt"
        ).readString();

        mapa = CarregadorMapa.carregar(texto);

        pacman = new Pacman(
            mapa.getLinhaInicialPacman(),
            mapa.getColunaInicialPacman()
        );

        controlador = new ControladorJogo(
            mapa,
            pacman
        );

        renderizador = new RenderizadorJogo();
    }

    @Override
    public void render() {

        // Atualiza a logica
        controlador.atualizar(
            Gdx.graphics.getDeltaTime()
        );

        ScreenUtils.clear(
            0.05f,
            0.05f,
            0.1f,
            1f
        );

        // NOVO: envia estado e vidas ao visual
        renderizador.desenhar(
            mapa,
            pacman,
            controlador.getPontuacao(),
            controlador.getEstado(),
            controlador.getVidas()
        );
    }

    @Override
    public void dispose() {

        renderizador.dispose();
        controlador.dispose();
        musicaFundo.dispose();
    }
}
