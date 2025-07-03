package br.com.fatec.dao;

import br.com.fatec.model.Alunos;
import br.com.fatec.conexao.Banco;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class AlunosDAO implements DAO<Alunos> {

    @Override
    public boolean inserir(Alunos model) throws SQLException { 
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            
            String sql = "INSERT INTO alunos (nome, cpf, telefone, email, nascimento) VALUES (?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(sql);

            ps.setString(1, model.getNome());         
            ps.setString(2, model.getCpf());        
            ps.setString(3, model.getTelefone());     
            ps.setString(4, model.getEmail());
            ps.setString(5, model.getNascimento());
            
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
    public boolean remover(Alunos model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "DELETE FROM alunos WHERE ra = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model.getRa());
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
    public boolean alterar(Alunos model) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Banco.obterConexao();
            String sql = "UPDATE alunos SET nome = ?, cpf = ?, telefone = ?, email = ?, nascimento = ? WHERE ra = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, model.getNome());
            ps.setString(2, model.getCpf());
            ps.setString(3, model.getTelefone());
            ps.setString(4, model.getEmail());
            ps.setString(5, model.getNascimento());
            ps.setInt(6, model.getRa());
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
    public Alunos buscarID(Alunos model) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT * FROM alunos WHERE ra = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, model.getRa());
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Alunos(
                    rs.getInt("ra"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("nascimento")
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
    public Collection<Alunos> lista(String criterio) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Banco.obterConexao();
            String sql = "SELECT * FROM alunos";
            if (criterio != null && !criterio.isEmpty()) {
                sql += " WHERE nome LIKE ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + criterio + "%");
            } else {
                ps = conn.prepareStatement(sql);
            }
            rs = ps.executeQuery();
            Collection<Alunos> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new Alunos(
                    rs.getInt("ra"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("email"),
                    rs.getString("nascimento")
                ));
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            Banco.desconectar();
        }
    }

    public Alunos buscarPorCPF(String cpfNumerico) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM alunos WHERE REPLACE(REPLACE(cpf, '.', ''), '-', '') = ?";
        
        try {
            conn = Banco.obterConexao();
            ps = conn.prepareStatement(sql);
            ps.setString(1, cpfNumerico); 
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return new Alunos(
                        rs.getInt("ra"),
                        rs.getString("nome"),
                        rs.getString("cpf"), 
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("nascimento")
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
}
