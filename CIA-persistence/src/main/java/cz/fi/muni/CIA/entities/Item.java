package cz.fi.muni.CIA.entities;

import java.util.Objects;

/**
 * Item in Invoice
 *
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class Item {

	private String name;

	private String Description;

	private Integer count;

	private Double price;

	private Double totalPrice;


	public Item() {
	}

	public Item(String name, String description, Integer count, Double price, Double totalPrice) {
		this.name = name;
		Description = description;
		this.count = count;
		this.price = price;
		this.totalPrice = totalPrice;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item)) return false;
		Item item = (Item) o;
		return Objects.equals(getName(), item.getName()) &&
				Objects.equals(getDescription(), item.getDescription()) &&
				Objects.equals(getCount(), item.getCount()) &&
				Objects.equals(getPrice(), item.getPrice()) &&
				Objects.equals(getTotalPrice(), item.getTotalPrice());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getName(), getDescription(), getCount(), getPrice(), getTotalPrice());
	}

	@Override
	public String toString() {
		return "Item{" +
				"name='" + name + '\'' +
				", Description='" + Description + '\'' +
				", count=" + count +
				", price=" + price +
				", totalPrice=" + totalPrice +
				'}';
	}
}
