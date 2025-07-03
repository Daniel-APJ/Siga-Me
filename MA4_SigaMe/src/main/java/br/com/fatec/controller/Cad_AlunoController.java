package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.AlunosDAO;
import br.com.fatec.model.Alunos;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Cad_AlunoController implements Initializable {

    @FXML private Button close_btn;
    @FXML private TextField nome_txt;
    @FXML private TextField cpf_txt;
    @FXML private TextField tel_txt;
    @FXML private TextField email_txt;
    @FXML private TextField dataNasc_txt;
    @FXML private Button finalizar_btn;
    
    private final AlunosDAO alunosDAO = new AlunosDAO();
    
    private String textoOriginalFinalizarBtn = "Finalizar Cadastro";
    private static final DateTimeFormatter DATE_FORMATTER_VIEW = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private boolean cpfJaCadastrado = false;
    private int raDoAlunoEmEdicao = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textoOriginalFinalizarBtn = finalizar_btn.getText();
        aplicarMascaraCPF(cpf_txt);
        aplicarMascaraTelefone(tel_txt);
        aplicarMascaraData(dataNasc_txt);

        cpf_txt.textProperty().addListener((observable, oldValue, newValue) -> {
            String cpfNumerico = newValue.replaceAll("[^0-9]", "");
            if (cpfNumerico.length() == 11) {
                Platform.runLater(() -> verificarCpfExistenteEPreencherCampos(cpfNumerico));
            } else if (newValue.trim().isEmpty() || cpfNumerico.length() < 11) {
                if (cpfJaCadastrado) {
                    limparCamposAposMudancaCpf();
                    cpfJaCadastrado = false;
                    finalizar_btn.setText(textoOriginalFinalizarBtn);
                }
            }
        });
    }

    private boolean verificarCpfExistenteEPreencherCampos(String cpfNumerico) {
        if (cpfNumerico == null || cpfNumerico.length() != 11) {
            if (cpfJaCadastrado) {
                 limparCamposAposMudancaCpf();
            }
            cpfJaCadastrado = false;
            finalizar_btn.setText(textoOriginalFinalizarBtn);
            return false;
        }

        Alunos alunoEncontrado = alunosDAO.buscarPorCPF(cpfNumerico);

        if (alunoEncontrado != null) {
            nome_txt.setText(alunoEncontrado.getNome());
            tel_txt.setText(formatarTelefoneParaExibicao(alunoEncontrado.getTelefone()));
            email_txt.setText(alunoEncontrado.getEmail());
            if (alunoEncontrado.getNascimento() != null && !alunoEncontrado.getNascimento().isEmpty()) {
                try {
                    LocalDate dataDb = LocalDate.parse(alunoEncontrado.getNascimento());
                    dataNasc_txt.setText(dataDb.format(DATE_FORMATTER_VIEW));
                } catch (DateTimeParseException e) {
                    dataNasc_txt.setText(alunoEncontrado.getNascimento());
                }
            } else {
                dataNasc_txt.clear();
            }

            raDoAlunoEmEdicao = alunoEncontrado.getRa();
            cpfJaCadastrado = true;
            finalizar_btn.setText("Atualizar Cadastro");
            mostrarAlerta("CPF Encontrado", "Os dados do aluno foram carregados para edição.", AlertType.INFORMATION);
            return true;
        } else { 
            if (cpfJaCadastrado) {
                limparCamposAposMudancaCpf();
            }
            cpfJaCadastrado = false;
            finalizar_btn.setText(textoOriginalFinalizarBtn);
            raDoAlunoEmEdicao = -1;
            return false;
        }
    }
    
    @FXML
    private void finalizarCadastro(ActionEvent event) {
        String cpfInput = cpf_txt.getText().replaceAll("[^0-9]", "");
        if (cpfInput.isEmpty()) {
            exibirErros(List.of("O campo 'CPF' é obrigatório."));
            return;
        }

        String nome = nome_txt.getText();
        String telefone = tel_txt.getText().replaceAll("[^0-9]", "");
        String email = email_txt.getText();
        String dataNascStr = dataNasc_txt.getText();

        List<String> erros = validarCampos(nome, cpfInput, telefone, email, dataNascStr);
        if (!erros.isEmpty()) {
            exibirErros(erros);
            return;
        }

        try {
            LocalDate dataNascimento = LocalDate.parse(dataNascStr, DATE_FORMATTER_VIEW);
            String dataNascimentoParaBanco = dataNascimento.toString();

            Alunos aluno = new Alunos(raDoAlunoEmEdicao, nome, cpfInput, telefone, email, dataNascimentoParaBanco);

            boolean sucesso;
            if (cpfJaCadastrado) {
                sucesso = alunosDAO.alterar(aluno);
                if (sucesso) {
                    mostrarAlerta("Sucesso", "Dados do aluno atualizados com sucesso!", AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Erro", "Falha ao atualizar o cadastro do aluno. Verifique o console para mais detalhes.", AlertType.ERROR);
                }
            } else { 
                try {
                    sucesso = alunosDAO.inserir(aluno);
                    if (sucesso) {
                        mostrarAlerta("Sucesso", "Aluno cadastrado com sucesso!", AlertType.INFORMATION);
                        limparCampos();
                    } else {
                        mostrarAlerta("Erro", "Falha ao cadastrar o novo aluno. Verifique o console.", AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    mostrarAlerta("Erro de Banco de Dados", "Não foi possível inserir o aluno. Detalhes: " + e.getMessage(), AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        } catch (DateTimeParseException e) {
            exibirErros(List.of("Formato de Data de Nascimento inválido. Use dd/MM/yyyy."));
        }
    }

    private void limparCamposAposMudancaCpf() {
        nome_txt.clear();
        tel_txt.clear();
        email_txt.clear();
        dataNasc_txt.clear();
        raDoAlunoEmEdicao = -1;
    }

    @FXML
    private void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        App.mostrarMenuPrincipal();
    }

    private String formatarTelefoneParaExibicao(String telDb) {
        if (telDb == null) {
            return "";
        }
        String numeros = telDb.replaceAll("[^0-9]", "");
        if (numeros.length() == 11) {
            return String.format("(%s) %s-%s", numeros.substring(0, 2), numeros.substring(2, 7), numeros.substring(7));
        } else if (numeros.length() == 10) {
            return String.format("(%s) %s-%s", numeros.substring(0, 2), numeros.substring(2, 6), numeros.substring(6));
        }
        return telDb;
    }

    private List<String> validarCampos(String nome, String cpf, String telefone, String email, String dataNascStr) {
        List<String> erros = new ArrayList<>();
        
        if (nome == null || nome.trim().isEmpty()) {
            erros.add("O campo 'Nome Completo' é obrigatório.");
        }
        
        if (cpf.replaceAll("[^0-9]", "").length() != 11 && !cpfJaCadastrado) {
            erros.add("CPF deve conter 11 dígitos.");
        }
        
        String telNumeros = telefone.replaceAll("[^0-9]", "");
        if (telNumeros.length() < 10 || telNumeros.length() > 11) {
            erros.add("Telefone parece incompleto.");
        }
        
        if (email == null || email.trim().isEmpty()) {
            erros.add("O campo 'Email' é obrigatório.");
        } else if (!isValidEmail(email)) {
            erros.add("O formato do Email é inválido.");
        }
        
        if (dataNascStr == null || dataNascStr.trim().isEmpty()) {
            erros.add("O campo 'Data de Nascimento' é obrigatório.");
        } else {
            try {
                LocalDate.parse(dataNascStr, DATE_FORMATTER_VIEW);
            } catch (DateTimeParseException e) {
                erros.add("Formato inválido para 'Data de Nascimento'. Use dd/MM/yyyy.");
            }
        }
        return erros;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private void exibirErros(List<String> erros) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro de Validação");
        alert.setHeaderText("Por favor, corrija os seguintes erros:");
        alert.setContentText("- " + String.join("\n- ", erros));
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensagem, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        nome_txt.clear();
        cpf_txt.clear();
        tel_txt.clear();
        email_txt.clear();
        dataNasc_txt.clear();
        cpfJaCadastrado = false;
        raDoAlunoEmEdicao = -1;
        finalizar_btn.setText(textoOriginalFinalizarBtn);
        cpf_txt.requestFocus();
    }

    private void aplicarMascaraCPF(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String novoTextoNumerico = change.getControlNewText().replaceAll("[^0-9]", "");
            if (novoTextoNumerico.length() > 11) {
                return null;
            }
            
            StringBuilder textoFormatado = new StringBuilder();
            int i = 0;
            for (char c : novoTextoNumerico.toCharArray()) {
                if (i == 3 || i == 6) {
                    textoFormatado.append('.');
                } else if (i == 9) {
                    textoFormatado.append('-');
                }
                textoFormatado.append(c);
                i++;
            }
            
            change.setText(textoFormatado.toString());
            change.setRange(0, change.getControlText().length());
            change.selectRange(textoFormatado.length(), textoFormatado.length());
            return change;
        };
        
        textField.setTextFormatter(new TextFormatter<>(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }, "", filter));
    }

    private void aplicarMascaraTelefone(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            int len = newText.length();
            if (len > 11) {
                return null;
            }
            
            StringBuilder formatted = new StringBuilder();
            if (len > 0) formatted.append("(");
            if (len > 0) formatted.append(newText.substring(0, Math.min(len, 2)));
            if (len > 2) formatted.append(") ");
            
            if (len > 2) {
                String mainNumber = newText.substring(2);
                if (mainNumber.length() <= 4) {
                    formatted.append(mainNumber);
                } else if (mainNumber.length() <= 8) {
                    formatted.append(mainNumber.substring(0, 4)).append("-").append(mainNumber.substring(4));
                } else {
                    formatted.append(mainNumber.substring(0, 5)).append("-").append(mainNumber.substring(5));
                }
            }
            
            change.setText(formatted.toString());
            change.setRange(0, change.getControlText().length());
            change.selectRange(formatted.length(), formatted.length());
            return change;
        };
        
        textField.setTextFormatter(new TextFormatter<>(new StringConverter<String>() {
            @Override
            public String toString(String s) {
                return s;
            }

            @Override
            public String fromString(String s) {
                return s;
            }
        }, "", filter));
    }

    private void aplicarMascaraData(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText().replaceAll("[^0-9]", "");
            int len = newText.length();
            if (len > 8) {
                return null;
            }
            
            StringBuilder formatted = new StringBuilder();
            if (len > 0) formatted.append(newText.substring(0, Math.min(len, 2)));
            if (len > 2) formatted.append("/").append(newText.substring(2, Math.min(len, 4)));
            if (len > 4) formatted.append("/").append(newText.substring(4, Math.min(len, 8)));
            
            change.setText(formatted.toString());
            change.setRange(0, change.getControlText().length());
            change.selectRange(formatted.length(), formatted.length());
            return change;
        };
        
        textField.setTextFormatter(new TextFormatter<>(new StringConverter<String>() {
            @Override
            public String toString(String s) {
                return s;
            }

            @Override
            public String fromString(String s) {
                return s;
            }
        }, "", filter));
    }

    public void carregarDadosParaEdicao(Alunos aluno) {
        cpf_txt.setText(aluno.getCpf());
        nome_txt.setText(aluno.getNome());
        tel_txt.setText(aluno.getTelefone());
        email_txt.setText(aluno.getEmail());
        
        if (aluno.getNascimento() != null && !aluno.getNascimento().isEmpty()) {
            try {
                LocalDate dataDb = LocalDate.parse(aluno.getNascimento()); 
                dataNasc_txt.setText(dataDb.format(DATE_FORMATTER_VIEW));
            } catch (DateTimeParseException e) {
                dataNasc_txt.setText(aluno.getNascimento()); 
            }
        } else {
            dataNasc_txt.clear();
        }
        
        cpf_txt.setDisable(true);
        
        verificarCpfExistenteEPreencherCampos(aluno.getCpf().replaceAll("[^0-9]", ""));
    }
}