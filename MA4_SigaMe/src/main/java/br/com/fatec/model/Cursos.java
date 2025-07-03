package br.com.fatec.model;

import java.util.Objects;

public class Cursos {
    private String id_curso;
    private String nome_curso;

    public Cursos() {}
    
    public Cursos(String idCurso, String nomeCurso) {
        id_curso = idCurso;
        nome_curso = nomeCurso;
    }

    public String getId_curso() {
        return id_curso;
    }

    public void setId_curso(String id_curso) {
        this.id_curso = id_curso;
    }

    public String getNome_curso() {
        return nome_curso;
    }

    public void setNome_curso(String nome_curso) {
        this.nome_curso = nome_curso;
    }

    @Override
    public String toString() {
        return getNome_curso();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id_curso);
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
        final Cursos other = (Cursos) obj;
        return Objects.equals(this.id_curso, other.id_curso);
    }
}