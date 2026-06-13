package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDAO {

	// insere no banco
	void insert(Seller obj);

	// atualiza informacao
	void update(Seller obj);

	// deleta no bd
	void deleteById(Seller obj);

	// busca por id
	Seller findById(Integer id);

	// busca na lista os sellers
	List<Seller> findAll();

}
