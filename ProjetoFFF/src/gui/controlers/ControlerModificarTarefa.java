package gui.controlers;


import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import exceptions.ArgumentoInvalidoException;
import exceptions.ElementoNaoEncontradoException;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import negocio.Fachada;
import negocio.beans.Task;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ControlerModificarTarefa implements Initializable {

    @FXML
    private CheckBox checkbox;

    @FXML
    private TextField label;

    @FXML
    private MaterialIconView star;

    @FXML
    private ContextMenu cm;

    @FXML
    private Button data;

    @FXML
    private BorderPane telamodifica;

    @FXML
    private TextArea conteudo;

    @FXML
    private Label dataCriada;

    @FXML
    private Button favoritar;

    @FXML
    private DatePicker dataEscolhida;

    private Task task;

    public void setTask(Task task){
        this.task = task;
        mudarNome(task.getNome());
        dataCriada.setText("Criado em: " + task.getDataCriada().toString());
        conteudo.setText(task.getConteudo());
        checarStar();
        checarCheckbox();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Se o textfield perder o foco chama registrarNovoValor
        label.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                registrarNovoValor(label);
                System.out.println("TextField perdeu foco");
            }
        });


        //Se der enter atualiza a tarefa
        label.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                registrarNovoValor(label);
            }
        });

        data.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
        data.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double posx = data.localToScreen(data.getBoundsInLocal()).getCenterX();
                double posy = data.localToScreen(data.getBoundsInLocal()).getMaxY();
                cm.show(data, posx, posy);
            }
        });

        Fachada.getInstance().addChangeListener(tasks -> {
            checarStar();
            checarCheckbox();
        });
    }

    @FXML
    void fecha(){
        ControlerPrincipal.getInstance().fecharTela("RIGHT");
    }

    @FXML
    public void setConcluida(){
        if (checkbox.isSelected()) {
            Fachada.getInstance().marcarComoConcluida(task);
            System.out.println("marcou como concluida");
        } else {
            Fachada.getInstance().desmarcarComoConcluida(task);
            System.out.println("desmarcou como concluida");
        }
    }

    @FXML
    public void setFavoritar() {
        if (star.getGlyphName().equals("STAR_BORDER")) {
            gravaStar("STAR");
        } else {
            gravaStar("STAR_BORDER");
        }
    }

    public void mudarNome(String nome) {
        label.setText(nome);
    }

    public void gravaStar(String glyphName) {
        setStar(glyphName);
        if ("STAR".equals(glyphName)) {
            Fachada.getInstance().marcarComoImportante(task);
            System.out.println("marcou como importante");
        }
        else {
            Fachada.getInstance().desmarcarComoImportante(task);
            System.out.println("desmarcou como importante");
        }
    }

    public void setStar(String glyphName) {
        star.setGlyphName(glyphName);
    }

    private void checarStar(){
        if("importante".equals(task.getClassificacao().getPrioridadeDaTask())){
            setStar("STAR");
        }
        else {
            setStar("STAR_BORDER");
        }
    }

    private void checarCheckbox(){
        if("concluida".equals(task.getClassificacao().getStatusDaTask())){
            checkbox.setSelected(true);
        }
        else {
            checkbox.setSelected(false);
        }
    }

    private void registrarNovoValor(TextField textField) {
        String novoValor = textField.getText();
        task.setNome(novoValor);
        try {
            Fachada.getInstance().mudarNome(task, novoValor);
        } catch (ArgumentoInvalidoException e) {
            System.out.println("Argumento invalido");
        } catch (ElementoNaoEncontradoException e) {
            System.out.println("task nao encontrada");
        }
        textField.setText(novoValor);
        label.getParent().requestFocus();
    }

    @FXML
    void dataAmanha() {
        LocalDate amanha = LocalDate.now().plusDays(1);
        Fachada.getInstance().setDataPrevisao(task, amanha);
    }

    @FXML
    void dataEscolher() {
        dataEscolhida.setDisable(false);
        dataEscolhida.setVisible(true);
        dataEscolhida.setOnAction(event -> {
            Fachada.getInstance().setDataPrevisao(task, dataEscolhida.getValue());
        });
    }
    @FXML
    void dataHoje() {
        LocalDate hoje = LocalDate.now();
        Fachada.getInstance().setDataPrevisao(task, hoje);
    }

    @FXML
    void dataProxSem() {
        LocalDate proxSem = LocalDate.now().plusDays(7);
        Fachada.getInstance().setDataPrevisao(task, proxSem);
    }

    @FXML
    void apagar(){
        Fachada.getInstance().remover(task);
        ControlerPrincipal.getInstance().fecharTela("RIGHT");
    }

}

