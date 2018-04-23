package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Bill;

import java.util.Collection;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public interface BillManager {

	void addBill(Bill bill);

	void removeBill(Bill bill);

	void updateBill(Bill bill);

	Bill findById(Long id);

	Collection<Bill> getAll();

	Collection<Bill> getAllIncomes();

	Collection<Bill> getAllExpenses();

}
