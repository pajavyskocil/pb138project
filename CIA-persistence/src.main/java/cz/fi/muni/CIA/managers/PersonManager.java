package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Person;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public interface PersonManager {

	void createPerson(Person person);

	void updatePerson(Person person);

	void deletePerson(Person person);

	void findById(Long id);

	void getAll();
}
