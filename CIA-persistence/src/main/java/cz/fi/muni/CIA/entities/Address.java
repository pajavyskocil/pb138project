package cz.fi.muni.CIA.entities;

import java.util.Objects;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class Address {

	private String streetAddress;

	private String city;

	private String country;

	private String postCode;

	public Address(String streetAddress, String city, String country, String postCode) {
		this.streetAddress = streetAddress;
		this.city = city;
		this.country = country;
		this.postCode = postCode;
	}

	public Address() {
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Address)) return false;
		Address address = (Address) o;
		return Objects.equals(getStreetAddress(), address.getStreetAddress()) &&
				Objects.equals(getCity(), address.getCity()) &&
				Objects.equals(getCountry(), address.getCountry()) &&
				Objects.equals(getPostCode(), address.getPostCode());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getStreetAddress(), getCity(), getCountry(), getPostCode());
	}

	@Override
	public String toString() {
		return "Address{" +
				"streetAddress='" + streetAddress + '\'' +
				", city='" + city + '\'' +
				", country='" + country + '\'' +
				", postCode='" + postCode + '\'' +
				'}';
	}

	public String getPrettyAddress() {
		return streetAddress + ", " + city + ", " + postCode + ", " + country;
	}
}
