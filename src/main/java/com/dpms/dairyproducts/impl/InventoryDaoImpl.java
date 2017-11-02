package com.dpms.dairyproducts.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dpms.dairyproducts.dao.InventoryDao;

public class InventoryDaoImpl implements InventoryDao {

	public void showOnlyAvailableProducts(Connection connection) {
		// TODO Auto-generated method stub
		String sql = "SELECT i.product_id, p.product_name,i.manufacturing_date, i.quantity "+
				"FROM \"DPMS\".product_inventory as i,\"DPMS\".product as p WHERE quantity != 0 AND i.product_id = p.product_id";
	try {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		if (rs != null) {
			String product_id = "";
			String product_name = "";
			String maufact_date = "";
			String quantity = "";
			
			while (rs.next()) {
				product_id = rs.getString("product_id").trim();
				product_name = rs.getString("product_name").trim();
				maufact_date = rs.getString("manufacturing_date").trim();
				quantity = rs.getString("quantity").trim();
				//Date date = new SimpleDateFormat("dd-MM-yyyy").parse(maufact_date);
				System.out.println("product id: " + product_id + " product name: " + product_name
						+ " manufacturing date: " + maufact_date + " quantity: " + quantity);
			}
		}
	} catch (SQLException e) {
		// TODO: handle exception
		e.printStackTrace();
	} 
	}

	public void showNonAvailableProducts(Connection connection) {
		// TODO Auto-generated method stub
		String sql = "SELECT i.product_id, p.product_name,i.manufacturing_date, i.quantity "
				+ "FROM \"DPMS\".product_inventory as i,\"DPMS\".product as p WHERE quantity = 0 AND i.product_id = p.product_id";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs != null) {
				String product_id = "";
				String product_name = "";
				String maufact_date = "";
				String quantity = "";

				while (rs.next()) {
					product_id = rs.getString("product_id").trim();
					product_name = rs.getString("product_name").trim();
					maufact_date = rs.getString("manufacturing_date").trim();
					quantity = rs.getString("quantity").trim();
					// Date date = new
					// SimpleDateFormat("dd-MM-yyyy").parse(maufact_date);
					System.out.println("product id: " + product_id + " product name: " + product_name
							+ " manufacturing date: " + maufact_date + " quantity: " + quantity);
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void showInventory(Connection connection) {
		// TODO Auto-generated method stub
		String sql = "SELECT i.product_id, p.product_name,i.manufacturing_date, i.quantity "
				+ "FROM \"DPMS\".product_inventory as i,\"DPMS\".product as p WHERE i.product_id = p.product_id;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs != null) {
				String product_id = "";
				String product_name = "";
				String maufact_date = "";
				String quantity = "";

				while (rs.next()) {
					product_id = rs.getString("product_id").trim();
					product_name = rs.getString("product_name").trim();
					maufact_date = rs.getString("manufacturing_date").trim();
					quantity = rs.getString("quantity").trim();
					// Date date = new
					// SimpleDateFormat("dd-MM-yyyy").parse(maufact_date);
					System.out.println("product id: " + product_id + " product name: " + product_name
							+ " manufacturing date: " + maufact_date + " quantity: " + quantity);
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
