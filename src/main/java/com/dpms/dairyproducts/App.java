package com.dpms.dairyproducts;

import java.sql.Connection;
import java.util.Scanner;

import com.dpms.dairyproducts.configurations.ConnectToDB;
import com.dpms.dairyproducts.dao.EmployeeDao;
import com.dpms.dairyproducts.impl.EmployeeDaoImpl;

public class App {
    public static void main( String[] args ) {
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Enter username: ");
        String username = sc.next();
        System.out.println("Enter password: ");
        String password = sc.next();
        
        ConnectToDB db = new ConnectToDB();
        db.LoadDrivers();
        Connection connection = db.connect(username,password);
        if(connection!=null){
        	EmployeeDao employeeDao = new EmployeeDaoImpl();
        	String department = employeeDao.findDepartment(username, connection);
        	department = department.trim();
        	if(department.equalsIgnoreCase("purchase")){
        		new Purchase(connection);
        	}else if(department.equals("sales")){
        		new Sales(connection);
        	}
        	else
        		System.out.println("No department Found");
        } else {
        	System.out.println("No Such USer");
        }
        sc.close();
    }
}
