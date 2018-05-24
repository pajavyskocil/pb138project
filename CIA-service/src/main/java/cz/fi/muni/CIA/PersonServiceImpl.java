package cz.fi.muni.CIA;

import cz.fi.muni.CIA.managers.PersonManager;
import cz.fi.muni.CIA.entities.Person;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * class for Person service implementation
 *
 * @author Peter Balcirak <peter.balcirak@gmail.com>
 */
@Service
public class PersonServiceImpl implements PersonService {

	private final PersonManager personManager;
	private final InvoiceService invoiceService;

	@Inject
	public PersonServiceImpl(PersonManager personManager, InvoiceService invoiceService) {
		this.personManager = personManager;
		this.invoiceService = invoiceService;
	}

	public void createPerson(Person person) {
		personCheck(person);
		if (person.getId() != null) throw new IllegalArgumentException("Persons id has to be be null!");
		personManager.createPerson(person);
	}

	public void editPerson(Person person) {
		personCheck(person);
		idCheck(person.getId());
		personManager.updatePerson(person);
	}

	public void deletePerson(Long id) {
		idCheck(id);
		if (!invoiceService.getInvoicesByPersonId(id).isEmpty()) throw new IllegalArgumentException("It's not possible to delete person who is figuring in som invoice!");
		personManager.deletePerson(id);
	}

	public Person getPersonById(Long id) {
		idCheck(id);
		return personManager.findById(id);
	}

	public List<Person> getAllPersons() {
		return personManager.getAll();
	}

	private void personCheck(Person person) {
		if (person.getName() == null) throw new IllegalArgumentException("Person name cannot be null!");
		if (person.getEmail() == null) throw new IllegalArgumentException("Person email cannot be null!");
		if (person.getAccountNumber() == null) throw new IllegalArgumentException("Person account number cannot be null!");
		if (person.getPhoneNumber() == null) throw new IllegalArgumentException("Person phone number cannot be null!");
		if (person.getAddress() == null) throw new IllegalArgumentException("Person address cannot be null!");
		if (person.getAddress().getCity() == null) throw new IllegalArgumentException("Person city cannot be null!");
		if (person.getAddress().getCountry() == null) throw new IllegalArgumentException("Person country cannot be null!");
		if (person.getAddress().getPostCode() == null) throw new IllegalArgumentException("Person post code cannot be null!");
		if (person.getAddress().getStreetAddress() == null) throw new IllegalArgumentException("Person street address cannot be null!");
	}

	private void idCheck(Long id) {
		if (id == null) throw new IllegalArgumentException("Id cannot be null!");
		if (id < 0) throw new IllegalArgumentException("Id cannot be less than zero!");
	}
}
