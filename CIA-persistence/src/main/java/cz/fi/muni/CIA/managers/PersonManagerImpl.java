package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.DbUtils;
import cz.fi.muni.CIA.Exceptions.PersonException;
import cz.fi.muni.CIA.entities.Configuration;
import cz.fi.muni.CIA.entities.Person;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.base.Collection;


import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
@Named
public class PersonManagerImpl implements PersonManager {
	private static final Logger logger = Logger.getLogger(PersonManagerImpl.class.getName());

	private Configuration configuration = DbUtils.loadConfig();

	private Collection collection;

	public PersonManagerImpl(Collection collection) {
		this.collection = collection;
	}

	@Override
	public void createPerson(Person person) {
		logger.log(Level.INFO, "Creating new Person: " + person.toString());
		try {
			if (person.getId() != null) {
				throw new PersonException("Person id is not null!");
			}
			person.setId(DbUtils.getPersonNextId(configuration, collection));

			String xQuery = "let $accounting := doc('" + configuration.getAccountingResourceName()+ "')" +
					"return update insert element person { " +
					"attribute pid {'" + person.getId()+ "'}, " +
					"element name {'" + person.getName() + "'}, " +
					"element email {'" + person.getEmail() + "'}, " +
					"element phone {'" + person.getPhoneNumber() + "'}, " +
					"element address {" +
					"element streetAddress {'" + person.getAddress().getStreetAddress() + "'}, " +
					"element city {'" + person.getAddress().getCity() + "'}, " +
					"element country {'" + person.getAddress().getCountry() + "'}, " +
					"element postalCode {'" + person.getAddress().getPostCode() + "'}" +
					"}, " +
					"element accountNumber {'" + person.getAccountNumber() + "'}" +
					"} into $accounting//addressBook";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiled = service.compile(xQuery);

			service.execute(compiled);
		} catch (XMLDBException ex){
			logger.log(Level.SEVERE, "XMLDBException during create new person: " + ex.getMessage());
			throw new PersonException("Error during create new person");
		}
	}

	@Override
	public void updatePerson(Person person) {
		logger.log(Level.INFO, "Updating Person with id: " + person.getId());
		try {
			String xQuery = "let $accounting := doc('" + configuration.getAccountingResourceName()+ "')" +
					"return update replace $accounting//addressBook/person[@pid='" + person.getId() + "'] with element person { " +
					"attribute pid {'" + person.getId()+ "'}, " +
					"element name {'" + person.getName() + "'}, " +
					"element email {'" + person.getEmail() + "'}, " +
					"element phone {'" + person.getPhoneNumber() + "'}, " +
					"element address {" +
					"element streetAddress {'" + person.getAddress().getStreetAddress() + "'}, " +
					"element city {'" + person.getAddress().getCity() + "'}, " +
					"element country {'" + person.getAddress().getCountry() + "'}, " +
					"element postalCode {'" + person.getAddress().getPostCode() + "'}" +
					"}, " +
					"element accountNumber {'" + person.getAccountNumber() + "'}" +
					"}";
			System.out.println(xQuery);
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiled = service.compile(xQuery);

			service.execute(compiled);
		} catch (XMLDBException ex){
			logger.log(Level.SEVERE, "XMLDBException during update person: " + ex.getMessage());
			throw new PersonException("Error during update person with id" + person.getId());
		}
	}

	@Override
	public void deletePerson(Long id) {
		logger.log(Level.INFO, "Deleting Person with id: " + id);

		try {
			String xQuery = "let $accounting := doc('" + configuration.getAccountingResourceName()+ "')" +
					"return update delete $accounting//addressBook/person[@pid='" + id + "']";
			System.out.println(xQuery);
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiled = service.compile(xQuery);

			service.execute(compiled);
		} catch (XMLDBException ex){
			logger.log(Level.SEVERE, "XMLDBException during delete person: " + ex.getMessage());
			throw new PersonException("Error during delete person with id: " + id);
		}
	}

	@Override
	public Person findById(Long id) {
		logger.log(Level.INFO, "Getting Person with id: " + id);
		try {
			String xQuery = "let $personList := doc('" + configuration.getAccountingResourceName() + "')" +
					"return $personList//person[@pid='" + id + "']";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiledExpression = service.compile(xQuery);
			ResourceSet resourceSet = service.execute(compiledExpression);
			ResourceIterator resourceIterator = resourceSet.getIterator();

			return DbUtils.personXMLtoPerson(resourceIterator.nextResource().getContent().toString());
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during find person by id: " + ex.getMessage());
			throw new PersonException("Error during find person by id with id: " + id);
		}
	}

	@Override
	public List<Person> getAll() {
		logger.log(Level.INFO, "Getting all persons");
		List<Person> personList = new ArrayList<>();
		try{
			String xQuery = "let $personList := doc('" + configuration.getAccountingResourceName() + "')" +
					"return $personList//person";
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			service.setProperty("encoding", "UTF-8");
			CompiledExpression compiledExpression = service.compile(xQuery);

			ResourceSet resourceSet = service.execute(compiledExpression);
			ResourceIterator resourceIterator = resourceSet.getIterator();

			while (resourceIterator.hasMoreResources()) {
				Resource resource = resourceIterator.nextResource();
				personList.add(DbUtils.personXMLtoPerson(resource.getContent().toString()));
			}
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during get all person: " + ex.getMessage());
			throw new PersonException("Error during get all persons");
		}
		return personList;
	}
}
