package org.pacman.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import org.pacman.model.EstadoJogo;
import org.pacman.model.MapaJogo;
import org.pacman.model.Pacman;
import org.pacman.model.Pontuacao;

public class RenderizadorJogo {

    private static final float TAMANHO_CELULA = 40f;

    private final ShapeRenderer formas =
        new ShapeRenderer();

    private final SpriteBatch batch =
        new SpriteBatch();

    private final BitmapFont fonte =
        new BitmapFont();

    private final GlyphLayout layout =
        new GlyphLayout();

    private float deslocamentoX;
    private float deslocamentoY;

    private Texture texturaCereja;

    public RenderizadorJogo() {

        texturaCereja = new Texture(
            Gdx.files.internal("images/cereja.png")
        );

        texturaCereja.setFilter(
            Texture.TextureFilter.Nearest,
            Texture.TextureFilter.Nearest
        );
    }

    public void desenhar(
            MapaJogo mapa,
            Pacman pacman,
            Pontuacao pontuacao,
            EstadoJogo estado,
            int vidas) {

        deslocamentoX =
            (Gdx.graphics.getWidth()
                - mapa.getColunas() * TAMANHO_CELULA) / 2f;

        deslocamentoY =
            (Gdx.graphics.getHeight()
                - mapa.getLinhas() * TAMANHO_CELULA) / 2f;

        // Desenhar mapa e Pac-Man
        formas.begin(ShapeRenderer.ShapeType.Filled);

        desenharMapa(mapa);
        desenharPacman(mapa, pacman);

        formas.end();

        // Desenhar textos e cerejas
        batch.begin();

        desenharCerejas(mapa);

        desenharPainel(pontuacao, vidas);

        desenharEstado(estado);

        batch.end();
    }

    private void desenharPainel(
            Pontuacao pontuacao,
            int vidas) {

        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(1.5f);

        float altura = Gdx.graphics.getHeight();

        fonte.draw(
            batch,
            "Pontos: " + pontuacao.getPontos(),
            20,
            altura - 20
        );

        fonte.draw(
            batch,
            "Recorde: " + pontuacao.getRecorde(),
            20,
            altura - 55
        );

        // NOVO: mostrar quantidade de vidas
        fonte.setColor(Color.YELLOW);

        fonte.draw(
            batch,
            "Vidas: " + vidas,
            20,
            altura - 90
        );
    }

    // NOVO: mostrar mensagens dos estados
    private void desenharEstado(EstadoJogo estado) {

        String titulo = "";
        String instrucao = "";

        switch (estado) {

            case MENU:
                titulo = "PAC-MAN";
                instrucao = "APERTE ENTER PARA JOGAR";
                break;

            case PAUSADO:
                titulo = "JOGO PAUSADO";
                instrucao = "APERTE P OU ESC PARA CONTINUAR";
                break;

            case GAME_OVER:
                titulo = "FIM DE JOGO";
                instrucao = "ENTER PARA JOGAR NOVAMENTE";
                break;

            case VITORIA:
                titulo = "VOCE VENCEU!";
                instrucao = "ENTER PARA JOGAR NOVAMENTE";
                break;

            case JOGANDO:
                return;

            default:
                return;
        }

        float largura = Gdx.graphics.getWidth();
        float altura = Gdx.graphics.getHeight();

        // Fundo escuro para facilitar a leitura
        batch.setColor(0f, 0f, 0f, 0.85f);

        Texture pixel = obterPixel();

        batch.draw(
            pixel,
            0,
            altura / 2f - 85,
            largura,
            170
        );

        batch.setColor(Color.WHITE);

        // Titulo centralizado
        fonte.getData().setScale(2.5f);
        fonte.setColor(Color.YELLOW);

        layout.setText(fonte, titulo);

        fonte.draw(
            batch,
            titulo,
            (largura - layout.width) / 2f,
            altura / 2f + 35
        );

        // Instrucao centralizada
        fonte.getData().setScale(1.3f);
        fonte.setColor(Color.WHITE);

        layout.setText(fonte, instrucao);

        fonte.draw(
            batch,
            instrucao,
            (largura - layout.width) / 2f,
            altura / 2f - 30
        );
    }

    // Pixel branco usado no fundo das mensagens
    private Texture pixelBranco;

    private Texture obterPixel() {

        if (pixelBranco == null) {

            com.badlogic.gdx.graphics.Pixmap pixmap =
                new com.badlogic.gdx.graphics.Pixmap(
                    1,
                    1,
                    com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888
                );

            pixmap.setColor(Color.WHITE);
            pixmap.fill();

            pixelBranco = new Texture(pixmap);

            pixmap.dispose();
        }

        return pixelBranco;
    }

    private void desenharMapa(MapaJogo mapa) {

        for (int linha = 0;
             linha < mapa.getLinhas();
             linha++) {

            for (int coluna = 0;
                 coluna < mapa.getColunas();
                 coluna++) {

                float x = xDaColuna(coluna);

                float y = yDaLinha(
                    linha,
                    mapa.getLinhas()
                );

                if (mapa.ehParede(linha, coluna)) {

                    formas.setColor(Color.BLUE);

                    formas.rect(
                        x,
                        y,
                        TAMANHO_CELULA,
                        TAMANHO_CELULA
                    );

                } else if (mapa.temFruta(linha, coluna)) {

                    // A cereja sera desenhada com textura

                } else if (mapa.temPonto(linha, coluna)) {

                    formas.setColor(Color.GOLD);

                    formas.circle(
                        x + TAMANHO_CELULA / 2f,
                        y + TAMANHO_CELULA / 2f,
                        TAMANHO_CELULA / 8f
                    );
                }
            }
        }
    }

    private void desenharCerejas(MapaJogo mapa) {

        for (int linha = 0;
             linha < mapa.getLinhas();
             linha++) {

            for (int coluna = 0;
                 coluna < mapa.getColunas();
                 coluna++) {

                if (mapa.temFruta(linha, coluna)) {

                    float x = xDaColuna(coluna);

                    float y = yDaLinha(
                        linha,
                        mapa.getLinhas()
                    );

                    float tamanho =
                        TAMANHO_CELULA * 0.8f;

                    float margem =
                        (TAMANHO_CELULA - tamanho) / 2f;

                    batch.draw(
                        texturaCereja,
                        x + margem,
                        y + margem,
                        tamanho,
                        tamanho
                    );
                }
            }
        }
    }

    private void desenharPacman(
            MapaJogo mapa,
            Pacman pacman) {

        float x = xDaColuna(pacman.getColuna());

        float y = yDaLinha(
            pacman.getLinha(),
            mapa.getLinhas()
        );

        formas.setColor(Color.YELLOW);

        formas.circle(
            x + TAMANHO_CELULA / 2f,
            y + TAMANHO_CELULA / 2f,
            TAMANHO_CELULA / 2f - 3f
        );
    }

    private float xDaColuna(int coluna) {
        return deslocamentoX
            + coluna * TAMANHO_CELULA;
    }

    private float yDaLinha(
            int linha,
            int totalLinhas) {

        return deslocamentoY
            + (totalLinhas - 1 - linha)
            * TAMANHO_CELULA;
    }

    public void dispose() {

        formas.dispose();
        batch.dispose();
        fonte.dispose();
        texturaCereja.dispose();

        if (pixelBranco != null) {
            pixelBranco.dispose();
        }
    }
}
