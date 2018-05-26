package cz.fi.muni.CIA.entities;

import java.util.Objects;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class Person {

	private Long id;

	private String name;

	private String email;

	private String phoneNumber;

	private Address address;

	private String accountNumber;

	private String xmlRepresentation;


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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getXmlRepresentation() {
		return xmlRepresentation;
	}

	public void setXmlRepresentation(String xmlRepresentation) {
		this.xmlRepresentation = xmlRepresentation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Person)) return false;
		Person person = (Person) o;
		return	Objects.equals(getName(), person.getName()) &&
				Objects.equals(getEmail(), person.getEmail()) &&
				Objects.equals(getPhoneNumber(), person.getPhoneNumber()) &&
				Objects.equals(getAddress(), person.getAddress()) &&
				Objects.equals(getAccountNumber(), person.getAccountNumber());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getName(), getEmail(), getPhoneNumber(), getAddress(), getAccountNumber());
	}

	@Override
	public String toString() {
		return "Person{" +
				" name='" + name + '\'' +
				", email='" + email + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", address=" + address +
				", accountNumber='" + accountNumber + '\'' +
				'}';
	}

}
