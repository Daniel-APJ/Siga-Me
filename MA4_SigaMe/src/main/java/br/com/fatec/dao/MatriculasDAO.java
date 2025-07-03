package br.com.fatec.dao;

import br.com.fatec.model.Matriculas;
import br.com.fatec.model.Alunos;
import br.com.fatec.model.Cursos;
import br.com.fatec.conexao.Banco;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class MatriculasDAO implements DAO<Matriculas> {
    private final AlunosDAO alunosDAO = new AlunosDAO();
    private final CursosDAO  cursosDAO  = new CursosDAO();

    @Override
    public boolean inserir(Matriculas model) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "INSERT INTO matriculas (ra, curso, semestre, data_matricula, status) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model.getRa().getRa());
            ps.setString(2, model.getCurso().getId_curso());
            ps.setInt(3, model.getSemestre());
            ps.setString(4, model.getData_matricula());
            ps.setInt(5, model.getStatus());
            int rows = ps.executeUpdate();
            return rows >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            Banco.desconectar();
        }
    }

    @Override
    public boolean alterar(Matriculas model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "UPDATE matriculas SET ra = ?, id_curso = ?, semestre = ?, data_matricula = ?, status = ? WHERE id_matr = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model.getRa().getRa());
            ps.setString(2, model.getCurso().getId_curso());
            ps.setInt(3, model.getSemestre());
            ps.setString(4, model.getData_matricula());
            ps.setInt(5, model.getStatus());
            ps.setInt(6, model.getId_matr());
            int rows = ps.executeUpdate();
            return rows >= 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            Banco.desconectar();
        }
    }

    @Override
    public Matriculas buscarID(Matriculas model) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT * FROM matriculas WHERE id_matr = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model.getId_matr());
            rs = ps.executeQuery();
            if (rs.next()) {
                Alunos a = alunosDAO.buscarID(new Alunos(rs.getInt("ra"), null, null, null, null, null));
                Cursos c = cursosDAO.buscarID(new Cursos(rs.getString("id_curso"), null));
                Matriculas m = new Matriculas(a);
                m.setId_matr(rs.getInt("id_matr"));
                m.setCurso(c);
                m.setSemestre(rs.getInt("semestre"));
                m.setData_matricula(rs.getString("data_matricula"));
                m.setStatus(rs.getInt("status"));
                return m;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            Banco.desconectar();
        }
    }

    @Override
    public Collection<Matriculas> lista(String criterio) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection<Matriculas> list = new ArrayList<>();

        String sql = "SELECT m.id_matr, m.semestre, m.status, m.data_matricula, " +
                     "a.ra, a.nome, a.cpf, a.telefone, a.email, a.nascimento, " +
                     "c.id_curso, c.nome_curso " +
                     "FROM Matriculas m " +
                     "JOIN Alunos a ON m.ra = a.ra " +
                     "JOIN Cursos c ON m.curso = c.id_curso ";

        try {
            conn = Banco.obterConexao();

            if (criterio != null && !criterio.isEmpty()) {
                sql += " WHERE a.nome LIKE ?"; 
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + criterio + "%");
            } else {
                ps = conn.prepareStatement(sql);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                Alunos aluno = new Alunos(rs.getInt("ra"), rs.getString("nome"), rs.getString("cpf"),
                                          rs.getString("telefone"), rs.getString("email"), rs.getString("nascimento"));

                Cursos curso = new Cursos(rs.getString("id_curso"), rs.getString("nome_curso"));

                Matriculas matricula = new Matriculas(aluno); 
                matricula.setId_matr(rs.getInt("id_matr"));
                matricula.setCurso(curso);
                matricula.setSemestre(rs.getInt("semestre"));

                matricula.setData_matricula(rs.getDate("data_matricula").toString());
                matricula.setStatus(rs.getInt("status"));

                list.add(matricula);
            }
            return list;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            Banco.desconectar();
        }
    }
    
    @Override
    public boolean remover(Matriculas model) throws SQLException {
        Connection conn = null;
        try {
            conn = Banco.obterConexao();
            conn.setAutoCommit(false); 

            String sqlJunction = "DELETE FROM matricula_has_materias WHERE id_matricula_fk = ?";
            try (PreparedStatement psJunction = conn.prepareStatement(sqlJunction)) {
                psJunction.setInt(1, model.getId_matr());
                psJunction.executeUpdate();
            }

            String sqlMain = "DELETE FROM matriculas WHERE id_matr = ?";
            try (PreparedStatement psMain = conn.prepareStatement(sqlMain)) {
                psMain.setInt(1, model.getId_matr());
                int rows = psMain.executeUpdate();

                conn.commit(); 
                return rows >= 1;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Erro ao tentar rollback: " + ex.getMessage());
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                } catch (SQLException ex) {
                }
            }
            Banco.desconectar();
        }
    }

    public boolean existeMatriculaAtiva(int ra, String idCurso, int semestre) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT id_matr FROM matriculas WHERE ra = ? AND curso = ? AND semestre = ? AND status = 1";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ra);
            ps.setString(2, idCurso);
            ps.setInt(3, semestre);
            rs = ps.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            Banco.desconectar();
        }
    }
}

