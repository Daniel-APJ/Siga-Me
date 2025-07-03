package br.com.fatec.dao;

import br.com.fatec.model.Materias;
import br.com.fatec.model.Cursos;
import br.com.fatec.conexao.Banco;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class MateriasDAO implements DAO<Materias> {
    private final CursosDAO cursosDAO = new CursosDAO();

    @Override
    public boolean inserir(Materias model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "INSERT INTO materias (id_materia, nome_materia, id_curso, semestre) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getId_materia());
            ps.setString(2, model.getNome_materia());
            ps.setString(3, model.getCurso().getId_curso());
            ps.setInt(4, model.getSemestre());
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
    public boolean remover(Materias model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "DELETE FROM materias WHERE id_materia = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getId_materia());
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
    public boolean alterar(Materias model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "UPDATE materias SET nome_materia = ?, id_curso = ?, semestre = ? WHERE id_materia = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getNome_materia());
            ps.setString(2, model.getCurso().getId_curso());
            ps.setInt(3, model.getSemestre());
            ps.setString(4, model.getId_materia());
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
    public Materias buscarID(Materias model) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT * FROM materias WHERE id_materia = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getId_materia());
            rs = ps.executeQuery();
            if (rs.next()) {
                Cursos c = cursosDAO.buscarID(new Cursos(rs.getString("id_curso"), null));
                return new Materias(
                    rs.getString("id_materia"),
                    rs.getString("nome_materia"),
                    c,
                    rs.getInt("semestre")
                );
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
    public Collection<Materias> lista(String criterio) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT * FROM materias";
            if (criterio != null && !criterio.isEmpty()) {
                sql += " WHERE nome_materia LIKE ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + criterio + "%");
            } else {
                ps = conn.prepareStatement(sql);
            }
            rs = ps.executeQuery();
            Collection<Materias> list = new ArrayList<>();
            while (rs.next()) {
                Cursos c = cursosDAO.buscarID(new Cursos(rs.getString("id_curso"), null));
                list.add(new Materias(
                    rs.getString("id_materia"),
                    rs.getString("nome_materia"),
                    c,
                    rs.getInt("semestre")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            Banco.desconectar();
        }
    }

    public Collection<Integer> listarSemestresPorCurso(String idCurso) {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Collection<Integer> semestres = new ArrayList<>();
    try {
        conn = Banco.obterConexao();
        String sql = "SELECT DISTINCT semestre FROM materias WHERE curso = ? ORDER BY semestre";
        ps = conn.prepareStatement(sql);
        ps.setString(1, idCurso);
        rs = ps.executeQuery();
        while (rs.next()) {
            semestres.add(rs.getInt("semestre"));
        }
        return semestres;
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>();
    } finally {
        if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        Banco.desconectar();
    }
}

    public Collection<Materias> listarPorCursoESemestre(String idCurso, int semestreNumero) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Collection<Materias> list = new ArrayList<>();
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT * FROM materias WHERE curso = ? AND semestre = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, idCurso);
            ps.setInt(2, semestreNumero);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Cursos c = cursosDAO.buscarID(new Cursos(rs.getString("curso"), null));
                
                list.add(new Materias(
                    rs.getString("id_materia"),
                    rs.getString("nome_materia"),
                    c, 
                    rs.getInt("semestre")
                ));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            Banco.desconectar();
        }
    }
}
