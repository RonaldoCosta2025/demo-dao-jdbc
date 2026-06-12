package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*
 * AQUI VAI SER CRIADO ALGUNS METODOS ESTATICOS
 * PARA CONECTAR E DESCONECTAR COM O BD
 * 
 * CONECTAR AO BANCO, É INSTANCIAR UM OBJETO DO TIPO CONNECTION
 */

public class DB {

	// variavel para conexao com o bd
	private static Connection conn = null;

	// conexao com o banco
	public static Connection getConnection() {
		if (conn == null) {
			try {
				// pega as propriedades do bd
				Properties props = loadProperties();
				// dados de endereço do bd
				String url = props.getProperty("dburl");
				// obter conexao com o bd
				conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}

	// fecha a conexao
	public static void closConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	// CARREGA AS PROPRIEDADES COM O ACESSO AO BD ARQUIVO
	// bd.properties
	private static Properties loadProperties() {
		// abrindo o arquivo e lendo os dados
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			// instancia o obejto properties
			Properties props = new Properties();
			// faz a leitura do arquivo Properties,
			// apontado pelo arquivo fs
			// guardando os dados dentro de props
			props.load(fs);
			return props;
		} catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}

	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closeResutlSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

}
