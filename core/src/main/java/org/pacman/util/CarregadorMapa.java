package org.pacman.util;

import org.pacman.model.MapaJogo;

public class CarregadorMapa {

    public static MapaJogo carregar(String texto) {
        String[] linhas = texto.split("\\R");
        char[][] celulas = new char[linhas.length][linhas[0].length()];
        int linhaPacman = 0;
        int colunaPacman = 0;

        for (int i = 0; i < linhas.length; i++) {
            for (int j = 0; j < linhas[i].length(); j++) {
                char c = linhas[i].charAt(j);
                if (c == 'P') {
                    linhaPacman = i;
                    colunaPacman = j;
                    c = ' ';
                } else if (c == 'G') {
                    c = ' ';
                }
                celulas[i][j] = c;
            }
        }
        return new MapaJogo(celulas, linhaPacman, colunaPacman);
    }
}
