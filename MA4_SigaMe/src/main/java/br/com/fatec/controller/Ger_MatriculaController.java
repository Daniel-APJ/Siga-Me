package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.MatriculasDAO;
import br.com.fatec.model.Matriculas;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Ger_MatriculaController implements Initializable {

    @FXML private TableView<Matriculas> cadastro_tabv; 
    @FXML private Button editar_btn;
    @FXML private Button excluir_btn;
    @FXML private Button close_btn;

    @FXML private TableColumn<Matriculas, Integer> colRa;
    @FXML private TableColumn<Matriculas, String> colCurso;
    @FXML private TableColumn<Matriculas, Integer> colSemestre;
    @FXML private TableColumn<Matriculas, String> colStatus;

    private final ObservableList<Matriculas> listaMatriculas = FXCollections.observableArrayList();
    private final MatriculasDAO matriculasDAO = new MatriculasDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTableView();
        carregarMatriculasDoBanco();
    }

    private void configurarTableView() {
        colRa.setCellValueFactory(new PropertyValueFactory<>("alunoId"));
        colCurso.setCellValueFactory(new PropertyValueFactory<>("cursoNome"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusDisplay"));
        
        cadastro_tabv.setItems(listaMatriculas);
    }

    private void carregarMatriculasDoBanco() {
        listaMatriculas.clear();
        
        try {
            Collection<Matriculas> matriculasDoBanco = matriculasDAO.lista(null);
            listaMatriculas.addAll(matriculasDoBanco);
            
        } catch (SQLException e) {
            mostrarAlerta("Erro de Banco de Dados", "Não foi possível carregar as matrículas: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void editarMatricula(ActionEvent event) {
        Matriculas selecionada = cadastro_tabv.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Nenhuma Matrícula Selecionada", "Por favor, selecione uma matrícula para editar.", AlertType.WARNING);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/Matr_Aluno.fxml"));
            Parent root = loader.load();
            Matr_AlunoController controller = loader.getController();
 
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            final double[] xOffset = {0}, yOffset = {0};
            root.setOnMousePressed(mouseEvent -> { xOffset[0] = mouseEvent.getSceneX(); yOffset[0] = mouseEvent.getSceneY(); });
            root.setOnMouseDragged(mouseEvent -> { stage.setX(mouseEvent.getScreenX() - xOffset[0]); stage.setY(mouseEvent.getScreenY() - yOffset[0]); });
            stage.showAndWait();
            carregarMatriculasDoBanco();
        } catch (IOException e) {
            mostrarAlerta("Erro ao Abrir Tela", "Não foi possível carregar a tela de matrícula: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void excluirMatricula(ActionEvent event) {
        Matriculas selecionada = cadastro_tabv.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            mostrarAlerta("Nenhuma Matrícula Selecionada", "Por favor, selecione uma matrícula para excluir.", AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Excluir Matrícula");
        alert.setContentText(String.format("Deseja realmente excluir a matrícula do RA %d para o curso '%s'?", selecionada.getAlunoId(), selecionada.getCursoNome()));

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (matriculasDAO.remover(selecionada)) {
                    mostrarAlerta("Sucesso", "Matrícula excluída com sucesso!", AlertType.INFORMATION);
                    listaMatriculas.remove(selecionada);
                } else {
                    mostrarAlerta("Erro", "Nenhuma matrícula foi excluída.", AlertType.ERROR);
                }
            } catch (SQLException e) {
                mostrarAlerta("Erro de Banco de Dados", "Não foi possível excluir a matrícula: " + e.getMessage(), AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        App.mostrarMenuPrincipal();
    }
    
    private void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}