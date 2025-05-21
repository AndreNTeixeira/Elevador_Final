package Base;

import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;
import Metricas.MetricasElevador;
import Simulacao.EntidadeSimulavel;

public class Elevador extends EntidadeSimulavel {
    private int id;
    private int andarAtual = 0;
    private int capacidadeMaxima = 8;
    private int andarMaximo;
    private boolean subindo = true;
    private Lista passageiros = new Lista();

    // pausa nos andares
    private int pausaRestante = 0;          // ticks (minutos simulados) que ainda deve ficar parado
    private static final int TEMPO_POR_PASSAGEIRO = 1;
    private int passageirosParaDesembarcar = 0;   // faltam sair deste andar?
    private boolean emDesembarque = false;        // elevador já anunciou a parada?// cada passageiro = 1 minuto (ajuste ao gosto)

    //adicionando metricas
    private MetricasElevador metricas;
    private long tempoUltimaAtualizacao;
    private boolean estavaParado;

    public Elevador(int id, int andarMaximo) {
        this.id = id;
        this.andarMaximo = andarMaximo;
        // adicionando metricas
        this.metricas = new MetricasElevador();
        this.tempoUltimaAtualizacao = 0;
        this.estavaParado = true;
        System.out.println("Elevador " + id + " criado. Andar máximo: " + andarMaximo);
    }

    @Override
    public void atualizar(int minutoSimulado) {
        int tempoDecorrido = minutoSimulado - (int) tempoUltimaAtualizacao;

        if (getQuantidadePassageiros() == 0 && andarAtual == 0) {
            if (!estavaParado) {
                estavaParado = true;
            }
            metricas.adicionarTempoParado(tempoDecorrido);
        } else {
            if (estavaParado) {
                estavaParado = false;
                metricas.incrementarViagens();
            }
            int energiaGasta = calcularEnergiaGasta(tempoDecorrido);
            metricas.adicionarEnergiaGasta(energiaGasta);
            metricas.adicionarTempoMovimentacao(tempoDecorrido);
        }
// ─── 1) ainda parado? ─────────────────────────────
        if (pausaRestante > 0) {
            // libera UM passageiro por ciclo
            desembarcarUmPassageiro(minutoSimulado);
            pausaRestante--;                     // 1 min passou
            metricas.adicionarTempoParado(1);
            return;                              // não anda enquanto houver pausa
        }

// ─── 2) acabou a pausa ou não havia pausa ─────────
        if (!emDesembarque) {
            passageirosParaDesembarcar = contarPassageirosDoAndarAtual();
            if (passageirosParaDesembarcar > 0) {
                pausaRestante      = passageirosParaDesembarcar; // 1 min por passageiro
                emDesembarque      = true;
                System.out.printf(
                        "Elevador %d está aguardando %d passageiro(s) desembarcar(em) no andar %d%n",
                        id, passageirosParaDesembarcar, andarAtual
                );
                metricas.adicionarTempoParado(1); // já conta o 1º minuto
                return;                           // anunciamos – nada mais neste ciclo
            }
        }
        emDesembarque = false;
        tempoUltimaAtualizacao = minutoSimulado;
        removerPassageiros(minutoSimulado);
        mover();
    }

    private void desembarcarUmPassageiro(int minutoAtual) {
        Ponteiro atual = passageiros.getInicio();
        Ponteiro anterior = null;

        while (atual != null) {
            Pessoa p = (Pessoa) atual.getElemento();
            if (p.getAndarDestino() == andarAtual) {
                // remove o PRIMEIRO que encontrar e volta
                p.sairElevador();
                p.registrarTempoSaidaElevador(minutoAtual);
                System.out.printf("Pessoa %d saiu no andar %d do Elevador %d%n",
                        p.getId(), andarAtual, id);

                // remoção na lista
                if (anterior == null) {
                    passageiros.setInicio(atual.getProximo());
                } else {
                    anterior.setProximo(atual.getProximo());
                }
                if (atual.getProximo() == null) {
                    passageiros.setFim(anterior);
                }
                return;            // só 1 passageiro por ciclo
            }
            anterior = atual;
            atual = atual.getProximo();
        }
    }
    private int contarPassageirosDoAndarAtual() {
        int n = 0;
        for (Ponteiro p = passageiros.getInicio(); p != null; p = p.getProximo()) {
            if (((Pessoa) p.getElemento()).getAndarDestino() == andarAtual) n++;
        }
        return n;
    }


    private int calcularEnergiaGasta(int tempoDecorrido) {
        int consumoBase = 1;
        int consumoPorPassageiro = 1;
        int quantidadePassageiros = getQuantidadePassageiros();

        return tempoDecorrido * (consumoBase + (consumoPorPassageiro * quantidadePassageiros));
    }

    public void adicionarPassageiro(Pessoa pessoa, int minutoAtual) {
        if (getQuantidadePassageiros() < capacidadeMaxima && !pessoa.estaDentroDoElevador()) {
            pessoa.entrarElevador();
            pessoa.registrarTempoSaidaFila(minutoAtual);
            pessoa.registrarTempoEntradaElevador(minutoAtual);
            passageiros.inserirFim(pessoa);
            metricas.incrementarPessoasTransportadas();
            System.out.println("Pessoa " + pessoa.getId() + " entrou no Elevador " + id);
        }
    }

    public MetricasElevador getMetricas() {
        return metricas;
    }

    // novo aqui
    private void removerPassageiros(int minutoAtual) {
        int desembarques = 0;

        Ponteiro atual = passageiros.getInicio();
        Ponteiro anterior = null;

        while (atual != null) {
            Pessoa p = (Pessoa) atual.getElemento();
            Ponteiro proximo = atual.getProximo();

            if (p.getAndarDestino() == andarAtual) {
                p.sairElevador();
                p.registrarTempoSaidaElevador(minutoAtual);
                System.out.println("Pessoa " + p.getId() + " saiu no andar " + andarAtual + " do Elevador " + id);

                if (anterior == null) {
                    passageiros.setInicio(proximo);
                } else {
                    anterior.setProximo(proximo);
                }

                if (proximo == null) {
                    passageiros.setFim(anterior);
                }
                desembarques++;
            } else {
                anterior = atual;
            }
            atual = proximo;
        }
        if (desembarques > 0) {
            pausaRestante = desembarques * TEMPO_POR_PASSAGEIRO;
            System.out.println("Elevador " + id + " está aguardando "
                    + pausaRestante + " passageiro(s) " + " desembarcar(em) no andar " + andarAtual);
        }
    }

    private void mover() {

    /* ────────────────────────────────────────────────
       1) ELEVADOR VAZIO → deve retornar ao térreo
       ────────────────────────────────────────────────*/
        if (getQuantidadePassageiros() == 0) {

            // Se ainda não está no térreo, desce um andar
            if (andarAtual > 0) {
                subindo = false;
                andarAtual--;
            }

            // Chegou vazio ao térreo: reseta direção para cima e sai
            if (andarAtual == 0) {
                subindo = true;
                System.out.println("Elevador " + id +
                        " chegou vazio ao térreo. Pronto para nova viagem.");
                return;   // evita imprimir log “Descendo” parado no 0
            }

    /* ────────────────────────────────────────────────
       2) ELEVADOR COM PASSAGEIROS
       ────────────────────────────────────────────────*/
        } else {

            // Inverte direção nos extremos
            if (subindo && andarAtual == andarMaximo) {
                subindo = false;
            } else if (!subindo && andarAtual == 0) {
                subindo = true;
            }

            // Move um andar conforme a direção
            if (subindo) {
                andarAtual++;
            } else {
                andarAtual--;
            }
        }

    /* ────────────────────────────────────────────────
       Log
       ────────────────────────────────────────────────*/
        System.out.println("Elevador " + id + " está no andar " + andarAtual +
                " com " + getQuantidadePassageiros() + " passageiros. " +
                (subindo ? "Subindo ↑" : "Descendo ↓"));
    }

    public int getAndarAtual() {
        return andarAtual;
    }

    public void setAndarAtual(int andar) {
        this.andarAtual = andar;
    }

    public boolean isSubindo() {
        return subindo;
    }

    public void setSubindo(boolean subindo) {
        this.subindo = subindo;
    }

    public int getQuantidadePassageiros() {
        int count = 0;
        Ponteiro p = passageiros.getInicio();
        while (p != null) {
            count++;
            p = p.getProximo();
        }
        return count;
    }

    public int getCapacidadeDisponivel() {
        return capacidadeMaxima - getQuantidadePassageiros();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lista getPassageiros() {
        return passageiros;
    }
}
