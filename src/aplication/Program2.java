package aplication;


import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDAO;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		// teste
		// cria o sellerDaro, na fabrica
		DepartmentDAO depDao = DaoFactory.createDepartmentDao();

		System.out.println("== TEST 1 = Department FINDBYID ===");
		Department dep = depDao.findById(2);
		System.out.println(dep);

		
		System.out.println("\n== TEST 2 = DEPARTMENT FINDALL ===");

		// lista de departamento
		List<Department> list = depDao.findAll();

		for (Department obj : list) {
			System.out.println(obj);
		}
	
		
		
		System.out.println("\n== TEST 3 = DEPARTMENT INSERT ===");
		dep = new Department(null, "Cars");
		depDao.insert(dep);
		System.out.println("Inserted! New category = " + dep.getName());
		
		
				
		System.out.println("\n== TEST 4 = DEPARTMENT UPDATE ===");
		dep = depDao.findById(6);
		dep.setName("Food");
		depDao.update(dep);
		System.out.println("Update completed! " + dep);
		
		
	
		System.out.println("\n== TEST 5 = DEPARTMENT DELETE ===");
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		depDao.deleteById(id);
		System.out.println("Delete completed! ");
		list = depDao.findAll();

		for (Department obj : list) {
			System.out.println(obj);
		}
		
		sc.close();
		
	}

}
