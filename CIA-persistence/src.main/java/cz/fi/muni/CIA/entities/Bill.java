package cz.fi.muni.CIA.entities;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class Bill {

	private Long id;

	private Person payer;

	private Person recipient;

	private LocalDate issueDate;

	private LocalDate dueDate;

	private Double price;

	private HashMap<String, Integer> items = new HashMap<>();

	private BillType billType;

	public Bill() {
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

	public BillType getBillType() {
		return billType;
	}

	public void setBillType(BillType billType) {
		this.billType = billType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Bill)) return false;
		Bill bill = (Bill) o;
		return Objects.equals(getPayer(), bill.getPayer()) &&
				Objects.equals(getRecipient(), bill.getRecipient()) &&
				Objects.equals(getIssueDate(), bill.getIssueDate()) &&
				Objects.equals(getDueDate(), bill.getDueDate()) &&
				Objects.equals(getPrice(), bill.getPrice()) &&
				Objects.equals(getItems(), bill.getItems()) &&
				getBillType() == bill.getBillType();
	}

	@Override
	public int hashCode() {

		return Objects.hash(getPayer(), getRecipient(), getIssueDate(), getDueDate(), getPrice(), getItems(), getBillType());
	}

	@Override
	public String toString() {
		return "Bill{" +
				"id=" + id +
				", payer=" + payer +
				", recipient=" + recipient +
				", issueDate=" + issueDate +
				", dueDate=" + dueDate +
				", price=" + price +
				", items=" + items +
				", billType=" + billType +
				'}';
	}
}
