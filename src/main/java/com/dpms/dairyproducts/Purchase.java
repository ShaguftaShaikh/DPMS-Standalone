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
				int supplier_id = 0;
				int product_id = 0;
				int quantity = 0;
				int price = 0;

				System.out.println("Enter Retailer id: ");
				supplier_id = sc.nextInt();
				System.out.println("Enter Product id: ");
				product_id = sc.nextInt();
				System.out.println("Enter Quantity: ");
				quantity = sc.nextInt();
				System.out.println("Enter Price: ");
				price = sc.nextInt();

				boolean check = checkAvailability(product_id);
				if (check) {
					purchaseOrder(supplier_id, product_id, quantity, price);
				} else {
					System.out.println("Product has enough availibility");
				}
				break;
			case 3:
				System.out.println("1. Show All Orders");
				System.out.println("2. Find Order by Order Id");
				System.out.println("3. Find Order by Order Date");
				System.out.println("4. Find Order by Order Supplier Id");
				System.out.println("5. Find Order with Maximum Amount");
				System.out.println("6. Find Order with Minimum Amount");
				System.out.println("7. Find Average Amount of Orders");
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
					findOrderWithMinAmount();
					break;
				default:
					System.out.println("Invalid Choice");
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

	private void purchaseOrder(int supplier_id, int product_id, int quantity, int price) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO \"DPMS\".purchase_order(order_date, supplier_id)"+
					" VALUES (?,?)";
		String last = "SELECT currval('\"DPMS\".purchase_order_purchase_order_id_seq')";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			java.util.Date date = new java.util.Date();
			// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			preparedStatement.setDate(1, new Date(date.getTime()));
			preparedStatement.setInt(2, supplier_id);
			int update = preparedStatement.executeUpdate();
			if (update == 1) {
				preparedStatement = connection.prepareStatement(last);
				ResultSet rs = preparedStatement.executeQuery();
				int id = 0;
				if (rs != null) {
					while (rs.next())
						id = rs.getInt("currval");
				}
				// System.out.println(id);
				sql = "INSERT INTO \"DPMS\".purchase_product_order(product_id, purchase_order_id, quantity, price)"+
						" VALUES (?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, product_id);
				preparedStatement.setInt(3, quantity);
				preparedStatement.setInt(4, price);
				update = preparedStatement.executeUpdate();
				if (update == 1)
					System.out.println("Success");
				System.out.println("Successfully Inserted");
			} else
				System.out.println("Some Error Occured");

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private boolean checkAvailability(int product_id) {
		// TODO Auto-generated method stub
		String sql = "SELECT quantity FROM \"DPMS\".product_inventory WHERE product_id = ?;";
		int q = 0;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, product_id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					q = rs.getInt("quantity");
				}
			}
			if (q < 10)
				return true;
			return false;
			// System.out.println("Average Amount is: " + average);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	private void findOrderWithMinAmount() {
		// TODO Auto-generated method stub
		String sql = "SELECT purchase_order_id, order_date, name, total_quantity, total_price"
				+ " FROM \"DPMS\".purchase_order_amount_details WHERE total_price = (SELECT MIN(total_price)"
				+ " FROM \"DPMS\".purchase_order_amount_details)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String purchaseDate = "";
			String price = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("purchase_order_id").trim();
					purchaseDate = rs.getString("order_date").trim();
					price = rs.getString("total_price").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + purchaseDate + " Price: " + price
							+ " Name: " + name);
					System.out.println();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void findOrderWithMaxAmount() {
		// TODO Auto-generated method stub
		String sql = "SELECT purchase_order_id, order_date, name, total_quantity, total_price"
				+ " FROM \"DPMS\".purchase_order_amount_details WHERE total_price = (SELECT MAX(total_price)"
				+ " FROM \"DPMS\".purchase_order_amount_details)";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String purchaseDate = "";
			String price = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("purchase_order_id").trim();
					purchaseDate = rs.getString("order_date").trim();
					price = rs.getString("total_price").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + purchaseDate + " Price: " + price
							+ " Name: " + name);
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
		String sql = "SELECT purchase_order_id, order_date, name, quantity, price, product_name"
				+ " FROM \"DPMS\".purchase_order_full_details WHERE lower(name) = lower(?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String purchaseDate = "";
			String quantity = "";
			String price = "";
			String productName = "";
			// String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("purchase_order_id").trim();
					purchaseDate = rs.getString("order_date").trim();
					quantity = rs.getString("quantity").trim();
					price = rs.getString("price").trim();
					productName = rs.getString("product_name").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + purchaseDate + " Product Name: "
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
		String sql = "SELECT purchase_order_id, order_date, name, quantity, price, product_name"
				+ " FROM \"DPMS\".purchase_order_full_details WHERE order_date = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date d = f.parse(date);
			preparedStatement.setDate(1, new Date(d.getTime()));

			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String purchaseDate = "";
			String quantity = "";
			String price = "";
			String productName = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("purchase_order_id").trim();
					purchaseDate = rs.getString("order_date").trim();
					quantity = rs.getString("quantity").trim();
					price = rs.getString("price").trim();
					productName = rs.getString("product_name").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + purchaseDate + " Product Name: "
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
		String sql = "SELECT purchase_order_id, order_date, name, quantity, price, product_name"
				+ " FROM \"DPMS\".purchase_order_full_details WHERE purchase_order_id=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String purchaseDate = "";
			String quantity = "";
			String price = "";
			String productName = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("purchase_order_id").trim();
					purchaseDate = rs.getString("order_date").trim();
					quantity = rs.getString("quantity").trim();
					price = rs.getString("price").trim();
					productName = rs.getString("product_name").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + purchaseDate + " Product Name: "
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
		String sql = "SELECT purchase_order_id, order_date, name, quantity, price, product_name"
				+ " FROM \"DPMS\".purchase_order_full_details";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String orderId = "";
			String purchaseDate = "";
			String quantity = "";
			String price = "";
			String productName = "";
			String name = "";
			if (rs != null) {
				while (rs.next()) {
					orderId = rs.getString("purchase_order_id").trim();
					purchaseDate = rs.getString("order_date").trim();
					quantity = rs.getString("quantity").trim();
					price = rs.getString("price").trim();
					productName = rs.getString("product_name").trim();
					name = rs.getString("name").trim();

					System.out.println("Order id: " + orderId + " Selling date: " + purchaseDate + " Product Name: "
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
