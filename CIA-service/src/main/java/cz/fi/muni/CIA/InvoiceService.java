package cz.fi.muni.CIA;

import cz.fi.muni.CIA.entities.Invoice;
import cz.fi.muni.CIA.entities.InvoiceType;

import java.time.LocalDate;
import java.util.List;

/**
 * interface for Invoice service
 *
 * @author Peter Balcirak <peter.balcirak@gmail.com>
 */
public interface InvoiceService {

	/**
	 * Create Invoice entity
	 *
	 * @param invoice which will be created
	 */
	void createInvoice(Invoice invoice);

	/**
	 * Delete Invoice entity
	 *
	 * @param id of Invoice which will be deleted
	 */
	void deleteInvoice(Long id);

	/**
	 * Edit Invoice entity
	 *
	 * @param invoice which will be edited
	 */
	void editInvoice(Invoice invoice);

	/**
	 * Get Invoice entity by its id
	 *
	 * @param id of Invoice entity
	 * @return Invoice entity
	 */
	Invoice getInvoiceById(Long id);

	/**
	 * Get all Invoice entities
	 *
	 * @return List of Invoice entities
	 */
	List<Invoice> getAllInvoices();

	/**
	 * Get all Invoice entities by their type
	 *
	 * @param type of Invoice entity
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByType(InvoiceType type);

	/**
	 * Get all Invoice entities by their Person id
	 *
	 * @param personId whom InvoiceService entity belongs to
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByPersonId(Long personId);

	/**
	 * Get all invoices in specified dates
	 *
	 * @param oldest date from which Invoices will be included
	 * @param newest date to which Invoices will be included
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesInDateInterval(LocalDate oldest, LocalDate newest);

	/**
	 * Get all Invoice entities by Person id and date
	 *
	 * @param oldest date from which Invoices will be included
	 * @param newest date to which Invoices will be included
	 * @param personId id of person who can be payer or recipient
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByPersonIdAndDate(LocalDate oldest, LocalDate newest, Long personId);

	/**
	 * Filter given list depending on specified type
	 * @param input list of invoices
	 * @param type which should be kept
	 * @return List of Invoice entitites
	 */
	List<Invoice> filterInvoicesByType(List<Invoice> input, InvoiceType type);
}
