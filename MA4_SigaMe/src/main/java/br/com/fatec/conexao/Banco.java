package br.com.fatec.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Banco {

    // Atributos de conexão
    private static final String BANCO_DADOS = "siga_me";
    private static final String USUARIO = "root";
    private static final String SENHA = "";
    private static final String SERVIDOR = "localhost";
    private static final int PORTA = 3306;

    // Variável de conexão
    private static Connection conexao = null;

    // Construtor privado para evitar instâncias da classe
    private Banco() {
        // Construtor vazio ou com lógica de inicialização, se necessário
    }

    /**
     * Retorna uma instância da conexão com o banco de dados.
     * Se a conexão não estiver aberta ou estiver fechada, ela será restabelecida.
     * @return Objeto Connection com a conexão ativa.
     * @throws SQLException Se ocorrer um erro ao conectar ao banco de dados.
     */
    public static Connection obterConexao() throws SQLException {
        // Verifica se a conexão é nula ou se está fechada
        if (conexao == null || conexao.isClosed()) {
            String url = "jdbc:mariadb://" + SERVIDOR +
                         ":" + PORTA + "/" + BANCO_DADOS;
            conexao = DriverManager.getConnection(url, USUARIO, SENHA);
        }
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de dados, se ela estiver aberta.
     */
    public static void desconectar() {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null; // Garante que a conexão seja recriada na próxima chamada
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}