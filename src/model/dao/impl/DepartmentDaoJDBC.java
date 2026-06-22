package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDAO;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDAO {

	// variavel para conexao do bd
	private Connection conn;

	// faz a depencia com o bd
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	// inserir dados
	@Override
	public void insert(Department obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?) ",
					java.sql.Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());

			int rows = st.executeUpdate();

			if (rows > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
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
	public void update(Department obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE department " + "SET Name = ? " + "WHERE id = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM department WHERE id = ?");

			st.setInt(1, id);

			int rows = st.executeUpdate();

			if (rows == 0) {
				throw new DbException("Id not exists");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			// testa se veio resultado
			if (rs.next()) {
				return instantiateDepartment(rs);
			} else {
				System.out.println("Departamento não encontrado!");
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResutlSet(rs);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT Id, Name FROM department ORDER BY Name ASC");
			rs = st.executeQuery();

			// criando lista de retorno
			List<Department> list = new ArrayList<>();

			while (rs.next()) {
				Department dep = instantiateDepartment(rs);
				dep = instantiateDepartment(rs);
				list.add(dep);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResutlSet(rs);
		}
	}

}
