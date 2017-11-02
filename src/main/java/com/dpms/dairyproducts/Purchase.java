package com.dpms.dairyproducts;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import com.dpms.dairyproducts.impl.InventoryDaoImpl;

public class Purchase {

	private Connection connection;

	public Purchase(Connection connection) {
		// TODO Auto-generated constructor stub
		this.connection = connection;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("1. Show Inventory");
			System.out.println("2. Place Order of Product(s)");
			System.out.println("3. View Orders");
			System.out.println("4. View Supplier Detail");
			System.out.println("5. Exit");
			System.out.println("Enter your choice: ");
			int choice = sc.nextInt();
			sc.nextLine();
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

				break;
			case 3:
				System.out.println("1. Show All Orders");
				System.out.println("2. Find Order by Order Id");
				System.out.println("3. Find Order by Order Date");
				System.out.println("4. Find Order by Order Supplier Name");
				System.out.println("5. Find Order with Maximum Amount");
				System.out.println("6. Find Maximum Product Ordered");
				System.out.println("7. Find Order with Minimum Amount");
				System.out.println("8. Find Order by Minimum Supplier Supplied");
				System.out.println("9. Find Order by Minimum Product Ordered");
				System.out.println("10. Find Average Amount of Orders");
				System.out.println("Enter Your Choice: ");
				choice = sc.nextInt();
				sc.nextLine();
				switch (choice) {
				case 1:
					showAllOrders();
					break;
				case 2:
					System.out.println("Enter Order Id: ");
					int id = sc.nextInt();
					sc.nextLine();
					findOrderById(id);
					break;
				case 3:
					System.out.println("Enter Order Date(yyyy-mm-dd): ");
					String date = sc.nextLine();
					findByOrderDate(date);
					break;
				case 4:
					System.out.println("Enter Retailer Id: ");
					String name = sc.nextLine();
					findByRetailerId(name);
					break;
				case 5:
					findOrderWithMaxAmount();
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
				default:
					break;
				}
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

	private void findOrderWithMaxAmount() {
		// TODO Auto-generated method stub
		String sql = "SELECT sales_order_id,total_price,selling_date,name from \"DPMS\".sales_order_amount_details WHERE"
				+ " total_price = (SELECT MAX(total_price) FROM \"DPMS\".sales_order_amount_details)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String sellingDate = "";
			String price = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("sales_order_id").trim();
					sellingDate = rs.getString("selling_date").trim();
					price = rs.getString("total_price").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + sellingDate + " Price: " + price + " Name: " + name);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void findByRetailerId(String name) {
		// TODO Auto-generated method stub
		String sql = "SELECT sales_order_id, selling_date, name, quantity, price, product_name"
				+ " FROM \"DPMS\".sales_order_full_details WHERE lower(name) = lower(?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String sellingDate = "";
			String quantity = "";
			String price = "";
			String productName = "";
			// String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("sales_order_id").trim();
					sellingDate = rs.getString("selling_date").trim();
					quantity = rs.getString("quantity").trim();
					price = rs.getString("price").trim();
					productName = rs.getString("product_name").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + sellingDate + " Product Name: "
							+ productName + " Quantity: " + quantity + " Price: " + price + " Name: " + name);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void findByOrderDate(String date) {
		// TODO Auto-generated method stub
		String sql = "SELECT sales_order_id, selling_date, name, quantity, price, product_name"
				+ " FROM \"DPMS\".sales_order_full_details WHERE selling_date = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date d = f.parse(date);
			preparedStatement.setDate(1, new Date(d.getTime()));

			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String sellingDate = "";
			String quantity = "";
			String price = "";
			String productName = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("sales_order_id").trim();
					sellingDate = rs.getString("selling_date").trim();
					quantity = rs.getString("quantity").trim();
					price = rs.getString("price").trim();
					productName = rs.getString("product_name").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + sellingDate + " Product Name: "
							+ productName + " Quantity: " + quantity + " Price: " + price + " Name: " + name);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void findOrderById(int id) {
		// TODO Auto-generated method stub
		String sql = "SELECT sales_order_id, selling_date, name, quantity, price, product_name"
				+ " FROM \"DPMS\".sales_order_full_details WHERE sales_order_id = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String sellingDate = "";
			String quantity = "";
			String price = "";
			String productName = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("sales_order_id").trim();
					sellingDate = rs.getString("selling_date").trim();
					quantity = rs.getString("quantity").trim();
					price = rs.getString("price").trim();
					productName = rs.getString("product_name").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + sellingDate + " Product Name: "
							+ productName + " Quantity: " + quantity + " Price: " + price + " Name: " + name);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void showAllOrders() {
		// TODO Auto-generated method stub
		String sql = "SELECT sales_order_id, selling_date, name, quantity, price, product_name"
				+ " FROM \"DPMS\".sales_order_full_details";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String sellingDate = "";
			String quantity = "";
			String price = "";
			String productName = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("sales_order_id").trim();
					sellingDate = rs.getString("selling_date").trim();
					quantity = rs.getString("quantity").trim();
					price = rs.getString("price").trim();
					productName = rs.getString("product_name").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + sellingDate + " Product Name: "
							+ productName + " Quantity: " + quantity + " Price: " + price + " Name: " + name);
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

		String sql = "SELECT supplier_id, name, contact_number, street_no, street_name, city_name, email"
				+ " FROM \"DPMS\".supplier_info WHERE lower(city_name) = lower(?) order by (supplier_id)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, city);
			ResultSet rs = preparedStatement.executeQuery();
			String supplierId = "";
			String supplierName = "";
			String contact = "";
			String address = "";
			String email = "";
			if (rs != null) {
				while (rs.next()) {
					supplierId = rs.getString("supplier_id").trim();
					supplierName = rs.getString("name").trim();
					contact = rs.getString("contact_number").trim();
					address = rs.getString("street_no").trim() + " " + rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					city = rs.getString("city_name").trim();

					System.out.println("id: " + supplierId + " Name: " + supplierName + " contact: " + contact
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
		String sql = "SELECT supplier_id, name, contact_number, street_no, street_name, city_name, email"
				+ " FROM \"DPMS\".supplier_info WHERE lower(name) = lower(?) order by (supplier_id)";

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
			if (rs != null) {
				while (rs.next()) {
					supplierId = rs.getString("supplier_id").trim();
					supplierName = rs.getString("name").trim();
					contact = rs.getString("contact_number").trim();
					address = rs.getString("street_no").trim() + " " + rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();

					System.out.println("id: " + supplierId + " Name: " + supplierName + " contact: " + contact
							+ " Address: " + address + " Email: " + email + " City: " + cityName);
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
		String sql = "SELECT s.supplier_id, s.name, s.contact_number, c.name as city_name, s.email,s.street_no,s.street_name,"
				+ "p.product_name as supplying_product"
				+ " FROM \"DPMS\".supplier as s,\"DPMS\".city as c,\"DPMS\".supply_product as x,\"DPMS\".product as p"
				+ " WHERE lower(p.product_name)=lower(?) AND c.city_id = s.city_id "
				+ "AND s.supplier_id = x.supplier_id AND x.product_id = p.product_id" + " order by(supplier_id)";
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
			if (rs != null) {
				while (rs.next()) {
					supplierId = rs.getString("supplier_id").trim();
					supplierName = rs.getString("name").trim();
					contact = rs.getString("contact_number").trim();
					address = rs.getString("street_no").trim() + " " + rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();
					productSupplying = rs.getString("supplying_product").trim();
					System.out.println("id: " + supplierId + " Name: " + supplierName + " Supplying Product: "
							+ productSupplying + " contact: " + contact + " Address: " + address + " Email: " + email
							+ " City: " + cityName);
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
		String sql = "SELECT supplier_id, name, contact_number, street_no, street_name, city_name, email"
				+ " FROM \"DPMS\".supplier_info order by (supplier_id)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String supplierId = "";
			String supplierName = "";
			String contact = "";
			String address = "";
			String email = "";
			String cityName = "";
			if (rs != null) {
				while (rs.next()) {
					supplierId = rs.getString("supplier_id").trim();
					supplierName = rs.getString("name").trim();
					contact = rs.getString("contact_number").trim();
					address = rs.getString("street_no").trim() + " " + rs.getString("street_name").trim();
					email = rs.getString("email").trim();
					cityName = rs.getString("city_name").trim();

					System.out.println("id: " + supplierId + " Name: " + supplierName + " contact: " + contact
							+ " Address: " + address + " Email: " + email + " City: " + cityName);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
