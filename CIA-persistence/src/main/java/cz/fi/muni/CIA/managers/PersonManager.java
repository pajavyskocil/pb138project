package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Person;

import java.util.List;

/**
 * Manager for working with Person object
 *
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public interface PersonManager {

	/**
	 * This method creates given person
	 * @param person Person to create
	 */
	void createPerson(Person person);

	/**
	 * This method updates given person
	 * @param person Person to update
	 */
	void updatePerson(Person person);

	/**
	 * This method delete person with given Id
	 * @param id Id of person
	 */
	void deletePerson(Long id);

	/**
	 * This method returns Person with given Id
	 * @param id Id of person
	 * @return Person with given Id
	 */
	Person findById(Long id);

	/**
	 * This method returns all Persons
	 * @return array of all Persons
	 */
	List<Person> getAll();
}
