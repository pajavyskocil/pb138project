package cz.fi.muni.CIA.entities;

import java.util.Objects;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class Adress {

	private String streetAddress;

	private String city;

	private String country;

	private String postCode;

	public Adress() {
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
		if (!(o instanceof Adress)) return false;
		Adress adress = (Adress) o;
		return Objects.equals(getStreetAddress(), adress.getStreetAddress()) &&
				Objects.equals(getCity(), adress.getCity()) &&
				Objects.equals(getCountry(), adress.getCountry()) &&
				Objects.equals(getPostCode(), adress.getPostCode());
	}

	@Override
	public int hashCode() {

		return Objects.hash(getStreetAddress(), getCity(), getCountry(), getPostCode());
	}

	@Override
	public String toString() {
		return "Adress{" +
				"streetAddress='" + streetAddress + '\'' +
				", city='" + city + '\'' +
				", country='" + country + '\'' +
				", postCode='" + postCode + '\'' +
				'}';
	}
}
