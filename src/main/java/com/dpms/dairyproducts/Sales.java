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
				System.out.println("1. Show All Orders");
				System.out.println("2. Find Order by Order Id");
				System.out.println("3. Find Order by Order Date");
				System.out.println("4. Find Order by Order Retailer Id");
				System.out.println("5. Find Order with Maximum Amount");
				System.out.println("6. Find Order by Maximum Supplier Supplied");
				System.out.println("7. Find Maximum Product Ordered");
				System.out.println("8. Find Order with Minimum Amount");
				System.out.println("9. Find Order by Minimum Supplier Supplied");
				System.out.println("10. Find Order by Minimum Product Ordered");
				System.out.println("11. Find Average Amount of Orders");
				System.out.println("Enter Your Choice: ");
				choice = sc.nextInt();
				sc.nextLine();
				switch (choice) {
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				case 11:
					break;
				case 12:
					break;
				default:
					break;
				}
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
		String sql = "SELECT retailer_id, name, contact_no, street_no, street_name, city_name, email"
				+ " FROM \"DPMS\".retailer_info order by(retailer_id)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void searchByCity(String city) {
		// TODO Auto-generated method stub
		String sql = "SELECT retailer_id, name, contact_no, street_no, street_name, city_name, email"
				+ " FROM \"DPMS\".retailer_info WHERE lower(city_name) = lower(?) order by(retailer_id)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, city.toLowerCase());
			ResultSet rs = preparedStatement.executeQuery();
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
					city = rs.getString("city_name").trim();
					System.out.println("id: " + retailerId + " Name: " + retailerName + " contact: " + contact
							+ " Address: " + address + " Email: " + email + " City: " + city);
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
		String sql = "SELECT retailer_id, name, contact_no, street_no, street_name, city_name, email"+
					" FROM \"DPMS\".retailer_info WHERE lower(name) = lower(?) order by(retailer_id)";
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