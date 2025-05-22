package controle;

import Base.Elevador;
import EstruturaDados.Lista;

public abstract class HeuristicaControle {                                          // Classe abstrata base para definir estratégias (heurísticas) de controle
    public abstract void decidir(Lista elevadores, Lista andares, int minutoAtual); // Método que deve ser implementado por cada heurística para tomar decisões a cada minuto
}
