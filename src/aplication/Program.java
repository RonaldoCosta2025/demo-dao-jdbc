package aplication;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		// teste
		// cria o sellerDaro, na fabrica
		SellerDAO sellerDao = DaoFactory.createSellerDao();

		System.out.println("== TEST 1 = SELLER FINDBYID ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("\n== TEST 2 = SELLER FINDBYDEPARTMENT ===");

		// lista de departamento
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);

		for (Seller obj : list) {
			System.out.println(obj);
		}

		System.out.println("\n== TEST 3 = SELLER FINDALL ===");
		// lista todos vendedores
		
		list = sellerDao.findAll();

		for (Seller obj : list) {
			System.out.println(obj);
		}
	}

}
