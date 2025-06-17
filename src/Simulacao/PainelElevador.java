package Simulacao;

import EstruturaDados.Lista; // Importa estrutura de dados personalizada (lista encadeada)

import java.io.Serializable;

public class PainelElevador implements Serializable { // Classe que representa o painel interno do elevador
    private Lista destinosSolicitados = new Lista(); // Lista de destinos solicitados pelos passageiros

    public void registrarChamada(int destino) { // Adiciona um novo destino solicitado ao painel
        destinosSolicitados.inserirFim(destino); // Insere o destino no final da lista
    }

    public void limparChamadas() { // Limpa todas as chamadas registradas no painel
        destinosSolicitados = new Lista(); // Cria uma nova lista, descartando a anterior
    }

    public Lista getDestinosSolicitados() { // Retorna a lista de destinos solicitados
        return destinosSolicitados;
    }
}
