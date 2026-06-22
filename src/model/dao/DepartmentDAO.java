package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDAO {

	// insere no banco
	void insert(Department obj);

	// atualiza informacao
	void update(Department obj);

	// deleta no bd
	void deleteById(Integer id);

	// busca por id
	Department findById(Integer id);

	// busca na lista os departamanetos
	List<Department> findAll();

}
