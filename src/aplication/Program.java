package aplication;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {


		//teste
		//cria o sellerDaro, na fabrica
		SellerDAO sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("== TEST 1 = SELLER FINDBYID ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
	}

}
