package cz.fi.muni.CIA.entities;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class Invoice {

	private Long id;

	private Person payer;

	private Person recipient;

	private LocalDate issueDate;

	private LocalDate dueDate;

	private Double price;

	private HashMap<String, Integer> items = new HashMap<>();

	private InvoiceType invoiceType;

	public Invoice() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getPayer() {
		return payer;
	}

	public void setPayer(Person payer) {
		this.payer = payer;
	}

	public Person getRecipient() {
		return recipient;
	}

	public void setRecipient(Person recipient) {
		this.recipient = recipient;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public HashMap<String, Integer> getItems() {
		return items;
	}

	public void setItems(HashMap<String, Integer> items) {
		this.items = items;
	}

	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Invoice)) return false;
		Invoice invoice = (Invoice) o;
		return Objects.equals(getPayer(), invoice.getPayer()) &&
				Objects.equals(getRecipient(), invoice.getRecipient()) &&
				Objects.equals(getIssueDate(), invoice.getIssueDate()) &&
				Objects.equals(getDueDate(), invoice.getDueDate()) &&
				Objects.equals(getPrice(), invoice.getPrice()) &&
				Objects.equals(getItems(), invoice.getItems()) &&
				getInvoiceType() == invoice.getInvoiceType();
	}

	@Override
	public int hashCode() {

		return Objects.hash(getPayer(), getRecipient(), getIssueDate(), getDueDate(), getPrice(), getItems(), getInvoiceType());
	}

	@Override
	public String toString() {
		return "Invoice{" +
				"id=" + id +
				", payer=" + payer +
				", recipient=" + recipient +
				", issueDate=" + issueDate +
				", dueDate=" + dueDate +
				", price=" + price +
				", items=" + items +
				", invoiceType=" + invoiceType +
				'}';
	}
}
