package com.dpms.dairyproducts.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dpms.dairyproducts.dao.EmployeeDao;

public class EmployeeDaoImpl implements EmployeeDao{

	//Find Department of the employee
	public String findDepartment(String name, Connection connection) {
		// TODO Auto-generated method stub
		String sql = "SELECT d.dept_id,d.dept_name FROM \"DPMS\".department as d,\"DPMS\".employee as e"+
					" WHERE emp_name LIKE ? AND d.dept_id = e.dept_id;";
		name+="%";
		String department = "";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs!=null){
				while(rs.next())
					department = rs.getString("dept_name");
			}
			return department;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}


}
