package Base;

import EstruturaDados.Lista;
import EstruturaDados.Ponteiro;
import Metricas.MetricasElevador;
import Simulacao.EntidadeSimulavel;
import Simulacao.CentralDeControle;

public class Elevador extends EntidadeSimulavel {                                   // Representa um elevador dentro da simulação
    private int id;                                                                 // Identificador do elevador
    private int andarAtual = 0;                                                     // Andar atual do elevador (começa no térreo)
    private int capacidadeMaxima = 8;                                               // Capacidade máxima de passageiros
    private int andarMaximo;                                                        // Andar mais alto que o elevador pode alcançar
    private boolean subindo = true;                                                 // Direção atual do elevador
    private Lista passageiros = new Lista();
    private CentralDeControle central;// Lista de passageiros dentro do elevador


    /* ─── Pausa de desembarque ────────────────────── */
    private int pausaRestante = 0;                                                  // Tempo restante de pausa para desembarque
    private static final int TEMPO_POR_PASSAGEIRO = 1;                              // Tempo de desembarque por passageiro
    private int passageirosParaDesembarcar = 0;                                     // Quantidade de passageiros para desembarcar
    private boolean emDesembarque = false;                                          // Se está atualmente desembarcando passageiros

    /* ─── Dwell time no térreo ─────────────────────── */
    private boolean aguardandoPartidaDoTerreo = false;                              // Tempo de espera antes de sair do térreo

    /* ─── Métricas ─────────────────────────────────── */
    private MetricasElevador metricas;                                              // Objeto de métricas do elevador
    private long tempoUltimaAtualizacao;                                            // Último tempo registrado de atualização
    private boolean estavaParado;                                                   // Flag para saber se o elevador estava parado
    private boolean emViagem;                                                       // Flag que indica se está em uma nova viagem

    public Elevador(int id, int andarMaximo) {
        this.id = id;
        this.andarMaximo = andarMaximo;
        this.metricas = new MetricasElevador();                                     // Inicializa métricas
        this.tempoUltimaAtualizacao = 0;
        this.estavaParado = true;
        System.out.println("Elevador " + id + " criado. Andar máximo: " + andarMaximo);
    }

    @Override
    public void atualizar(int minutoSimulado) {
        // Dwell após embarque no térreo
        if (aguardandoPartidaDoTerreo) {
            System.out.printf("Elevador %d está no andar 0 com %d passageiros. Fechando portas…%n",
                    id, getQuantidadePassageiros());
            metricas.adicionarTempoParado(1);
            aguardandoPartidaDoTerreo = false;
            tempoUltimaAtualizacao = minutoSimulado;
            return;
        }

        // Tratamento de pausa para desembarque
        if (pausaRestante > 0) {
            desembarcarUmPassageiro(minutoSimulado);                                // Desembarque um passageiro
            pausaRestante--;
            metricas.adicionarTempoParado(1);
            return;
        }

        // Verifica se há novos passageiros para desembarcar
        if (!emDesembarque) {
            passageirosParaDesembarcar = contarPassageirosDoAndarAtual();
            if (passageirosParaDesembarcar > 0) {
                pausaRestante = passageirosParaDesembarcar;
                emDesembarque = true;
                System.out.printf("Elevador %d está aguardando %d passageiro(s) desembarcar(em) no andar %d%n",
                        id, passageirosParaDesembarcar, andarAtual);
                metricas.adicionarTempoParado(1);
                return;
            }
        }
        emDesembarque = false;

        // Atualiza métricas
        int tempoDecorrido = minutoSimulado - (int) tempoUltimaAtualizacao;
        if (getQuantidadePassageiros() == 0 && andarAtual == 0) {
            metricas.adicionarTempoParado(tempoDecorrido);                          // Parado vazio no térreo
            estavaParado = true;
        } else {
            if (estavaParado) {
                metricas.incrementarViagens();                                      // Começo de nova viagem
                estavaParado = false;
            }
            metricas.adicionarEnergiaGasta(calcularEnergiaGasta(tempoDecorrido));
            metricas.adicionarTempoMovimentacao(tempoDecorrido);
        }

        tempoUltimaAtualizacao = minutoSimulado;

        mover();                                                                     // Move o elevador
    }

    private void desembarcarUmPassageiro(int minutoAtual) {
        Ponteiro atual = passageiros.getInicio();
        Ponteiro anterior = null;

        while (atual != null) {
            Pessoa p = (Pessoa) atual.getElemento();
            if (p.getAndarDestino() == andarAtual) {
                p.sairElevador();
                p.registrarTempoSaidaElevador(minutoAtual);
                System.out.printf("Pessoa %d saiu no andar %d do Elevador %d%n",
                        p.getId(), andarAtual, id);

                if (central != null) {
                    Andar andar = central.getAndar(andarAtual);
                    if (andar != null) {
                        andar.registrarChegadaDePassageiro(p, minutoAtual);
                    }
                }

                if (anterior == null) {
                    passageiros.setInicio(atual.getProximo());
                } else {
                    anterior.setProximo(atual.getProximo());
                }
                if (atual.getProximo() == null) {
                    passageiros.setFim(anterior);
                }
                return;
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
        return tempoDecorrido * (consumoBase + consumoPorPassageiro * getQuantidadePassageiros());
    }

    public void setCentral(CentralDeControle central) {
        this.central = central;
    }

    public void adicionarPassageiro(Pessoa pessoa, int minutoAtual) {
        if (getQuantidadePassageiros() < capacidadeMaxima && !pessoa.estaDentroDoElevador()) {
            pessoa.entrarElevador();
            pessoa.registrarTempoSaidaFila(minutoAtual);
            pessoa.registrarTempoEntradaElevador(minutoAtual);
            passageiros.inserirFim(pessoa);
            metricas.incrementarPessoasTransportadas();
            System.out.println("Pessoa " + pessoa.getId() + " entrou no Elevador " + id);

            if (andarAtual == 0) {
                aguardandoPartidaDoTerreo = true;
            }
        }
    }

    public MetricasElevador getMetricas() {
        return metricas;
    }

    private void removerPassageiros(int minutoAtual) {
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

            } else {
                anterior = atual;
            }

            atual = proximo;
        }
    }

    private void mover() {
        if (getQuantidadePassageiros() == 0) {
            if (andarAtual > 0) {
                subindo = false;
                andarAtual--;
            }
            if (andarAtual == 0) {
                subindo = true;
                System.out.println("Elevador " + id +
                        " chegou vazio ao térreo. Pronto para nova viagem.");
                return;
            }
        } else {
            if (!emViagem && andarAtual == 0) {
                emViagem = true;
                metricas.incrementarViagens();
            }

            if (subindo && andarAtual == andarMaximo) {
                subindo = false;
            } else if (!subindo && andarAtual == 0) {
                subindo = true;
            }

            if (subindo) {
                andarAtual++;
            } else {
                andarAtual--;
            }
        }

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

    public int getPausaRestante() {
        return pausaRestante;
    }
}
