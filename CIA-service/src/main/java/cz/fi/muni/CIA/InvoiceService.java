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
	 * Get all Invoice entities by their Person id
	 *
	 * @param personId whom InvoiceService entity belongs to
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByPersonId(Long personId);

	/**
	 * Get all Invoice entities by their Type and Person id
	 *
	 * @param oldest date from which Invoices will be included
	 * @param newest date to which Invoices will be included
	 * @param personId whom InvoiceService entity belongs to
	 * @param type of Invoice entity
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByPersonIdAndTypeAndDate(LocalDate oldest, LocalDate newest, Long personId, InvoiceType type);

	/**
	 * Get all Invoice entities by Person id and date
	 *
	 * @param oldest date from which Invoices will be included
	 * @param newest date to which Invoices will be included
	 * @param personId
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByPersonIdAndDate(LocalDate oldest, LocalDate newest, Long personId);

	/**
	 * Get all Invoice entities by their tape and date
	 *
	 * @param oldest date from which Invoices will be included
	 * @param newest date to which Invoices will be included
	 * @param type of Invoice entity
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByTypeAndDate(LocalDate oldest, LocalDate newest, InvoiceType type);

	/**
	 * Get all InvoiceService entities by their date
	 *
	 * @param oldest date from which Invoices will be included
	 * @param newest date to which Invoices will be included
	 * @return List of InvoiceService entities
	 */
	List<Invoice> getInvoicesInDateInterval(LocalDate oldest, LocalDate newest);

	/**
	 * Get all Invoice entities by their type
	 *
	 * @param type of Invoice entity
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByType(InvoiceType type);

	/**
	 * Get all Invoice entities of person (person can be both payer and recipient)
	 *
	 * @param personId of Person
	 * @return List of Invoice entities
	 */
	List<Invoice> getInvoicesByPerson(Long personId);
}
