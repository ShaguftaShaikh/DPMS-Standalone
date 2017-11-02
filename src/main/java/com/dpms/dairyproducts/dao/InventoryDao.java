package com.dpms.dairyproducts.dao;

import java.sql.Connection;

public interface InventoryDao {
	
	void showInventory(Connection connection);
	
	void showOnlyAvailableProducts(Connection connection);

	void showNonAvailableProducts(Connection connection);
}
