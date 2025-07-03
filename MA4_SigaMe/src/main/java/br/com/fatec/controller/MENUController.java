package br.com.fatec.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle; 

public class MENUController implements Initializable {

    @FXML
    private Button cadAluno_btn;
    @FXML
    private Button matrAluno_btn;
    @FXML
    private Button genMatr_btn;
    @FXML
    private Button genCad_btn;
    @FXML
    private Button close_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    private void abrirNovaJanela(String fxmlPath, String title, ActionEvent event) {
        final double[] xOffset = {0};
        final double[] yOffset = {0};

        try {
            Stage menuStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/" + fxmlPath));
            Parent root = loader.load(); 

            Stage newStage = new Stage();

            newStage.initStyle(StageStyle.UNDECORATED);

            newStage.setResizable(false);

            root.setOnMousePressed(mouseEvent -> {
                xOffset[0] = mouseEvent.getSceneX();
                yOffset[0] = mouseEvent.getSceneY();
            });

            root.setOnMouseDragged(mouseEvent -> {
                newStage.setX(mouseEvent.getScreenX() - xOffset[0]);
                newStage.setY(mouseEvent.getScreenY() - yOffset[0]);
            });

            newStage.setTitle(title); 
            newStage.setScene(new Scene(root));

            newStage.show();

            if (menuStage != null) {
                menuStage.setIconified(true);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro ao Abrir Janela");
            alert.setHeaderText("Não foi possível carregar a tela solicitada.");
            alert.setContentText("Detalhes do erro: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void abrirCadastroAluno(ActionEvent event) {
        abrirNovaJanela("Cad_Aluno.fxml", "Cadastro de Aluno", event);
    }

    @FXML
    private void abrirMatriculaAluno(ActionEvent event) {
        abrirNovaJanela("Matr_Aluno.fxml", "Matrícula de Aluno", event);
    }

    @FXML
    private void abrirGerarMatricula(ActionEvent event) {
        abrirNovaJanela("Ger_Matricula.fxml", "Gerar Relatório de Matrículas", event);
    }

    @FXML
    private void abrirGerarCadastro(ActionEvent event) {
        abrirNovaJanela("Ger_Cadastro.fxml", "Gerar Relatório de Cadastros", event);
    }

    @FXML
    private void fecharTela(ActionEvent event) { 
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Saída");
        alert.setHeaderText("Encerrar Programa");
        alert.setContentText("Você realmente deseja encerrar o programa?");

        ButtonType buttonTypeSim = new ButtonType("Sim");
        ButtonType buttonTypeNao = new ButtonType("Não");

        alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeSim){
            Platform.exit(); 
            System.exit(0); 
        }
    }
}