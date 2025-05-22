package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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

    @FXML
    private javafx.scene.control.Label labelTempo;
    @FXML
    private RadioButton radioEnergia;
    @FXML
    private RadioButton radioTempo;
    @FXML
    private RadioButton radioSem;
    @FXML
    private TextField inputAndares;
    @FXML
    private TextField inputElevadores;
    @FXML
    private TextField inputPessoas;
    @FXML
    private TextField inputTempo;
    @FXML
    private Button botaoIniciar;
    @FXML
    private GridPane gradePredio;
    @FXML
    private Button botaoPausar;
    @FXML
    private Button botaoRetomar;
    @FXML
    private Button botaoCancelar;
    @FXML
    private StackPane paneGrafico;
    // ← injeta o contêiner
    private BarChart<String, Number> grafico;

    private Simulador simulador;
    private final Map<String, StackPane> elevadorViews = new HashMap<>();

    private enum EstadoSimulacao {NAO_INICIADA, RODANDO, PAUSADA}

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
            mostrarGrafico();
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

    // GRAFICO DO RESUMO DA SIMULAÇAO
    private void mostrarGrafico() {

        /* ───── eixos ───────────────────────────── */
        CategoryAxis eixoX = new CategoryAxis();
        eixoX.setLabel("Métrica");

        NumberAxis eixoY = new NumberAxis();
        eixoY.setLabel("Valor");

        grafico = new BarChart<>(eixoX, eixoY);
        grafico.setTitle("Resumo da Simulação");

        /* ───── cores que vamos usar ────────────── */
        String corTempo = "#2E8B57";   // verde
        String corEnergia = "#FF7F50";   // coral
        String corViagens = "#1E90FF";   // azul

        /* ─── Tempo ─────────────────────────────── */
        int tempo = simulador.getSomaTempoTotal();
        XYChart.Series<String, Number> sTempo = novaSerie(
                "Tempo (" + tempo + " min)", "Tempo (min)", tempo, corTempo);

        /* ─── Energia ───────────────────────────── */
        int energia = simulador.getSomaEnergiaTotal();
        XYChart.Series<String, Number> sEnergia = novaSerie(
                "Energia (" + energia + ")", "Energia", energia, corEnergia);

        /* ─── Viagens ───────────────────────────── */
        int viagens = simulador.getSomaViagensTotal();
        XYChart.Series<String, Number> sViagens = novaSerie(
                "Viagens (" + viagens + ")", "Viagens", viagens, corViagens);

        grafico.getData().addAll(sTempo, sEnergia, sViagens);

        /* coloca o gráfico no painel de destino */
        paneGrafico.getChildren().setAll(grafico);

        /* rótulos no topo da barra (depois que o CSS aplicar) */
        Platform.runLater(() -> addValueLabels(sTempo));
        Platform.runLater(() -> addValueLabels(sEnergia));
        Platform.runLater(() -> addValueLabels(sViagens));

        Platform.runLater(() -> {
            colorirLegenda("Tempo",   corTempo);
            colorirLegenda("Energia", corEnergia);
            colorirLegenda("Viagens", corViagens);
        });
    }

    /**
     * Cria uma série unitária (uma única barra colorida)
     */
    private XYChart.Series<String, Number> novaSerie(
            String nomeSerie, String categoriaX, int valorY, String corHex) {

        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName(nomeSerie);

        XYChart.Data<String, Number> d = new XYChart.Data<>(categoriaX, valorY);
        s.getData().add(d);

        // quando o nó da barra nascer, aplica cor
        d.nodeProperty().addListener((obs, oldNode, newNode) -> {
            if (newNode != null) newNode.setStyle("-fx-bar-fill: " + corHex + ";");
        });
        return s;
    }

    private void addValueLabels(XYChart.Series<String, Number> serie) {
        for (XYChart.Data<String, Number> d : serie.getData()) {
            Text label = new Text(d.getYValue().toString());
            label.setStyle("-fx-font-size: 11; -fx-font-weight: bold;");

            StackPane bar = (StackPane) d.getNode();
            bar.getChildren().add(label);             // coloca texto dentro da barra

            /* reposiciona texto acima do topo da barra */
            bar.heightProperty().addListener((o, oldH, newH) ->
                    label.setTranslateY(-newH.doubleValue() - 2));
        }
    }

    private void colorirLegenda(String prefixoTexto, String corHex) {
        for (Node n : grafico.lookupAll(".chart-legend-item")) {
            if (n instanceof Label lbl               // item da legenda é Label
                    && lbl.getText().startsWith(prefixoTexto)) {

                Node simbolo = lbl.getGraphic();     // quadradinho colorido
                if (simbolo != null) {
                    ((javafx.scene.Node) simbolo).setStyle("-fx-background-color: " + corHex + ";");
                }
            }
        }
    }


    //FINAL GRAFICO


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
        DoorPane portas = new DoorPane(40, 40);          // ← agora é DoorPane
        Text texto = new Text("");
        StackPane cel = new StackPane(portas, texto);

        String chave = e.hashCode() + "-" + andar;
        elevadorViews.put(chave, cel);

        return cel;
    }

    private void iniciarLoopVisual() {
        new Thread(() -> {
            while (true) {
                Platform.runLater(this::atualizarVisual);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    private void atualizarVisual() {
        int totalAndares = simulador.getPredio().getAndares().tamanho();
        int minutosSimulados = simulador.getMinutoAtual();
        int horas = minutosSimulados / 60;
        int minutos = minutosSimulados % 60;

        Ponteiro p = simulador.getPredio()
                .getCentral()
                .getElevadores()
                .getInicio();

        while (p != null) {
            Elevador elevador = (Elevador) p.getElemento();
            int andarAtual = elevador.getAndarAtual();
            int capacidade = elevador.getQuantidadePassageiros();
            boolean subindo = elevador.isSubindo();
            boolean abrindo = elevador.getPausaRestante() > 0;   // ← portas abertas?

            for (int andar = 0; andar < totalAndares; andar++) {
                String chave = elevador.hashCode() + "-" + andar;
                StackPane celula = elevadorViews.get(chave);
                if (celula == null) continue;

                DoorPane portas = (DoorPane) celula.getChildren().get(0); // índice 0
                Text texto = (Text) celula.getChildren().get(1); // índice 1

                if (andar == andarAtual) {                 // cabine aqui
                    if (abrindo) portas.open();            // desembarcando
                    else portas.close();

                    portas.setOpacity(1);                  // destaque (GOLD)
                    portas.setStyle("-fx-fill: gold;");    // cor base (via CSS)

                    String dir = subindo ? "↑" : "↓";
                    texto.setText("P: " + capacidade + " " + dir);
                } else {                                   // andares vazios
                    portas.close();
                    portas.setOpacity(0.6);                // tom mais claro
                    portas.setStyle("-fx-fill: lightgray;");
                    texto.setText("");
                }
            }
            p = p.getProximo();
        }
        labelTempo.setText(String.format("%02d:%02d:%02d", horas, minutos, 0));
    }
}
