package controle;

import Base.Elevador;
import EstruturaDados.Lista;

public interface HeuristicaControle {
    void decidir(Lista elevadores, Lista andares, int minutoAtual);
}