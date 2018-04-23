package cz.fi.muni.CIA.entities;

import java.util.Objects;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class Person {

	private Long id;

	private String name;

	private String surname;

	private String address;

	private String accountNumber;

	public Person() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Person)) return false;
		Person person = (Person) o;
		return Objects.equals(getName(), person.getName()) &&
				Objects.equals(getSurname(), person.getSurname()) &&
				Objects.equals(getAddress(), person.getAddress()) &&
				Objects.equals(getAccountNumber(), person.getAccountNumber());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getName(), getSurname(), getAddress(), getAccountNumber());
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", address='" + address + '\'' +
				", accountNumber='" + accountNumber + '\'' +
				'}';
	}
}
