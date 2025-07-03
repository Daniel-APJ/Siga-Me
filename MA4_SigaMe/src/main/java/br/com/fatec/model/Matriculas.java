package br.com.fatec.model;

public class Matriculas {
    private int id_matr; 
    private Alunos ra;  
    private Cursos curso;  
    private int semestre;
    private String data_matricula;
    private int status;
    
    public Matriculas() {
    }

    public Matriculas(Alunos ra) {
        this.ra = ra;
    }

    public Matriculas(Cursos curso) {
        this.curso = curso;
    }

    public Matriculas(int id_matr, Alunos ra, Cursos curso, int semestre, String data_matricula, int status) {
        this.id_matr = id_matr;
        this.ra = ra;
        this.curso = curso;
        this.semestre = semestre;
        this.data_matricula = data_matricula;
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + this.id_matr;
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
        final Matriculas other = (Matriculas) obj;
        return this.id_matr == other.id_matr;
    }
    
    public int getId_matr() {
        return id_matr;
    }

    public void setId_matr(int id_matr) {
        this.id_matr = id_matr;
    }

    public Alunos getRa() {
        return ra;
    }

    public void setRa(Alunos ra) {
        this.ra = ra;
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

    public String getData_matricula() {
        return data_matricula;
    }

    public void setData_matricula(String data_matricula) {
        this.data_matricula = data_matricula;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public int getAlunoId() {
        return this.ra != null ? this.ra.getRa() : 0;
    }

    public String getCursoNome() {
        return this.curso != null ? this.curso.getNome_curso() : "";
    }
    
    public String getStatusDisplay() {
        return this.status == 1 ? "ATIVO" : "INATIVO";
    }
}
