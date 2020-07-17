package caixa_simtr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dadosdeteste.AbstractCarga;

public class AbstractCargaSIMTR extends AbstractCarga{
	
	private Connection getConexao() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
			return DriverManager.getConnection(
					"jdbc:postgresql://go7875sx018.goiania.caixa:5432/postst01", "p737158", "xyz1234");
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}

	}

	protected long executarSQL(String sql) throws Throwable {
		return this.executarSQL(sql, true);
	}
	
	protected long consultaSQL(String sql) throws Throwable {
		return this.executarSQL(sql, false);
	}

	protected long executarSQL(String sql, boolean retornarIdGerado)
			throws Throwable {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet res = null;
		long result = 0;

		synchronized (AbstractCargaSIMTR.class) {
			try {
				conn = getConexao();

				if (retornarIdGerado) {
					ps = conn.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
				} else {
					ps = conn.prepareStatement(sql);
				}
				if ((sql.toUpperCase().indexOf("INSERT") > -1)
						|| (sql.toUpperCase().indexOf("SELECT") > -1)) {
					result = coletaInformacaoDB(ps, retornarIdGerado);
				} else {
					ps.executeUpdate();
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage() + "\r\n" + sql);
				throw new Throwable(e);
			} finally {
				try {
					if (res != null) {
						res.close();
					}
					if (ps != null) {
						ps.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return result;
	}

	private long coletaInformacaoDB(PreparedStatement ps,
			boolean retornarIdGerado) throws SQLException {
		long resultado = 0;
		ResultSet res = null;
		try {
			ps.execute();
			if (retornarIdGerado) {
				res = ps.getGeneratedKeys();
			} else {
				res = ps.getResultSet();
			}
			if (res.next()) {
				resultado = res.getLong(1);
			}
		} finally {
			if (res != null) {
				res.close();
			}
		}
		return resultado;
	}

	@Override
	public void carregar() throws Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void descarregar() throws Throwable {
		// TODO Auto-generated method stub
		
	}

}

