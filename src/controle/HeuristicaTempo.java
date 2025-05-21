package controle;

import Base.*;
import EstruturaDados.*;

public class HeuristicaTempo extends HeuristicaControle {
    @Override
    public void decidir(Lista elevadores, Lista andares, int minutoAtual) {
        Ponteiro pAndar = andares.getInicio();

        while (pAndar != null) {
            Andar andar = (Andar) pAndar.getElemento();
            Fila fila = andar.getPessoasAguardando();

            if (!fila.estaVazia()) {
                // Conta destinos mais comuns
                Lista destinos = new Lista();
                Lista contagens = new Lista();

                Ponteiro p = fila.getInicio();
                while (p != null) {
                    Pessoa pessoa = (Pessoa) p.getElemento();
                    int destino = pessoa.getAndarDestino();

                    boolean achou = false;
                    Ponteiro d = destinos.getInicio();
                    Ponteiro c = contagens.getInicio();
                    while (d != null && c != null) {
                        if ((int) d.getElemento() == destino) {
                            c.setElemento((int) c.getElemento() + 1);
                            achou = true;
                            break;
                        }
                        d = d.getProximo();
                        c = c.getProximo();
                    }
                    if (!achou) {
                        destinos.inserirFim(destino);
                        contagens.inserirFim(1);
                    }

                    p = p.getProximo();
                }

                // Encontra destino mais popular
                int destinoPopular = -1;
                int maiorContagem = 0;
                Ponteiro d = destinos.getInicio();
                Ponteiro c = contagens.getInicio();
                while (d != null && c != null) {
                    int qtd = (int) c.getElemento();
                    if (qtd > maiorContagem) {
                        maiorContagem = qtd;
                        destinoPopular = (int) d.getElemento();
                    }
                    d = d.getProximo();
                    c = c.getProximo();
                }

                // Atribui passageiros com destino igual/próximo ao mesmo elevador
                Elevador escolhido = buscarElevadorComCapacidade(elevadores, andar.getNumero(), destinoPopular);
                if (escolhido != null
                        && escolhido.getAndarAtual() == andar.getNumero()) {

                    // elevador já no andar → embarca agora
                    andar.embarcarPessoasPorDestino(escolhido,
                            destinoPopular,
                            minutoAtual);

                } else if (escolhido != null) {
                    // apenas registra chamada; embarque acontecerá
                    andar.getPainel().registrarChamada(andar.getNumero());
                }
            }

            pAndar = pAndar.getProximo();
        }
    }

    private Elevador buscarElevadorComCapacidade(Lista elevadores, int andarAtual, int destinoPreferido) {
        Ponteiro p = elevadores.getInicio();
        Elevador escolhido = null;
        int melhorDiferenca = Integer.MAX_VALUE;

        while (p != null) {
            Elevador elevador = (Elevador) p.getElemento();
            if (elevador.getCapacidadeDisponivel() > 0) {
                int diff = Math.abs(destinoPreferido - elevador.getAndarAtual());
                if (diff < melhorDiferenca) {
                    melhorDiferenca = diff;
                    escolhido = elevador;
                }
            }
            p = p.getProximo();
        }

        return escolhido;
    }
}
