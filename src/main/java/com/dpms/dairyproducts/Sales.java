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
				int retailer_id = 0;
				int product_id = 0;
				int quantity = 0;
				int price = 0;

				System.out.println("Enter Retailer id: ");
				retailer_id = sc.nextInt();
				System.out.println("Enter Product id: ");
				product_id = sc.nextInt();
				System.out.println("Enter Quantity: ");
				quantity = sc.nextInt();
				System.out.println("Enter Price: ");
				price = sc.nextInt();

				boolean check = checkAvailability(product_id, quantity);
				if(check){
					saleOrder(retailer_id,product_id,quantity,price);
				}else {
					System.out.println("Product Not Available");
				}
				break;
			case 3:
				System.out.println("1. Show All Orders");
				System.out.println("2. Find Order by Order Id");
				System.out.println("3. Find Order by Order Date");
				System.out.println("4. Find Order by Order Retailer Id");
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
				case 7:
					findAverageAmount();
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

	private void saleOrder(int retailer_id, int product_id, int quantity, int price) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO \"DPMS\".sales_order (selling_date,retailer_id)VALUES(?,?)";
		String last = "SELECT currval('\"DPMS\".sales_order_sales_order_id_seq')";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			java.util.Date date = new java.util.Date();
			// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			preparedStatement.setDate(1, new Date(date.getTime()));
			preparedStatement.setInt(2, retailer_id);
			int update = preparedStatement.executeUpdate();
			if(update==1){
				preparedStatement = connection.prepareStatement(last);
				ResultSet rs = preparedStatement.executeQuery();
				int id = 0;
				if(rs!=null){
					while(rs.next())
						id = rs.getInt("currval");
				}
				//System.out.println(id);
				sql = "INSERT INTO \"DPMS\".sales_product_order VALUES(?,?,?,?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, id);
				preparedStatement.setInt(2, product_id);
				preparedStatement.setInt(3, quantity);
				preparedStatement.setInt(4, price);
				update = preparedStatement.executeUpdate();
				if(update==1)
					System.out.println("Success");
				System.out.println("Successfully Inserted");
			}else
				System.out.println("Some Error Occured");
		
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private boolean checkAvailability(int product_id, int quantity) {
		// TODO Auto-generated method stub
		String sql = "SELECT quantity FROM \"DPMS\".product_inventory WHERE product_id = ?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, product_id);
			ResultSet rs = preparedStatement.executeQuery();
			int q = 0;
			if (rs != null) {
				while (rs.next()) {
					q = rs.getInt("quantity");
				}
			}
			if(quantity>q)
				return false;
			return true;
			//System.out.println("Average Amount is: " + average);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	private void findAverageAmount() {
		// TODO Auto-generated method stub
		String sql = "SELECT AVG(total_price) as average FROM \"DPMS\".sales_order_amount_details";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			String average = "";
			if (rs != null) {

				while (rs.next()) {
					average = rs.getString("average");
				}
			}
			System.out.println("Average Amount is: " + average);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void findOrderWithMinAmount() {
		// TODO Auto-generated method stub
		String sql = "SELECT sales_order_id,total_price,selling_date,name from \"DPMS\".sales_order_amount_details WHERE"
				+ " total_price = (SELECT Min(total_price) FROM \"DPMS\".sales_order_amount_details)";

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

					System.out.println("Order id: " + orderId + " Selling date: " + sellingDate + " Price: " + price
							+ " Name: " + name);
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
		String sql = "SELECT retailer_id, name, contact_no, street_no, street_name, city_name, email"
				+ " FROM \"DPMS\".retailer_info WHERE lower(name) = lower(?) order by(retailer_id)";
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

					System.out.println("Order id: " + orderId + " Selling date: " + sellingDate + " Price: " + price
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

}