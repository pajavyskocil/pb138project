package cz.fi.muni.CIA;

import cz.fi.muni.CIA.entities.Address;
import cz.fi.muni.CIA.entities.Person;
import cz.fi.muni.CIA.managers.InvoiceManager;
import cz.fi.muni.CIA.managers.PersonManager;
import org.testng.annotations.Test;
import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * class PersonServiceUnitTest
 *
 * @author Peter Balcirak <peter.balcirak@gmail.com>
 */
public class PersonServiceUnitTest {

	private PersonManager personManager = mock(PersonManager.class);
	private InvoiceService invoiceService = mock(InvoiceService.class);

	@InjectMocks
	private PersonService personService;

	private Person personOne;
	private Person personTwo;
	private Person personThree;

	@BeforeMethod
	public void setService() {
		reset(personManager);
		reset(invoiceService);
		personService = new PersonServiceImpl(personManager, invoiceService);
	}

	@BeforeMethod
	public void setEntities() {
		Address address = new Address();
		address.setCity("malinova");
		address.setCountry("SR");
		address.setPostCode("61400");
		address.setStreetAddress("none");

		personOne = new Person();
		personOne.setEmail("emailOne");
		personOne.setAccountNumber("1111");
		personOne.setPhoneNumber("11111");
		personOne.setName("peter");
		personOne.setAddress(address);

		personTwo = new Person();
		personTwo.setId(2L);
		personTwo.setEmail("emailTwo");
		personTwo.setAccountNumber("222");
		personTwo.setPhoneNumber("2222");
		personTwo.setName("lucia");
		personTwo.setAddress(address);

		personThree = new Person();
		personThree.setEmail("emailThree");
		personThree.setAccountNumber("333");
		personThree.setPhoneNumber("3333");
		personThree.setName("lenka");
		personThree.setAddress(address);

	}

	@Test
	public void testCreatePerson() {
		personService.createPerson(personOne);

		verify(personManager, times(1)).createPerson(personOne);
	}

	@Test
	public void testGetAll() {

		personService.getAllPersons();

		verify(personManager, times(1)).getAll();
	}

	@Test
	public void testDeletePerson() {
		personService.deletePerson(1L);

		verify(invoiceService, times(1)).getInvoicesByPersonId(1L);
		verify(personManager, times(1)).deletePerson(1L);
	}

	@Test
	public void testGetPersonById() {
		personService.getPersonById(1L);

		verify(personManager, times(1)).findById(1L);
	}

	@Test
	public void testEditPerson() {
		personService.editPerson(personTwo);

		verify(personManager, times(1)).updatePerson(personTwo);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testEditPersonWithoutId() {
		personService.editPerson(personOne);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testGetPersonByNegativeId() {
		personService.getPersonById(-1L);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testDeletePersonWithNullId() {
		personService.deletePerson(null);
	}

	@Test (expectedExceptions=IllegalArgumentException.class)
	public void testCreatePersonWithoutAddress() {
		personOne.setAddress(null);
		personService.createPerson(personOne);
	}
}
