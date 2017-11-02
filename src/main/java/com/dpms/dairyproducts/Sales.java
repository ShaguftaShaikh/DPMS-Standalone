package com.dpms.dairyproducts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.dpms.dairyproducts.impl.InventoryDaoImpl;

public class Sales {

	private Connection connection;

	public Sales(Connection connection) {
		// TODO Auto-generated constructor stub
		this.connection = connection;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("1. Show Inventory");
			System.out.println("2. Order Product(s)");
			System.out.println("3. View Orders");
			System.out.println("4. View Retailer's Detail");
			System.out.println("5. Exit");
			System.out.println("Enter your choice: ");
			int choice = sc.nextInt();
			switch (choice) {
			case 1:
				System.out.println("1. Show All Products");
				System.out.println("2. Show Non-Available Products ");
				System.out.println("3. Show Only Available Products");
				System.out.println("Enter Your Choice: ");
				choice = sc.nextInt();
				sc.nextLine();
				switch (choice) {
				case 1:
					new InventoryDaoImpl().showInventory(connection);
					break;
				case 2:
					new InventoryDaoImpl().showNonAvailableProducts(connection);
					break;
				case 3:
					new InventoryDaoImpl().showOnlyAvailableProducts(connection);
					break;
				default:
					System.out.println("Invalid Choice");
					break;
				}
				break;
			case 2:
				// showProducts();
				break;
			case 3:
				// viewOrders();
				break;
			case 4:
				System.out.println("1. Display All Retailer's Details");
				System.out.println("2. Search by Retailer's Product(s)");
				System.out.println("3. Search by Retailer's Name");
				System.out.println("4. Search by Retailer's City");
				System.out.println("Enter your choice: ");
				int c = sc.nextInt();
				sc.nextLine();
				switch (c) {
				case 1:
					displayAllRetailerDetailes();
					break;
				case 2:
					System.out.println("Enter product name: ");
					String productName = sc.nextLine();
					searchByProduct(productName);
					break;
				case 3:
					System.out.println("Enter name of retailer: ");
					String sName = sc.nextLine();
					searchByName(sName);
					break;
				case 4:
					System.out.println("Enter city: ");
					String city = sc.nextLine();
					searchByCity(city);
					break;
				default:
					System.out.println("Invalid Choice");
					break;
				}
				break;
			case 5:
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Choice");
				break;
			}
		}

	}

	private void searchByProduct(String productName) {
		// TODO Auto-generated method stub
		String sql = "SELECT r.retailer_id, r.name, r.contact_no, c.name as city_name, r.email,r.street_no,r.street_name,"
				+ " p.product_name as retailing_product"
				+ " FROM \"DPMS\".retailer as r,\"DPMS\".city as c,\"DPMS\".retail_product as x,\"DPMS\".product as p"
				+ " WHERE lower(p.product_name)=lower(?) AND c.city_id = r.city_id"
				+ " AND r.retailer_id = x.retailer_id AND x.product_id = p.product_id" + " order by(retailer_id)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, productName);
			ResultSet rs = preparedStatement.executeQuery();
			String retailerId = "";
			String retalierName = "";
			String contact = "";
			String address = "";
			String email = "";
			String cityName = "";
			String retailingSupplying = "";
			if (rs != null) {
				while (rs.next()) {
					retailerId = rs.getString("retailer_id").trim();
					retalierName = rs.getString("name").trim();
					contact = rs.getString("contact_no").trim();
					address = rs.getString("street_no").trim() + " " + rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();
					retailingSupplying = rs.getString("retailing_product").trim();

					System.out.println("id: " + retailerId + " Name: " + retalierName + " Supplying Product: "
							+ retailingSupplying + " contact: " + contact + " Address: " + address + " Email: " + email
							+ " City: " + cityName);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void displayAllRetailerDetailes() {
		// TODO Auto-generated method stub
		String sql = "SELECT r.retailer_id, r.name, r.contact_no, r.street_no, r.street_name, c.name as city_name, r.email"+
					" FROM \"DPMS\".retailer as r,\"DPMS\".city as c WHERE c.city_id = r.city_id order by(retailer_id)";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String retailerId = "";
			String retailerName = "";
			String contact = "";
			String address = "";
			String email = "";
			String cityName = "";
			if(rs!=null){
				while(rs.next()){
					retailerId = rs.getString("retailer_id").trim();
					retailerName = rs.getString("name").trim();
					contact = rs.getString("contact_no").trim();
					address = rs.getString("street_no").trim()+" "+rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();
					
					System.out.println("id: "+retailerId+" Name: "+retailerName+" contact: "+contact+" Address: "+address+" Email: "+email+" City: "+cityName);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void searchByCity(String city) {
		// TODO Auto-generated method stub
		String sql = "SELECT city_id, name FROM \"DPMS\".city WHERE lower(name) = ?;";
		String cityName = "";
		int id = 0;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, city.toLowerCase());
			ResultSet rs = preparedStatement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					cityName = rs.getString("name");
					id = rs.getInt("city_id");
				}
			}
			cityName = cityName.trim();
			sql = "SELECT r.retailer_id, r.name, r.contact_no, r.street_no, r.street_name, c.name as city_name, r.email"
					+ " FROM \"DPMS\".retailer as r, \"DPMS\".city as c WHERE r.city_id = ? AND c.city_id = r.city_id";
			// System.out.println(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			String retailerId = "";
			String retailerName = "";
			String contact = "";
			String address = "";
			String email = "";
			if (rs != null) {
				while (rs.next()) {
					retailerId = rs.getString("retailer_id").trim();
					retailerName = rs.getString("name").trim();
					contact = rs.getString("contact_no").trim();
					address = rs.getString("street_no").trim() + " " + rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();
					System.out.println("id: " + retailerId + " Name: " + retailerName + " contact: " + contact
							+ " Address: " + address + " Email: " + email + " City: " + cityName);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void searchByName(String sName) {
		// TODO Auto-generated method stub
		String sql = "SELECT r.retailer_id, r.name, r.contact_no, r.street_no, r.street_name, c.name as city_name, r.email"
				+ " FROM \"DPMS\".retailer as r,\"DPMS\".city as c WHERE lower(r.name) = lower(?) AND r.city_id = c.city_id;";
		// System.out.println(sName);
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, sName);
			ResultSet rs = preparedStatement.executeQuery();
			String retailerId = "";
			String retailerName = "";
			String contact = "";
			String address = "";
			String email = "";
			String cityName = "";
			if (rs != null) {
				while (rs.next()) {
					retailerId = rs.getString("retailer_id").trim();
					retailerName = rs.getString("name").trim();
					contact = rs.getString("contact_no").trim();
					address = rs.getString("street_no").trim() + " " + rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();

					System.out.println("id: " + retailerId + " Name: " + retailerName + " contact: " + contact
							+ " Address: " + address + " Email: " + email + " City: " + cityName);
					System.out.println();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}