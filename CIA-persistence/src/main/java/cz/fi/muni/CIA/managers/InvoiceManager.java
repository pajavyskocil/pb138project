package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Invoice;

import java.util.List;

/**
 * Manager for working with Person objects
 *
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public interface InvoiceManager {

	/**
	 * This method add given invoice into db
	 * @param invoice Invoice to add
	 */
	void addInvoice(Invoice invoice);

	/**
	 * This method delete the Invoice with given Id
	 * @param id Id of invoice
	 */
	void deleteInvoice(Long id);

	/**
	 * This method update the invoice in DB with given invoice
	 * @param invoice Invoice to update
	 */
	void updateInvoice(Invoice invoice);

	/**
	 * This method returns Invoice with given Id
	 * @param id Id of invoice
	 * @return Invoice with given Id
	 */
	Invoice findById(Long id);

	/**
	 * This method returns all Invoices
	 * @return array of all Invoices
	 */
	List<Invoice> getAll();

	/**
	 * This method returns all Incomes
	 * @return array of all Incomes
	 */
	List<Invoice> getAllIncomes();

	/**
	 * This method returns all Expenses
	 * @return array of all Expenses
	 */
	List<Invoice> getAllExpenses();

	/**
	 * This method returns all Invoices for person with given PersonId
	 * @param personId Id of person
	 * @return array of all Invoices for person with given PersonId
	 */
	List<Invoice> getAllForPerson(Long personId);

}
