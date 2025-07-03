package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.AlunosDAO;
import br.com.fatec.model.Alunos;

import java.io.IOException;
import java.net.URL;
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

public class Ger_CadastroController implements Initializable {

    @FXML private Button close_btn;
    @FXML private TableView<Alunos> cadastro_tabv; 
    @FXML private Button editar_btn;
    @FXML private Button excluir_btn;

    @FXML private TableColumn<Alunos, Integer> colRa;
    @FXML private TableColumn<Alunos, String> colCpf;
    @FXML private TableColumn<Alunos, String> colNome;
    @FXML private TableColumn<Alunos, String> colTelefone;
    @FXML private TableColumn<Alunos, String> colEmail;
    @FXML private TableColumn<Alunos, String> colDataNascimento;
    
    private final ObservableList<Alunos> listaAlunos = FXCollections.observableArrayList();
    private final AlunosDAO alunosDAO = new AlunosDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTableView();
        carregarCadastrosDoBanco();
    }    
    
    private void configurarTableView() {
        colRa.setCellValueFactory(new PropertyValueFactory<>("ra"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpfFormatado"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefoneFormatado"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDataNascimento.setCellValueFactory(new PropertyValueFactory<>("nascimentoFormatado"));
        
        cadastro_tabv.setItems(listaAlunos);
    }
    
    private void carregarCadastrosDoBanco() {
        listaAlunos.clear();
        
        Collection<Alunos> alunosDoBanco = alunosDAO.lista(null);
        listaAlunos.addAll(alunosDoBanco);
    }

    @FXML
    private void editarCadastro(ActionEvent event) {
        Alunos selecionado = cadastro_tabv.getSelectionModel().getSelectedItem();
        
        if (selecionado == null) {
            mostrarAlerta("Nenhum Aluno Selecionado", "Por favor, selecione um aluno na tabela para editar.", AlertType.WARNING);
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/fatec/view/Cad_Aluno.fxml"));
            Parent root = loader.load();
            
            Cad_AlunoController controller = loader.getController();
            controller.carregarDadosParaEdicao(selecionado);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);

            final double[] xOffset = {0}, yOffset = {0};
            root.setOnMousePressed(mouseEvent -> { xOffset[0] = mouseEvent.getSceneX(); yOffset[0] = mouseEvent.getSceneY(); });
            root.setOnMouseDragged(mouseEvent -> { stage.setX(mouseEvent.getScreenX() - xOffset[0]); stage.setY(mouseEvent.getScreenY() - yOffset[0]); });
            
            stage.showAndWait();
            carregarCadastrosDoBanco();
            
        } catch (IOException e) {
            mostrarAlerta("Erro ao Abrir Tela", "Não foi possível carregar a tela de edição: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void excluirCadastro(ActionEvent event) {
        Alunos selecionado = cadastro_tabv.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Nenhum Aluno Selecionado", "Por favor, selecione um aluno para excluir.", AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Excluir Cadastro");
        alert.setContentText(String.format("Deseja realmente excluir o cadastro referente ao RA %d (%s)?", selecionado.getRa(), selecionado.getNome()));
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (alunosDAO.remover(selecionado)) {
                mostrarAlerta("Sucesso", "Cadastro excluído com sucesso!", AlertType.INFORMATION);
                listaAlunos.remove(selecionado);
            } else {
                mostrarAlerta("Erro", "Nenhum cadastro foi excluído.", AlertType.ERROR);
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