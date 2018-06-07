package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.DbUtils;
import cz.fi.muni.CIA.Exceptions.InvoiceException;
import cz.fi.muni.CIA.Exceptions.PersonException;
import cz.fi.muni.CIA.entities.Configuration;
import cz.fi.muni.CIA.entities.Invoice;
import cz.fi.muni.CIA.entities.InvoiceType;
import cz.fi.muni.CIA.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of InvoiceManager
 *
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
@Named
public class InvoiceManagerImpl implements InvoiceManager {

	private static final Logger logger = Logger.getLogger(InvoiceManagerImpl.class.getName());

	@Autowired
	private Configuration configuration;

	@Autowired
	private Collection collection;

	@Override
	public void addInvoice(Invoice invoice) {
		logger.log(Level.INFO, "Adding new invoice: " + invoice.toString());

		try{
			if (invoice.getId() != null) {
				throw new InvoiceException("Invoice id is not null!");
			}

			invoice.setId(DbUtils.getInvoiceNextId(configuration, collection));
			String itemsQuery = getItemsQuery(invoice.getItems());

			String xQuery = "let $accounting := doc('" + configuration.getAccountingResourceName() + "')" +
					"return update insert element invoice { " +
					"attribute id {'" + invoice.getId() + "'}, " +
					"attribute type {'" + invoice.getInvoiceType().toString().toLowerCase() + "'}, " +
					"element payerID {'" + invoice.getPayer().getId() + "'}, " +
					"element recipientID {'" + invoice.getRecipient().getId() + "'}, " +
					"element issueDate {'" + invoice.getIssueDate().toString() + "'}, " +
					"element dueDate {'" + invoice.getDueDate().toString() + "'}, " +
					"element totalPrice {'" + String.format("%.2f",invoice.getPrice()) + "'}, " +
					"element items {" + itemsQuery + "} " +
					"} into $accounting//invoices";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiled = service.compile(xQuery);

			service.execute(compiled);
			} catch (XMLDBException ex) {
				logger.log(Level.SEVERE, "XMLDBException during add new invoice: " + ex.getMessage());
				throw new InvoiceException("Error during add new invoice");
			}
	}

	@Override
	public void deleteInvoice(Long id) {
		logger.log(Level.INFO, "Delete invoice with id: " + id);
		try {
			String xQuery = "let $accounting := doc('" + configuration.getAccountingResourceName()+ "')" +
					"return update delete $accounting//invoices/invoice[@id='" + id + "']";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiled = service.compile(xQuery);

			service.execute(compiled);
		} catch (XMLDBException ex){
			logger.log(Level.SEVERE, "XMLDBException during delete invoice: " + ex.getMessage());
			throw new InvoiceException("Error during delete invoice with id: " + id);
		}
	}

	@Override
	public void updateInvoice(Invoice invoice) {
		logger.log(Level.INFO, "Updating invoice with id: " + invoice.getId());
		try{
			String itemsQuery = getItemsQuery(invoice.getItems());

			String xQuery = "let $accounting := doc('" + configuration.getAccountingResourceName() + "')" +
					"return update replace $accounting//invoices/invoice[@id='" + invoice.getId() + "'] with element invoice { " +
					"attribute id {'" + invoice.getId() + "'}, " +
					"attribute type {'" + invoice.getInvoiceType().toString().toLowerCase() + "'}, " +
					"element payerID {'" + invoice.getPayer().getId() + "'}, " +
					"element recipientID {'" + invoice.getRecipient().getId() + "'}, " +
					"element issueDate {'" + invoice.getIssueDate().toString() + "'}, " +
					"element dueDate {'" + invoice.getDueDate().toString() + "'}, " +
					"element totalPrice {'" + String.format("%.2f",invoice.getPrice()) + "'}, " +
					"element items {" + itemsQuery + "}}";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiled = service.compile(xQuery);

			service.execute(compiled);
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during update invoice: " + ex.getMessage());
			throw new InvoiceException("Error during update invoice with id: " + invoice.getId());
		}
	}

	@Override
	public Invoice findById(Long id) {
		logger.log(Level.INFO, "Getting Invoice with id: " + id);
		try {
			String xQuery = "let $invoiceList := doc('" + configuration.getAccountingResourceName() + "')" +
					"return $invoiceList//invoice[@id='" + id + "']";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiledExpression = service.compile(xQuery);
			ResourceSet resourceSet = service.execute(compiledExpression);
			ResourceIterator resourceIterator = resourceSet.getIterator();

			return DbUtils.invoiceXMLToInvoice(resourceIterator.nextResource().getContent().toString());
		} catch (XMLDBException ex){
			logger.log(Level.SEVERE, "XMLDBException during get invoice by id: " + ex.getMessage());
			throw new InvoiceException("Error during get invoice with id: " + id);
		}
	}

	@Override
	public List<Invoice> getAll() {
		logger.log(Level.INFO, "Getting all invoices");
		List<Invoice> invoiceList = new ArrayList<>();
		try {
			String xQuery = "let $invoiceList := doc('" + configuration.getAccountingResourceName() + "')" +
					"return $invoiceList//invoice";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiledExpression = service.compile(xQuery);
			ResourceSet resourceSet = service.execute(compiledExpression);
			ResourceIterator resourceIterator = resourceSet.getIterator();

			while (resourceIterator.hasMoreResources()) {
				Resource resource = resourceIterator.nextResource();
				invoiceList.add(DbUtils.invoiceXMLToInvoice(resource.getContent().toString()));
			}
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during get all invoices: " + ex.getMessage());
			throw new InvoiceException("Error during get all invoices");
		}
		return invoiceList;
	}

	@Override
	public List<Invoice> getAllIncomes() {
		return getAllForType(InvoiceType.INCOME);
	}

	@Override
	public List<Invoice> getAllExpenses() {
		return getAllForType(InvoiceType.EXPENSE);
	}

	private List<Invoice> getAllForType(InvoiceType type) {
		logger.log(Level.INFO, "Getting all invoices with type: " + type);
		List<Invoice> invoiceList = new ArrayList<>();
		try{
			String xQuery = "let $invoiceList := doc('" + configuration.getAccountingResourceName() + "')" +
					"return $invoiceList//invoice[@type='" + type.toString().toLowerCase() + "']";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiledExpression = service.compile(xQuery);
			ResourceSet resourceSet = service.execute(compiledExpression);
			ResourceIterator resourceIterator = resourceSet.getIterator();

			while (resourceIterator.hasMoreResources()) {
				Resource resource = resourceIterator.nextResource();
				invoiceList.add(DbUtils.invoiceXMLToInvoice(resource.getContent().toString()));
			}
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during get all invoices in type: "+ type + " " + ex.getMessage());
			throw new InvoiceException("Error during get all invoices in type: " + type);
		}
		return invoiceList;
	}

	@Override
	public List<Invoice> getAllForPerson(Long personId) {
		logger.log(Level.INFO, "Getting all invoices with person as payer or recipient: " + personId);
		List<Invoice> invoiceList = new ArrayList<>();
		try{
			String xQuery = "let $invoiceList := doc('" + configuration.getAccountingResourceName() + "')" +
					"return $invoiceList//invoice[payerID='" + personId + "' or recipientID=' " + personId + "']";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiledExpression = service.compile(xQuery);
			ResourceSet resourceSet = service.execute(compiledExpression);
			ResourceIterator resourceIterator = resourceSet.getIterator();

			while (resourceIterator.hasMoreResources()) {
				Resource resource = resourceIterator.nextResource();
				invoiceList.add(DbUtils.invoiceXMLToInvoice(resource.getContent().toString()));
			}
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during get all invoices for person: "+ personId + " " + ex.getMessage());
			throw new InvoiceException("Error during get all invoices for person: " + personId);
		}
		return invoiceList;
	}

	private String getItemsQuery(Set<Item> items) {
		StringBuilder itemsQuery = new StringBuilder();

		for (Item item:items) {
			if (!itemsQuery.toString().equals("")) {
				itemsQuery.append(", ");
			}
			String itemQuery = "element item {" +
					"element name {'" + item.getName() + "'}, " +
					"element description {'" + item.getDescription() + "'} ," +
					"element price {'" + String.format("%.2f", item.getPrice()) + "'} ," +
					"element count {'" + item.getCount() + "'} ," +
					"element totalPrice{'" + String.format("%.2f", item.getTotalPrice()) + "'}" +
					"}";
			itemsQuery.append(itemQuery);
		}
		return itemsQuery.toString();
	}
}
