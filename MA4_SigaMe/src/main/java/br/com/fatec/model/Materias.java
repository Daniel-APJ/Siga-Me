package br.com.fatec.model;

import java.util.Objects;

public class Materias {
    private String id_materia;
    private String nome_materia;
    private Cursos curso; 
    private int semestre;

    public Materias(Cursos curso) {
        this.curso = curso;
    }

    public Materias(String id_materia, String nome_materia, Cursos curso, int semestre) {
        this.id_materia = id_materia;
        this.nome_materia = nome_materia;
        this.curso = curso;
        this.semestre = semestre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id_materia);
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
        final Materias other = (Materias) obj;
        return Objects.equals(this.id_materia, other.id_materia);
    }

    @Override
    public String toString() {
        return "Materias{" + "nome_materia=" + nome_materia + ", semestre=" + semestre + '}';
    }
    
    public String getId_materia() {
        return id_materia;
    }

    public void setId_materia(String id_materia) {
        this.id_materia = id_materia;
    }

    public String getNome_materia() {
        return nome_materia;
    }

    public void setNome_materia(String nome_materia) {
        this.nome_materia = nome_materia;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
    
    
}
