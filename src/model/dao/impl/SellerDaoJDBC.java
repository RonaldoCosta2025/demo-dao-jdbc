package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				Department dep = instantiateDepartment(rs);

				// chama o metodo e configura os campos de seller e departamento
				Seller obj = instantiatieSeller(rs, dep);
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

	private Seller instantiatieSeller(ResultSet rs, Department dep) throws SQLException {
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

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		// faz a consulta sql
		PreparedStatement st = null;
		ResultSet rs = null;

		// METODO PARA RETORNAR VENDEDOR POR ID
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId  = ? " + " ORDER BY Name");

			// configura o ?
			st.setInt(1, department.getId());

			// recebe a execucao do sql
			rs = st.executeQuery();

			// criando uma lista, pois, o metodo exige o retorno na lista
			List<Seller> list = new ArrayList<>();

			// cria um a MAP = para buscar e validar se veio somente
			// um id do departamento
			Map<Integer, Department> map = new HashMap<>();

			// testa se veio resultado
			while (rs.next()) {

				// verifica se o departamento existe e coloca no map

				// com isso, pega o departamento com varios vendedores
				// apontando para o mesmo, e nao 1 para 1

				Department dep = map.get(rs.getInt("DepartmentId"));

				// se retornar nulo, instancia o departamento
				if (dep == null) {
					dep = instantiateDepartment(rs);
					// guarda no map
					map.put(rs.getInt("DepartmentId"), dep);
				}

				// chama o metodo e configura os campos de seller e departamento
				Seller obj = instantiatieSeller(rs, dep);
				list.add(obj);

			}
			// retorna a lista
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResutlSet(rs);
		}
	}

}
