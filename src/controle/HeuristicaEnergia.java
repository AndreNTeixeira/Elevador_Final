package controle;

import Base.Andar;
import Base.Elevador;
import Base.Pessoa;
import EstruturaDados.Fila;
import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;

public class HeuristicaEnergia extends HeuristicaControle {
    @Override
    public void decidir(Lista elevadores, Lista andares, int minutoAtual) {
        Ponteiro pAndar = andares.getInicio();

        while (pAndar != null) {
            Andar andar = (Andar) pAndar.getElemento();
            Fila fila = andar.getPessoasAguardando();

            if (!fila.estaVazia()) {
                Pessoa primeiraPessoa = (Pessoa) fila.getInicio().getElemento();
                Elevador elevadorMaisProximo = buscarElevadorMaisProximo(elevadores, andar.getNumero());

                if (elevadorMaisProximo != null && elevadorMaisProximo.getAndarAtual() == andar.getNumero()) {
                    // elevador está parado exatamente neste andar → embarque
                    andar.embarcarPessoasPorDestino(elevadorMaisProximo,
                            primeiraPessoa.getAndarDestino(),
                            minutoAtual);
                } else if (elevadorMaisProximo != null) {
                    // apenas REGISTRA a chamada; o embarque será feito
                    // quando o elevador realmente chegar aqui
                    andar.getPainel().registrarChamada(andar.getNumero());
                }
            }

            pAndar = pAndar.getProximo();
        }
    }

    private Elevador buscarElevadorMaisProximo(Lista elevadores, int andarOrigem) {
        Ponteiro p = elevadores.getInicio();
        Elevador escolhido = null;
        int menorDistancia = Integer.MAX_VALUE;

        while (p != null) {
            Elevador elevador = (Elevador) p.getElemento();
            int distancia = Math.abs(elevador.getAndarAtual() - andarOrigem);

            if (distancia < menorDistancia && elevador.getCapacidadeDisponivel() > 0) {
                menorDistancia = distancia;
                escolhido = elevador;
            }

            p = p.getProximo();
        }

        return escolhido;
    }
}
