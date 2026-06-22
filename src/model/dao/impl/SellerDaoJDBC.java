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

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO  seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					// aqui ja joga o id gerado no objeto
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResutlSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Seller obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE  seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE id = ?");

			// configura os ?, trocando pelo atributo do objeto
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

			// configura o ?
			st.setInt(1, id);

			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected == 0) {
				System.out.println("Id not exists!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

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
		// faz a consulta sql
		PreparedStatement st = null;
		ResultSet rs = null;

		// METODO PARA RETORNAR VENDEDOR POR ID
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + " ORDER BY Name");

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

	@Override
	public List<Seller> findByDepartment(Department department) {
		// faz a consulta sql
		PreparedStatement st = null;
		ResultSet rs = null;

		// METODO PARA RETORNAR VENDEDOR POR ID
		try {
			st = conn.prepareStatement("SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department " + "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId  = ? " + " ORDER BY Name");

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
