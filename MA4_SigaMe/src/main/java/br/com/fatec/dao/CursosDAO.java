package br.com.fatec.dao;

import br.com.fatec.model.Cursos;
import br.com.fatec.conexao.Banco;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class CursosDAO implements DAO<Cursos> {

    @Override
    public boolean inserir(Cursos model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "INSERT INTO cursos (id_curso, nome_curso) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getId_curso());
            ps.setString(2, model.getNome_curso());
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
    public boolean remover(Cursos model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "DELETE FROM cursos WHERE id_curso = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getId_curso());
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
    public boolean alterar(Cursos model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "UPDATE cursos SET nome_curso = ? WHERE id_curso = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getNome_curso());
            ps.setString(2, model.getId_curso());
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
    public Cursos buscarID(Cursos model) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT * FROM cursos WHERE id_curso = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getId_curso());
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Cursos(
                    rs.getString("id_curso"),
                    rs.getString("nome_curso")
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
    public Collection<Cursos> lista(String criterio) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT * FROM cursos";
            if (criterio != null && !criterio.isEmpty()) {
                sql += " WHERE nome_curso LIKE ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + criterio + "%");
            } else {
                ps = conn.prepareStatement(sql);
            }
            rs = ps.executeQuery();
            Collection<Cursos> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Cursos(
                    rs.getString("id_curso"),
                    rs.getString("nome_curso")
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
