package controle;

import Base.Elevador;
import EstruturaDados.Lista;

public abstract class HeuristicaControle {
    public abstract void decidir(Lista elevadores, Lista andares, int minutoAtual);
}