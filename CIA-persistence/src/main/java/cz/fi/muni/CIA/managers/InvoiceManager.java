package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Invoice;

import java.util.List;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public interface InvoiceManager {

	void addInvoice(Invoice invoice);

	void deleteInvoice(Long id);

	void updateInvoice(Invoice invoice);

	Invoice findById(Long id);

	List<Invoice> getAll();

	List<Invoice> getAllIncomes();

	List<Invoice> getAllExpenses();

	List<Invoice> getAllForPerson(Long personId);

}
