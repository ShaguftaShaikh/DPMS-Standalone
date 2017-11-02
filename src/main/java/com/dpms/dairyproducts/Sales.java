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
		while(true){
			System.out.println("1. Show Inventory");
			System.out.println("2. Order Product(s)");
			System.out.println("3. View Orders");
			System.out.println("4. View Supplier Detail");
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
				showOrderedProducts();
				break;
			case 3:
				viewOrders();
				break;
			case 4:
				System.out.println("1. Display All Supplier Details");
				System.out.println("2. Search by Supplying Product(s)");
				System.out.println("3. Search by Name");
				System.out.println("4. Search by City");
				System.out.println("Enter your choice: ");
				int c = sc.nextInt();
				sc.nextLine();
				switch (c) {
				case 1:
					displayAllSupplierDetailes();
					break;
				case 2:
					System.out.println("Enter product name: ");
					String productName = sc.nextLine();
					searchByProduct(productName);
					break;
				case 3:
					System.out.println("Enter name of supplier: ");
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

	private void searchByCity(String city) {
		// TODO Auto-generated method stub
		String sql = "SELECT city_id, name FROM \"DPMS\".city WHERE lower(name) = ?;";
		String cityName = "";
		int id = 0;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, city.toLowerCase());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs!=null){
				while(rs.next()){
					cityName = rs.getString("name");
					id = rs.getInt("city_id");
				}
			}
			cityName = cityName.trim();
			sql = "SELECT s.supplier_id, s.name, s.contact_number, s.street_no, s.street_name, c.name as city_name, s.email"+
				" FROM \"DPMS\".supplier as s, \"DPMS\".city as c WHERE s.city_id = ? AND c.city_id = s.city_id";
			//System.out.println(sql);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			String supplierId = "";
			String supplierName = "";
			String contact = "";
			String address = "";
			String email = "";
			if(rs!=null){
				while(rs.next()){
					supplierId = rs.getString("supplier_id").trim();
					supplierName = rs.getString("name").trim();
					contact = rs.getString("contact_number").trim();
					address = rs.getString("street_no").trim()+" "+rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();
					
					System.out.println("id: "+supplierId+" Name: "+supplierName+" contact: "+contact+" Address: "+address+" Email: "+email+" City: "+cityName);
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
		String sql = "SELECT s.supplier_id, s.name, s.contact_number, s.street_no, s.street_name, c.name as city_name, s.email"+
					" FROM \"DPMS\".supplier as s,\"DPMS\".city as c WHERE lower(s.name)=? AND s.city_id = c.city_id;";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, sName.toLowerCase());
			ResultSet rs = preparedStatement.executeQuery();
			String supplierId = "";
			String supplierName = "";
			String contact = "";
			String address = "";
			String email = "";
			String cityName = "";
			if(rs!=null){
				while(rs.next()){
					supplierId = rs.getString("supplier_id").trim();
					supplierName = rs.getString("name").trim();
					contact = rs.getString("contact_number").trim();
					address = rs.getString("street_no").trim()+" "+rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();
					
					System.out.println("id: "+supplierId+" Name: "+supplierName+" contact: "+contact+" Address: "+address+" Email: "+email+" City: "+cityName);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void searchByProduct(String productName) {
		// TODO Auto-generated method stub
		String sql ="SELECT s.supplier_id, s.name, s.contact_number, c.name as city_name, s.email,s.street_no,s.street_name,"+
					"p.product_name as supplying_product"+
					" FROM \"DPMS\".supplier as s,\"DPMS\".city as c,\"DPMS\".supply_product as x,\"DPMS\".product as p"+ 
					" WHERE lower(p.product_name)=lower(?) AND c.city_id = s.city_id "
					+ "AND s.supplier_id = x.supplier_id AND x.product_id = p.product_id"+
					" order by(supplier_id)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, productName);
			ResultSet rs = preparedStatement.executeQuery();
			String supplierId = "";
			String supplierName = "";
			String contact = "";
			String address = "";
			String email = "";
			String cityName = "";
			String productSupplying = "";
			if(rs!=null){
				while(rs.next()){
					supplierId = rs.getString("supplier_id").trim();
					supplierName = rs.getString("name").trim();
					contact = rs.getString("contact_number").trim();
					address = rs.getString("street_no").trim()+" "+rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();
					productSupplying = rs.getString("supplying_product").trim();
					System.out.println("id: "+supplierId+" Name: "+supplierName+" Supplying Product: "+productSupplying+
							" contact: "+contact+" Address: "+address+" Email: "+email+" City: "+cityName);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void displayAllSupplierDetailes() {
		// TODO Auto-generated method stub
		String sql = "SELECT s.supplier_id, s.name, s.contact_number, s.street_no, s.street_name, c.name as city_name, s.email"+
				" FROM \"DPMS\".supplier as s,\"DPMS\".city as c WHERE s.city_id = c.city_id";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String supplierId = "";
			String supplierName = "";
			String contact = "";
			String address = "";
			String email = "";
			String cityName = "";
			if(rs!=null){
				while(rs.next()){
					supplierId = rs.getString("supplier_id").trim();
					supplierName = rs.getString("name").trim();
					contact = rs.getString("contact_number").trim();
					address = rs.getString("street_no").trim()+" "+rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();
					
					System.out.println("id: "+supplierId+" Name: "+supplierName+" contact: "+contact+" Address: "+address+" Email: "+email+" City: "+cityName);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void viewOrders() {
		// TODO Auto-generated method stub
		
	}

	private void showOrderedProducts() {
		// TODO Auto-generated method stub
		
	}
}
