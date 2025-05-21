package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

import Base.Elevador;
import EstruturaDados.Ponteiro;
import Simulacao.Simulador;
import view.DoorPane;
import javafx.util.Duration;

import controle.HeuristicaControle;
import controle.HeuristicaSemOtimizacao;
import controle.HeuristicaTempo;
import controle.HeuristicaEnergia;


public class TelaInicialController {

    @FXML private javafx.scene.control.Label labelTempo;
    @FXML private RadioButton radioEnergia;
    @FXML private RadioButton radioTempo;
    @FXML private RadioButton radioSem;
    @FXML private TextField inputAndares;
    @FXML private TextField inputElevadores;
    @FXML private TextField inputPessoas;
    @FXML private TextField inputTempo;
    @FXML private Button botaoIniciar;
    @FXML private GridPane gradePredio;
    @FXML private Button botaoPausar;
    @FXML private Button botaoRetomar;
    @FXML private Button botaoCancelar;


    private Simulador simulador;
    private final Map<String, StackPane> elevadorViews = new HashMap<>();
    private enum EstadoSimulacao { NAO_INICIADA, RODANDO, PAUSADA }
    private EstadoSimulacao estadoAtual = EstadoSimulacao.NAO_INICIADA;

    @FXML
    public void aoPausar() {
        if (simulador != null) {
            simulador.pausar();
        }
    }

    @FXML
    public void aoRetomar() {
        if (simulador != null) {
            simulador.continuar();
        }
    }

    @FXML
    public void aoCancelar() {
        if (simulador != null) {
            simulador.encerrar();
        }
        gradePredio.getChildren().clear();
        elevadorViews.clear();
        estadoAtual = EstadoSimulacao.NAO_INICIADA;
        botaoIniciar.setStyle("-fx-background-color: green; -fx-border-radius: 6px;");
        botaoIniciar.setText("Iniciar");
        labelTempo.setText("00:00:00");

        // Limpar os campos de entrada
        inputAndares.clear();
        inputElevadores.clear();
        inputPessoas.clear();
        inputTempo.clear();

        // habilitar os campos
        inputAndares.setDisable(false);
        inputElevadores.setDisable(false);
        inputPessoas.setDisable(false);
        inputTempo.setDisable(false);

    }


    @FXML
    public void inicializarSimulador() {
        // Verifica campos vazios
        if (inputAndares.getText().isEmpty() ||
                inputElevadores.getText().isEmpty() ||
                inputPessoas.getText().isEmpty() ||
                inputTempo.getText().isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos obrigatórios");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, preencha todos os campos antes de iniciar.");
            alerta.showAndWait();
            return;
        }

        //selecionar heuristicas
        HeuristicaControle heuristica;
        if (radioTempo.isSelected()) {
            heuristica = new HeuristicaTempo();
        } else if (radioEnergia.isSelected()) {
            heuristica = new HeuristicaEnergia();
        } else {
            heuristica = new HeuristicaSemOtimizacao();
        }

        // Verifica se os campos são números válidos
        try {
            Integer.parseInt(inputAndares.getText());
            Integer.parseInt(inputElevadores.getText());
            Integer.parseInt(inputPessoas.getText());
            Integer.parseInt(inputTempo.getText());
        } catch (NumberFormatException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro de Formato");
            alerta.setHeaderText(null);
            alerta.setContentText("Todos os campos devem conter apenas números inteiros.");
            alerta.showAndWait();
            return;
        }

        // Desativa campos após validação
        inputAndares.setDisable(true);
        inputElevadores.setDisable(true);
        inputPessoas.setDisable(true);
        inputTempo.setDisable(true);

        // continua com seu switch normalmente...
        switch (estadoAtual) {
            case NAO_INICIADA -> {
                try {
                    int andares = Integer.parseInt(inputAndares.getText());
                    int elevadores = Integer.parseInt(inputElevadores.getText());
                    int pessoas = Integer.parseInt(inputPessoas.getText());
                    int tempo = Integer.parseInt(inputTempo.getText());

                    simulador = new Simulador();
                    simulador.configurar(andares, elevadores, pessoas, tempo, heuristica);

                    construirPredioVisual(andares, elevadores);
                    iniciarLoopVisual();
                    simulador.iniciarSimulacao();

                    botaoIniciar.setStyle("-fx-background-color: blue; -fx-border-radius: 6px;");
                    botaoIniciar.setText("Pausar");
                    estadoAtual = EstadoSimulacao.RODANDO;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            case RODANDO -> {
                simulador.pausar();
                botaoIniciar.setText("Retomar");
                botaoIniciar.setStyle("-fx-background-color: green; -fx-border-radius: 6px;");
                estadoAtual = EstadoSimulacao.PAUSADA;
            }

            case PAUSADA -> {
                simulador.continuar();
                botaoIniciar.setStyle("-fx-background-color: blue; -fx-border-radius: 6px;");
                botaoIniciar.setText("Pausar");
                estadoAtual = EstadoSimulacao.RODANDO;
            }
        }
    }

    public void initialize() {
        ToggleGroup grupo = new ToggleGroup();
        radioEnergia.setToggleGroup(grupo);
        radioTempo.setToggleGroup(grupo);
        radioSem.setToggleGroup(grupo);
    }

    private void construirPredioVisual(int andares, int elevadores) {
        gradePredio.getChildren().clear();
        gradePredio.getColumnConstraints().clear();
        gradePredio.getRowConstraints().clear();
        elevadorViews.clear();

        // Define tamanho fixo das células
        int larguraCelula = 40;
        int alturaCelula = 40;

        // Impede o GridPane de esticar além das preferências
        gradePredio.setMaxWidth(Region.USE_PREF_SIZE);
        gradePredio.setMaxHeight(Region.USE_PREF_SIZE);

        for (int i = 0; i < elevadores; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(larguraCelula);
            col.setMinWidth(larguraCelula);
            col.setMaxWidth(larguraCelula);
            gradePredio.getColumnConstraints().add(col);
        }

        for (int j = 0; j <= andares; j++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(alturaCelula);
            row.setMinHeight(alturaCelula);
            row.setMaxHeight(alturaCelula);
            gradePredio.getRowConstraints().add(row);
        }

        for (int col = 0; col < elevadores; col++) {
            Elevador elevador = (Elevador) simulador.getPredio()
                    .getCentral()
                    .getElevadores()
                    .getElementoNaPosicao(col);

            for (int row = 0; row <= andares; row++) {
                int andarInvertido = andares - row;
                StackPane celula = criarCelulaElevador(elevador, andarInvertido);
                gradePredio.add(celula, col, row);
            }
        }
    }

    private StackPane criarCelulaElevador(Elevador e, int andar) {
        Rectangle fundo = new Rectangle(40, 40);
        fundo.setFill(Color.LIGHTGRAY);
        fundo.setStroke(Color.DARKGRAY);

        Text texto = new Text("");
        StackPane celula = new StackPane(fundo, texto);

        String chave = e.hashCode() + "-" + andar;
        elevadorViews.put(chave, celula);

        return celula;
    }

    private void iniciarLoopVisual() {
        new Thread(() -> {
            while (true) {
                Platform.runLater(this::atualizarVisual);
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void atualizarVisual() {
        int totalAndares = simulador.getPredio().getAndares().tamanho();
        int minutosSimulados = simulador.getMinutoAtual();
        int horas = minutosSimulados / 60;
        int minutos = minutosSimulados % 60;


        Ponteiro p = simulador.getPredio().getCentral().getElevadores().getInicio();
        while (p != null) {
            Elevador elevador = (Elevador) p.getElemento();
            int andarAtual = elevador.getAndarAtual();
            int capacidade = elevador.getQuantidadePassageiros();
            boolean subindo = elevador.isSubindo();

            for (int andar = 0; andar < totalAndares; andar++) {
                String chave = elevador.hashCode() + "-" + andar;
                StackPane celula = elevadorViews.get(chave);
                if (celula == null) continue;

                Rectangle fundo = (Rectangle) celula.getChildren().get(0);
                Text texto = (Text) celula.getChildren().get(1);

                if (andar == andarAtual) {
                    fundo.setFill(Color.GOLD);
                    String dir = subindo ? "↑" : "↓";
                    texto.setText("P: " + capacidade + " " + dir);
                } else {
                    fundo.setFill(Color.LIGHTGRAY);
                    texto.setText("");
                }
            }

            p = p.getProximo();
        }
        labelTempo.setText(String.format("%02d:%02d:%02d", horas, minutos, 0));
    }
}
