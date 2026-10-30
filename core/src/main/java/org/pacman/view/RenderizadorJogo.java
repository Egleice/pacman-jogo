package org.pacman.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.pacman.model.MapaJogo;
import org.pacman.model.Pacman;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.pacman.model.Pontuacao;

public class RenderizadorJogo {
    private static final float TAMANHO_CELULA = 40f;

    private final ShapeRenderer formas = new ShapeRenderer();
    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont fonte = new BitmapFont();
    private float deslocamentoX;
    private float deslocamentoY;

    public void desenhar(MapaJogo mapa, Pacman pacman, Pontuacao pontuacao) {
        // centraliza o mapa na janela
        deslocamentoX = (Gdx.graphics.getWidth() - mapa.getColunas() * TAMANHO_CELULA) / 2f;
        deslocamentoY = (Gdx.graphics.getHeight() - mapa.getLinhas() * TAMANHO_CELULA) / 2f;

        formas.begin(ShapeRenderer.ShapeType.Filled);
        desenharMapa(mapa);
        desenharPacman(mapa, pacman);
        formas.end();

        batch.begin();

        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(1.5f);

        fonte.draw(batch, "Pontos: " + pontuacao.getPontos(),
            20, Gdx.graphics.getHeight() - 20);

        fonte.draw(batch, "Recorde: " + pontuacao.getRecorde(),
            20, Gdx.graphics.getHeight() - 55);

        batch.end();
    }

    private void desenharMapa(MapaJogo mapa) {
        for (int linha = 0; linha < mapa.getLinhas(); linha++) {
            for (int coluna = 0; coluna < mapa.getColunas(); coluna++) {
                float x = xDaColuna(coluna);
                float y = yDaLinha(linha, mapa.getLinhas());

                if (mapa.ehParede(linha, coluna)) {
                    formas.setColor(Color.BLUE);
                    formas.rect(x, y, TAMANHO_CELULA, TAMANHO_CELULA);

                } else if (mapa.temFruta(linha, coluna)) {
                    formas.setColor(Color.RED);
                    formas.circle(x + TAMANHO_CELULA / 2f,
                        y + TAMANHO_CELULA / 2f, TAMANHO_CELULA / 4f);

                } else if (mapa.temPonto(linha, coluna)) {
                    formas.setColor(Color.GOLD);
                    formas.circle(x + TAMANHO_CELULA / 2f,
                        y + TAMANHO_CELULA / 2f, TAMANHO_CELULA / 8f);
                }
            }
        }
    }

    private void desenharPacman(MapaJogo mapa, Pacman pacman) {
        float x = xDaColuna(pacman.getColuna());
        float y = yDaLinha(pacman.getLinha(), mapa.getLinhas());
        formas.setColor(Color.YELLOW);
        formas.circle(x + TAMANHO_CELULA / 2f, y + TAMANHO_CELULA / 2f, TAMANHO_CELULA / 2f - 3f);
    }

    // o eixo Y do libGDX cresce para cima, então a linha é invertida
    private float xDaColuna(int coluna) {
        return deslocamentoX + coluna * TAMANHO_CELULA;
    }

    private float yDaLinha(int linha, int totalLinhas) {
        return deslocamentoY + (totalLinhas - 1 - linha) * TAMANHO_CELULA;
    }

    public void dispose() {
        formas.dispose();
        batch.dispose();
        fonte.dispose();
    }
}
