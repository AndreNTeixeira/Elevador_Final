package Base;

import EstruturaDados.Lista;                               // Importa a estrutura de lista encadeada usada nos andares
import EstruturaDados.Ponteiro;                            // Importa ponteiros usados para percorrer a lista
import Simulacao.CentralDeControle;                        // Importa a classe responsável por coordenar os elevadores
import Simulacao.EntidadeSimulavel;                        // Importa a base para entidades que participam da simulação
import controle.HeuristicaControle;                        // Importa a heurística de controle de elevadores

// Classe que representa o prédio da simulação
public class Predio extends EntidadeSimulavel {
    private CentralDeControle central;                     // Controlador central dos elevadores
    private Lista andares;                                 // Lista de andares do prédio
    private int proximoIdPessoa = 1;                       // Identificador sequencial para novas pessoas (ainda não usado)

    // Construtor do prédio, inicializa andares e a central com a heurística de controle
    public Predio(int quantidadeAndares, int quantidadeElevadores, HeuristicaControle heuristica) {
        andares = new Lista();                             // Cria a lista de andares
        central = new CentralDeControle(quantidadeElevadores, quantidadeAndares, heuristica); // Cria a central

        for (int i = 0; i <= quantidadeAndares; i++) {     // Cria andares do térreo até o último
            andares.inserirFim(new Andar(i));              // Insere cada andar na lista
        }

        central.setAndares(andares);                       // Informa os andares à central para ela gerenciar
    }

    // Método chamado a cada minuto da simulação para atualizar o estado do prédio
    @Override
    public void atualizar(int minutoSimulado) {
        Ponteiro p = andares.getInicio();                  // Inicia ponteiro no primeiro andar

        while (p != null) {                                // Percorre todos os andares
            Andar a = (Andar) p.getElemento();             // Converte elemento para Andar
            a.processarRetornoParaTerreo(minutoSimulado); // Processa chamadas de retorno para o térreo

            int totalPresentes = contarPresentes(a);
           // System.out.printf("⏱️ Minuto %d - Andar %d: %d pessoa(s) presentes.%n",
           //         minutoSimulado, a.getNumero(), totalPresentes);
            p = p.getProximo();                            // Vai para o próximo andar
        }

        central.atualizar(minutoSimulado);                 // Atualiza também a lógica da central (elevadores, etc.)
    }

    // Retorna a central de controle
    public CentralDeControle getCentral() {
        return central;
    }

    // Retorna a lista de andares
    public Lista getAndares() {
        return andares;
    }

    private int contarPresentes(Andar andar) {
        int count = 0;
        Ponteiro p = andar.getPessoasPresentes().getInicio();
        while (p != null) {
            count++;
            p = p.getProximo();
        }
        return count;
    }


    // Retorna um andar específico pelo número
    public Andar getAndar(int numero) {
        Ponteiro p = andares.getInicio();                  // Começa pelo início da lista

        while (p != null) {                                // Percorre todos os andares
            Andar a = (Andar) p.getElemento();             // Cast para Andar
            if (a.getNumero() == numero) return a;         // Retorna se o número do andar for o desejado
            p = p.getProximo();                            // Avança para o próximo
        }

        return null;                                       // Retorna null se o andar não for encontrado
    }
}
