
package application;

import java.util.List;
import java.util.Scanner;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

/**
 *
 * @author jasom
 */
public class Program2 {
    public static void main(String[] args) {
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao(); //Dessa maneira protegemos a nossa implentação de intanciação do objeto DAO
        Scanner sc = new Scanner(System.in);
        
        System.out.println("================ TEST 1: Department FindAll ================");
        List <Department> department = departmentDao.findAll();
        for(Department dep:department){
            System.out.println(dep);
        }
        
        System.out.println();
        System.out.println("================ TEST 2: Department FindById ================");
        Department dep = departmentDao.findById(2);
        System.out.println(dep);
        
        
        System.out.println();
        System.out.println("================ TEST 3: Department INSERT ================");
        Department newDepartment = new Department(null, "Sales");
        departmentDao.insert(newDepartment);
        System.out.println("Inserted! Department Id = " + newDepartment.getId());
        
        System.out.println();
        System.out.println("================ TEST 4: Department UPDATE ================");
        dep = departmentDao.findById(2);
        dep.setName("Mechanicals");
        departmentDao.update(dep);
        System.out.println("Updated!");
        
        System.out.println();
        System.out.println("============== TEST 5: Department delete ==============");
        System.out.println("Enter id for Delete test: ");
        int id = sc.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Delete Completed!");
    }
    
    
}
