package controle;

import Base.*;
import EstruturaDados.*;

public class HeuristicaTempo extends HeuristicaControle {                            // Estratégia que otimiza o tempo agrupando destinos semelhantes
    @Override
    public void decidir(Lista elevadores, Lista andares, int minutoAtual) {          // Método chamado a cada minuto para tomar decisões de controle
        Ponteiro pAndar = andares.getInicio();                                       // Itera sobre todos os andares

        while (pAndar != null) {
            Andar andar = (Andar) pAndar.getElemento();                              // Recupera o andar atual
            Fila fila = andar.getPessoasAguardando();                                // Pega a fila de pessoas aguardando no andar

            if (!fila.estaVazia()) {                                                 // Se houver pessoas esperando

                // Contabiliza os destinos mais comuns entre os passageiros
                Lista destinos = new Lista();                                         // Lista de destinos únicos
                Lista contagens = new Lista();                                        // Contador de pessoas por destino

                Ponteiro p = fila.getInicio();
                while (p != null) {
                    Pessoa pessoa = (Pessoa) p.getElemento();                         // Pessoa atual
                    int destino = pessoa.getAndarDestino();                           // Destino da pessoa

                    boolean achou = false;
                    Ponteiro d = destinos.getInicio();                                // Ponteiro da lista de destinos
                    Ponteiro c = contagens.getInicio();                               // Ponteiro da lista de contagens

                    while (d != null && c != null) {                                  // Procura se destino já foi registrado
                        if ((int) d.getElemento() == destino) {
                            c.setElemento((int) c.getElemento() + 1);                 // Incrementa contador se já existente
                            achou = true;
                            break;
                        }
                        d = d.getProximo();
                        c = c.getProximo();
                    }

                    if (!achou) {
                        destinos.inserirFim(destino);                                 // Adiciona novo destino
                        contagens.inserirFim(1);                                      // Inicia contagem em 1
                    }

                    p = p.getProximo();
                }

                // Identifica o destino mais frequente
                int destinoPopular = -1;
                int maiorContagem = 0;
                Ponteiro d = destinos.getInicio();
                Ponteiro c = contagens.getInicio();

                while (d != null && c != null) {
                    int qtd = (int) c.getElemento();
                    if (qtd > maiorContagem) {
                        maiorContagem = qtd;
                        destinoPopular = (int) d.getElemento();                       // Salva o destino mais frequente
                    }
                    d = d.getProximo();
                    c = c.getProximo();
                }

                // Procura um elevador disponível para atender esse grupo
                Elevador escolhido = buscarElevadorComCapacidade(
                        elevadores, andar.getNumero(), destinoPopular
                );

                if (escolhido != null && escolhido.getAndarAtual() == andar.getNumero()) {
                    // Se o elevador já estiver no andar, embarca os passageiros agora
                    andar.embarcarPessoasPorDestino(escolhido, destinoPopular, minutoAtual);

                } else if (escolhido != null) {
                    // Caso contrário, registra a chamada no painel do andar
                    andar.getPainel().registrarChamada(andar.getNumero());
                }
            }

            pAndar = pAndar.getProximo();                                             // Passa para o próximo andar
        }
    }

    private Elevador buscarElevadorComCapacidade(Lista elevadores, int andarAtual, int destinoPreferido) {
        Ponteiro p = elevadores.getInicio();                                          // Iterador de elevadores
        Elevador escolhido = null;
        int melhorDiferenca = Integer.MAX_VALUE;

        while (p != null) {
            Elevador elevador = (Elevador) p.getElemento();                           // Elevador atual

            if (elevador.getCapacidadeDisponivel() > 0) {                             // Só considera elevadores com espaço disponível
                int diff = Math.abs(destinoPreferido - elevador.getAndarAtual());     // Calcula a distância até o destino desejado

                if (diff < melhorDiferenca) {                                         // Escolhe o mais próximo
                    melhorDiferenca = diff;
                    escolhido = elevador;
                }
            }

            p = p.getProximo();                                                       // Vai para o próximo elevador
        }

        return escolhido;                                                             // Retorna o melhor elevador encontrado
    }
}
