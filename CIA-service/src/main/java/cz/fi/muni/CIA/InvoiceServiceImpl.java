package cz.fi.muni.CIA;

import cz.fi.muni.CIA.entities.Invoice;
import cz.fi.muni.CIA.entities.InvoiceType;
import cz.fi.muni.CIA.managers.InvoiceManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * class for Invoice service implementation
 *
 * @author Peter Balcirak <peter.balcirak@gmail.com>
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

	private final InvoiceManager invoiceManager;

	@Inject
	public InvoiceServiceImpl(InvoiceManager invoiceManager) {
		this.invoiceManager = invoiceManager;
	}

	@Override
	public void createInvoice(Invoice invoice) {
		invoiceCheck(invoice);
		if (invoice.getId() != null) throw new IllegalArgumentException("Invoice id has to be null!");
		invoiceManager.addInvoice(invoice);
	}

	@Override
	public void deleteInvoice(Long id) {
		idCheck(id);
		invoiceManager.deleteInvoice(id);
	}

	@Override
	public void editInvoice(Invoice invoice) {
		invoiceCheck(invoice);
		idCheck(invoice.getId());
		invoiceManager.updateInvoice(invoice);
	}

	@Override
	public Invoice getInvoiceById(Long id) {
		idCheck(id);
		return invoiceManager.findById(id);
	}

	@Override
	public List<Invoice> getAllInvoices() {
		return invoiceManager.getAll();
	}

	@Override
	public List<Invoice> getInvoicesByPersonId(Long personId) {
		idCheck(personId);
		List<Invoice> allInvoices = getAllInvoices();
		return selectInvoicesByPersonId(personId, allInvoices);
	}

	@Override
	public List<Invoice> getInvoicesByPersonIdAndTypeAndDate(LocalDate oldest, LocalDate newest, Long personId, InvoiceType type) {
		idCheck(personId);

		List<Invoice> allInvoices = getInvoicesByType(type);
		allInvoices = selectInvoicesByDate(oldest, newest, allInvoices);

		return selectInvoicesByPersonId(personId, allInvoices);
	}

	@Override
	public List<Invoice> getInvoicesByPersonIdAndDate(LocalDate oldest, LocalDate newest, Long personId) {
		idCheck(personId);
		dateCheck(oldest, newest);

		List<Invoice> allInvoices = getInvoicesInDateInterval(oldest, newest);

		return selectInvoicesByPersonId(personId, allInvoices);
	}

	@Override
	public List<Invoice> getInvoicesByTypeAndDate(LocalDate oldest, LocalDate newest, InvoiceType type) {
		dateCheck(oldest, newest);

		List<Invoice> allInvoices = getInvoicesByType(type);

		return selectInvoicesByDate(oldest, newest, allInvoices);
	}

	@Override
	public List<Invoice> getInvoicesInDateInterval(LocalDate oldest, LocalDate newest) {
		dateCheck(oldest, newest);

		List<Invoice> allInvoices = getAllInvoices();

		return selectInvoicesByDate(oldest, newest, allInvoices);
	}

	@Override
	public List<Invoice> getInvoicesByType(InvoiceType type) {
		typeCheck(type);
		if (type == InvoiceType.EXPENSE) {
			return invoiceManager.getAllExpenses();
		} else {
			return invoiceManager.getAllIncomes();
		}
	}

	@Override
	public List<Invoice> getInvoicesByPerson(Long personId) {
		idCheck(personId);

		return invoiceManager.getAllForPerson(personId);
	}

	private void idCheck(Long id) {
		if (id == null) throw new IllegalArgumentException("Id cannot be null!");
		if (id < 0) throw new IllegalArgumentException("Id cannot be less than zero!");
	}

	private void typeCheck(InvoiceType type) {
		if (type == null) throw new IllegalArgumentException("Invoice type cannot be null!");
	}

	private void dateCheck(LocalDate oldest, LocalDate newest) {
		if (oldest == null || newest == null) throw new IllegalArgumentException("Dates parameters cannot be null!");
	}

	private void invoiceCheck(Invoice invoice) {
		if (invoice == null) throw new IllegalArgumentException("Invoice cannot be null!");
		if (invoice.getInvoiceType() == null) throw new IllegalArgumentException("Invoice type cannot be null!");
		if (invoice.getItems() == null) throw new IllegalArgumentException("Invoice items cannot be null!");
		if (invoice.getPayer() == null) throw new IllegalArgumentException("Invoice payer cannot be null!");
		if (invoice.getPrice() == null) throw new IllegalArgumentException("Invoice price cannot be null!");
		if (invoice.getRecipient() == null) throw new IllegalArgumentException("Invoice recipient cannot be null!");
		if (invoice.getDueDate() == null) throw new IllegalArgumentException("Invoice due date cannot be null!");
		if (invoice.getIssueDate() == null) throw new IllegalArgumentException("Invoice issue date cannot be null!");
		if (invoice.getPrice() < 0) throw new IllegalArgumentException("Invoice price cannot be less than zero!");
	}

	private List<Invoice> selectInvoicesByDate(LocalDate oldest, LocalDate newest, List<Invoice> invoices) {
		List<Invoice> satisfiableInvoices = new ArrayList<>();

		for (Invoice invoice: invoices) {

			if (invoice.getIssueDate().isBefore(newest) && invoice.getIssueDate().isAfter(oldest)) satisfiableInvoices.add(invoice);
		}
		return satisfiableInvoices;
	}

	private List<Invoice> selectInvoicesByPersonId(Long id, List<Invoice> invoices) {
		List<Invoice> satisfiableInvoices = new ArrayList<>();

		for (Invoice invoice: invoices) {

			if (invoice.getRecipient().getId().equals(id) || invoice.getPayer().getId().equals(id)) satisfiableInvoices.add(invoice);
		}
		return satisfiableInvoices;
	}
}
