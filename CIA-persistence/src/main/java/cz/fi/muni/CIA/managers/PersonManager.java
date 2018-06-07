package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Person;

import java.util.List;

/**
 * Manager for working with Person object
 *
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public interface PersonManager {

	void createPerson(Person person);

	void updatePerson(Person person);

	void deletePerson(Long id);

	Person findById(Long id);

	List<Person> getAll();
}
