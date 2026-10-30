package org.pacman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

    @Override
    public void create() {
        String texto = Gdx.files.internal("maps/mapa1.txt").readString();
        mapa = CarregadorMapa.carregar(texto);
        pacman = new Pacman(mapa.getLinhaInicialPacman(), mapa.getColunaInicialPacman());
        controlador = new ControladorJogo(mapa, pacman);
        renderizador = new RenderizadorJogo();
    }

    @Override
    public void render() {
        controlador.atualizar(Gdx.graphics.getDeltaTime());
        ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1f);
        renderizador.desenhar(mapa, pacman, controlador.getPontuacao());
    }

    @Override
    public void dispose() {
        renderizador.dispose();
    }
}
