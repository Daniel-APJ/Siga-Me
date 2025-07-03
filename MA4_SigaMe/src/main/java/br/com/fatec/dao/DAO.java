package br.com.fatec.dao;

import java.sql.SQLException;
import java.util.Collection;
import javafx.print.Collation;

public interface DAO <T> {
    public boolean inserir(T model) throws SQLException;
    public boolean remover(T model) throws SQLException;
    public boolean alterar(T model) throws SQLException;
    public T buscarID(T model) throws SQLException;
    public Collection<T> lista(String criterio) throws SQLException;
}
