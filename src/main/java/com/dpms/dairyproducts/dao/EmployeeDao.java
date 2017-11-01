package com.dpms.dairyproducts.dao;

import java.sql.Connection;

public interface EmployeeDao {

	public String findDepartment(String name,Connection connection);
}
