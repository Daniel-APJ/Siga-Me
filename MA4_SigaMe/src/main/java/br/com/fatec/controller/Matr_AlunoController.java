package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.AlunosDAO;
import br.com.fatec.dao.CursosDAO;
import br.com.fatec.dao.MateriasDAO;
import br.com.fatec.dao.MatriculasDAO;
import br.com.fatec.model.Alunos;
import br.com.fatec.model.Cursos;
import br.com.fatec.model.Materias;
import br.com.fatec.model.Matriculas;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Matr_AlunoController implements Initializable {

    @FXML private Button finalizar_btn;
    @FXML private ComboBox<Integer> semestre_cmb;
    @FXML private ComboBox<Cursos> curso_cmb;
    @FXML private ComboBox<Alunos> ra_cmb;
    @FXML private TableView<Materias> materias_tabv;
    @FXML private Button close_btn;

    @FXML private TableColumn<Materias, String> colNomeMateria;
    @FXML private TableColumn<Materias, Integer> colSemestreMateria;

    private final AlunosDAO alunosDAO = new AlunosDAO();
    private final CursosDAO cursosDAO = new CursosDAO();
    private final MateriasDAO materiasDAO = new MateriasDAO();
    private final MatriculasDAO matriculasDAO = new MatriculasDAO();

    private final ObservableList<Alunos> listaRAs = FXCollections.observableArrayList();
    private final ObservableList<Cursos> listaCursos = FXCollections.observableArrayList();
    private final ObservableList<Integer> listaSemestres = FXCollections.observableArrayList();
    private final ObservableList<Materias> listaMaterias = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarComboBoxes();
        configurarTableView();
        carregarDadosIniciais();

        curso_cmb.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarSemestresParaCurso(newVal.getId_curso());
                materias_tabv.getItems().clear();
            } else {
                semestre_cmb.getItems().clear();
                materias_tabv.getItems().clear();
            }
        });

        semestre_cmb.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && curso_cmb.getValue() != null) {
                carregarMaterias(curso_cmb.getValue().getId_curso(), newVal);
            } else {
                materias_tabv.getItems().clear();
            }
        });
    }

    private void configurarComboBoxes() {
        ra_cmb.setItems(listaRAs);
        curso_cmb.setItems(listaCursos);
        semestre_cmb.setItems(listaSemestres);
        ra_cmb.setConverter(new StringConverter<Alunos>() {
            @Override public String toString(Alunos aluno) { return aluno == null ? null : aluno.toString(); }
            @Override public Alunos fromString(String string) { return null; }
        });
        curso_cmb.setConverter(new StringConverter<Cursos>() {
            @Override public String toString(Cursos curso) { return curso == null ? null : curso.toString(); }
            @Override public Cursos fromString(String string) { return null; }
        });
    }

    private void configurarTableView() {
        colNomeMateria.setCellValueFactory(new PropertyValueFactory<>("nome_materia")); 
        colSemestreMateria.setCellValueFactory(new PropertyValueFactory<>("semestre"));     
        materias_tabv.setItems(listaMaterias);
    }

    private void carregarDadosIniciais() {
        carregarRAs();
        carregarCursos();
    }

    private void carregarRAs() {
        listaRAs.clear();
        listaRAs.addAll(alunosDAO.lista(null));
    }

    private void carregarCursos() {
        listaCursos.clear();
        listaCursos.addAll(cursosDAO.lista(null));
    }

    private void carregarSemestresParaCurso(String idCurso) {
        listaSemestres.clear();
        semestre_cmb.setValue(null);
        try {
            listaSemestres.addAll(materiasDAO.listarSemestresPorCurso(idCurso));
        } catch (Exception e) {
            mostrarAlertaErro("Erro ao carregar Semestres", e.getMessage());
        }
    }

    private void carregarMaterias(String idCurso, int semestreNumero) {
        listaMaterias.clear();
        try {
            Collection<Materias> materiasDoBanco = materiasDAO.listarPorCursoESemestre(idCurso, semestreNumero);
            listaMaterias.addAll(materiasDoBanco);
        } catch (Exception e) {
            mostrarAlertaErro("Erro ao carregar Matérias", e.getMessage());
        }
    }

    @FXML
    private void finalizarMatricula(ActionEvent event) {
        Alunos alunoSelecionado = ra_cmb.getValue();
        Cursos cursoSelecionado = curso_cmb.getValue();
        Integer semestreSelecionado = semestre_cmb.getValue();

        if (alunoSelecionado == null || cursoSelecionado == null || semestreSelecionado == null) {
            mostrarAlertaAviso("Campos Incompletos", "Selecione RA, Curso e Semestre.");
            return;
        }
        if (materias_tabv.getItems().isEmpty()) {
            mostrarAlertaAviso("Sem Matérias", "Não há matérias para matricular neste semestre/curso.");
            return;
        }

        try {
            if (matriculasDAO.existeMatriculaAtiva(alunoSelecionado.getRa(), cursoSelecionado.getId_curso(), semestreSelecionado)) {
                mostrarAlertaAviso("Matrícula Existente", "Este aluno já possui uma matrícula ativa para o curso e semestre selecionados.");
                return;
            }

            Matriculas novaMatricula = new Matriculas(alunoSelecionado);
            novaMatricula.setCurso(cursoSelecionado);
            novaMatricula.setSemestre(semestreSelecionado);
            novaMatricula.setData_matricula(LocalDate.now().toString());
            novaMatricula.setStatus(1); // 1 = Ativa

            if (matriculasDAO.inserir(novaMatricula)) { 
                mostrarAlertaInfo("Sucesso", "Matrícula realizada com sucesso!");
                limparSelecoes();
            } else {
                mostrarAlertaErro("Erro ao Matricular", "Não foi possível concluir a matrícula.");
            }

        } catch (Exception e) {
            mostrarAlertaErro("Erro ao Finalizar Matrícula", "Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        App.mostrarMenuPrincipal();
    }

    private void mostrarAlertaErro(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Erro");
        a.setHeaderText(t);
        a.setContentText(m);
        a.showAndWait();
    }

    private void mostrarAlertaAviso(String t, String m) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Aviso");
        a.setHeaderText(t);
        a.setContentText(m);
        a.showAndWait();
    }

    private void mostrarAlertaInfo(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Informação");
        a.setHeaderText(t);
        a.setContentText(m);
        a.showAndWait();
    }

    private void limparSelecoes() {
        ra_cmb.getSelectionModel().clearSelection();
        curso_cmb.getSelectionModel().clearSelection();
        semestre_cmb.getItems().clear();
        semestre_cmb.setValue(null);
        materias_tabv.getItems().clear();
    }

    public void carregarDadosParaEdicao(Matriculas matricula) {
    }
}