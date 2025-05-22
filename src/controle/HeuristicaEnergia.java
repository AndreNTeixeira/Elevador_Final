package controle;

import Base.Andar;
import Base.Elevador;
import Base.Pessoa;
import EstruturaDados.Fila;
import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;

public class HeuristicaEnergia extends HeuristicaControle {                              // Heurística que busca economizar energia chamando o elevador mais próximo
    @Override
    public void decidir(Lista elevadores, Lista andares, int minutoAtual) {              // Método principal de decisão, chamado a cada minuto de simulação
        Ponteiro pAndar = andares.getInicio();                                           // Itera sobre todos os andares

        while (pAndar != null) {
            Andar andar = (Andar) pAndar.getElemento();                                  // Recupera o andar atual
            Fila fila = andar.getPessoasAguardando();                                    // Pega a fila de pessoas aguardando

            if (!fila.estaVazia()) {                                                     // Se há pessoas esperando
                Pessoa primeiraPessoa = (Pessoa) fila.getInicio().getElemento();         // Pega a primeira pessoa da fila
                Elevador elevadorMaisProximo = buscarElevadorMaisProximo(                // Busca o elevador mais próximo com espaço
                        elevadores, andar.getNumero());

                if (elevadorMaisProximo != null &&                                       // Se o elevador está no mesmo andar
                        elevadorMaisProximo.getAndarAtual() == andar.getNumero()) {
                    andar.embarcarPessoasPorDestino(                                     // Embarca passageiros com mesmo destino
                            elevadorMaisProximo,
                            primeiraPessoa.getAndarDestino(),
                            minutoAtual);
                } else if (elevadorMaisProximo != null) {
                    andar.getPainel().registrarChamada(andar.getNumero());              // Apenas registra a chamada no painel do andar
                }
            }

            pAndar = pAndar.getProximo();                                                // Vai para o próximo andar
        }
    }

    private Elevador buscarElevadorMaisProximo(Lista elevadores, int andarOrigem) {     // Busca o elevador mais próximo com espaço disponível
        Ponteiro p = elevadores.getInicio();
        Elevador escolhido = null;
        int menorDistancia = Integer.MAX_VALUE;

        while (p != null) {
            Elevador elevador = (Elevador) p.getElemento();                              // Recupera o elevador atual
            int distancia = Math.abs(elevador.getAndarAtual() - andarOrigem);            // Calcula a distância até o andar de origem

            if (distancia < menorDistancia &&                                            // Se for o mais próximo até agora
                    elevador.getCapacidadeDisponivel() > 0) {                            // E tiver espaço disponível
                menorDistancia = distancia;
                escolhido = elevador;
            }

            p = p.getProximo();                                                          // Vai para o próximo elevador
        }

        return escolhido;                                                                // Retorna o elevador mais próximo com capacidade
    }
}
