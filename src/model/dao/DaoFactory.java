package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

// FABRICA = classe auxiliar responsavel por instanciar os DAOS
public class DaoFactory {

	/*
	 * VAI EXPOR UM METODO QUE RETORNA O TIPO DA INTERFACE(SELLERDAO),
	 * mas internamente ela vai instanciar uma implementacao
	 * nao expoe a implementacao
	 */
	public static SellerDAO createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
}
