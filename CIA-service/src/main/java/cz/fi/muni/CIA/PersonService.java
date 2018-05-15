package cz.fi.muni.CIA;

import cz.fi.muni.CIA.entities.Person;

import java.util.List;

/**
 * interface for Person service
 *
 * @author Peter Balcirak <peter.balcirak@gmail.com>
 */
public interface PersonService {

	/**
	 * Create person entity
	 *
	 * @param person
	 */
	void createPerson(Person person);

	/**
	 * Edit person entity
	 *
	 * @param person
	 */
	void editPerson(Person person);

	/**
	 * Delete person entity
	 *
	 * @param id
	 */
	void deletePerson(Long id);

	/**
	 * Get person entity by its id
	 *
	 * @param id
	 * @return Person with specified id
	 */
	Person getPersonById(Long id);

	/**
	 * Get all peron entities
	 *
	 * @return list of Person entities
	 */
	List<Person> getAllPersons();
}
