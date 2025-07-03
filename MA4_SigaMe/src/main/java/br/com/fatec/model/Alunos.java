package br.com.fatec.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Alunos {
    private int ra;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String nascimento;

    public Alunos() {}

    public Alunos(int ra, String nome, String cpf, String telefone, String email, String nascimento) {
        this.ra = ra;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.nascimento = nascimento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.ra;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Alunos other = (Alunos) obj;
        return this.ra == other.ra;
    }

    @Override
    public String toString() {
        return ra + " - " + nome; 
    }
 
    public int getRa() {
        return ra;
    }

    public void setRa(int ra) {
        this.ra = ra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    } 

    public String getCpfFormatado() {
        if (this.cpf == null) return "";
        String numeros = this.cpf.replaceAll("[^0-9]", ""); 
        if (numeros.length() != 11) {
            return this.cpf; 
        }
        return String.format("%s.%s.%s-%s", 
                numeros.substring(0, 3), 
                numeros.substring(3, 6), 
                numeros.substring(6, 9), 
                numeros.substring(9, 11));
    }

    public String getTelefoneFormatado() {
        if (this.telefone == null) return "";
        String numeros = this.telefone.replaceAll("[^0-9]", "");
        if (numeros.length() == 11) {
            return String.format("(%s) %s-%s", numeros.substring(0, 2), numeros.substring(2, 7), numeros.substring(7));
        } else if (numeros.length() == 10) {
            return String.format("(%s) %s-%s", numeros.substring(0, 2), numeros.substring(2, 6), numeros.substring(6));
        }
        return this.telefone; 
    }

    public String getNascimentoFormatado() {
        DateTimeFormatter formatoDoBanco = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoDeTela = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (this.nascimento == null || this.nascimento.isEmpty()) {
            return ""; 
        }

        try {
            LocalDate data = LocalDate.parse(this.nascimento, formatoDoBanco);
            return data.format(formatoDeTela);
        } catch (DateTimeParseException e) {
            return this.nascimento;
        }
    }
}
