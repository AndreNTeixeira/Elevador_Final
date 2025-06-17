package controle;

import Base.*;
import EstruturaDados.*;

public class HeuristicaSemOtimizacao extends HeuristicaControle {            // Heurística que não interfere no controle dos elevadores
    @Override
    public void decidir(Lista elevadores, Lista andares, int minutoAtual) {  // Método sobrescrito da heurística, chamado a cada minuto
        // não faz nada: cada elevador segue sua lógica interna              // Nenhuma decisão é tomada — comportamento autônomo dos elevadores
    }
}
