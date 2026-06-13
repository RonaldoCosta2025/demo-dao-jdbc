package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

// essa classe é a implementação do JDBC do sellerDao
public class SellerDaoJDBC implements SellerDAO {

	// essa variavel fica disponivel em qualquer lugar nesta classe
	private Connection conn;

	// faz a dependenci com o bd
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {

	}

	@Override
	public void update(Seller obj) {

	}

	@Override
	public void deleteById(Seller obj) {

	}

	@Override
	public Seller findById(Integer id) {
		// faz a consulta sql
		PreparedStatement st = null;
		ResultSet rs = null;

		// METODO PARA RETORNAR VENDEDOR POR ID
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			// configura o ?
			st.setInt(1, id);
			// recebe a execucao do sql
			rs = st.executeQuery();
			// testa se veio resultado
			if (rs.next()) {
				// instancia o departamento e configurando os campos
				Department dep = new Department();
				dep.setId(rs.getInt("DepartmentId"));
				dep.setName(rs.getString("DepName"));

				// instancia o departamento e configurando os campos, apontando para
				// departamento
				Seller obj = new Seller();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				obj.setEmail(rs.getString("Email"));
				obj.setBaseSalary(rs.getDouble("BaseSalary"));
				obj.setBirthDate(rs.getDate("BirthDate"));
				// pega o objeto inteiro
				obj.setDepartment(dep);
				return obj;

			}
			// se falso
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResutlSet(rs);
		}

	}

	@Override
	public List<Seller> findAll() {
		return null;
	}

}
