package application;

import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.impl.DepartmentDaoJDBC;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		System.out.println("===Test 1 : Department insert===");
		Department depi =  new Department(8, "NonAnimators");
		departmentDao.insert(depi);
		System.out.println("Inserted completed! new Department is: "+ depi.getName());
		System.out.println("===Test 1 : Department insert===");
		Department dep =  new Department(7, "Animators");
		departmentDao.insert(dep);
		System.out.println("Inserted completed! new Department is: "+ dep.getName());
		System.out.println("===Test 1 : Department insert===");
	}

}
