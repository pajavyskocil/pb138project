package cz.fi.muni.CIA;

import cz.fi.muni.CIA.Exceptions.DatabaseException;
import cz.fi.muni.CIA.Exceptions.ConfigException;
import cz.fi.muni.CIA.Exceptions.PersonException;
import cz.fi.muni.CIA.entities.Address;
import cz.fi.muni.CIA.entities.Invoice;
import cz.fi.muni.CIA.entities.Person;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class DbUtils {
	private static final Logger logger = Logger.getLogger(DbUtils.class.getName());

	private Properties properties = new Properties();

	private String dbUserName;
	private String dbUserPassword;
	private String dbDriver;
	private String dbPrefix;
	private String dbCollectionName;
	private String accountingResourceName;
	private String metadataResourceName;

	public DbUtils() {
		try {
			loadConfig();
			dbUserName = properties.getProperty("db_user_name");
			dbUserPassword = properties.getProperty("db_user_password");
			dbDriver = properties.getProperty("db_driver");
			dbPrefix = properties.getProperty("db_prefix");
			dbCollectionName = properties.getProperty("db_collectionName");
			accountingResourceName = properties.getProperty("db_accounting_resourceName");
			metadataResourceName = properties.getProperty("db_metadata_resourceName");
		} catch (ConfigException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			throw new InternalError(ex.getMessage());
		}
	}

	public Collection loadCollection() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			XMLDBException, DatabaseException{
		Database database = (Database) Class.forName(dbDriver).newInstance();
		database.setProperty("create-database", "true");
		DatabaseManager.registerDatabase(database);
		Collection collection =  DatabaseManager.getCollection(dbPrefix + dbCollectionName, dbUserName, dbUserPassword);
		if (collection == null) {
			logger.log(Level.WARNING, "Collection with name " + dbCollectionName + " does not exist");
			collection = createCollection();
		}
		if (collection.getResource(accountingResourceName) == null) {
			logger.log(Level.WARNING, "The collection does not have resource with name " + accountingResourceName);
			createAccountingResource(collection);
		}
		if (collection.getResource(metadataResourceName) == null) {
			logger.log(Level.WARNING, "The collection does not have resource with name " + metadataResourceName);
			createMetadataResource(collection);
		}

		return collection;

	}

	private Collection createCollection() throws XMLDBException, DatabaseException{
		Collection parent =  DatabaseManager.getCollection(dbPrefix, dbUserName, dbUserPassword);
		CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
		mgt.createCollection(dbCollectionName);
		parent.close();
		Collection collection =  DatabaseManager.getCollection(dbPrefix + dbCollectionName, dbUserName, dbUserPassword);

		createAccountingResource(collection);
		createMetadataResource(collection);

		return collection;
	}

	private void createAccountingResource(Collection collection) throws DatabaseException {
		logger.log(Level.INFO, "Creating resource with name " + accountingResourceName);
		try {
			XMLResource accountingResource = (XMLResource) collection.createResource(accountingResourceName, "XMLResource");
			accountingResource.setContent("<accounting><invoices></invoices><addressBook></addressBook></accounting>");
			collection.storeResource(accountingResource);
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "Problem during creating resource");
			throw new DatabaseException();
		}
	}

	private void createMetadataResource(Collection collection) throws DatabaseException {
		logger.log(Level.INFO, "Creating resource with name " + metadataResourceName);
		try {
			XMLResource metadataResource = (XMLResource) collection.createResource(metadataResourceName, "XMLResource");
			metadataResource.setContent("<metadata><invoice-next-id>1</invoice-next-id><person-next-id>1</person-next-id></metadata>");
			collection.storeResource(metadataResource);
		} catch (XMLDBException  ex) {
			logger.log(Level.SEVERE, "Problem during creating resource");
			throw new DatabaseException();
		}
	}

	private void loadConfig() throws ConfigException {
		logger.log(Level.INFO, "Loading config file.");
		InputStream inputStream = null;
		try{
			inputStream = DbUtils.class.getClassLoader().getResourceAsStream("config.conf");
			this.properties.load(inputStream);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					throw new ConfigException("Cannot load config properties from input stream.");
				}
			} else {
				throw new ConfigException("Cannot load config file.");
			}
		}
	}

	public void dropCollection() throws XMLDBException {
		Collection parent =  DatabaseManager.getCollection(dbPrefix, dbUserName, dbUserPassword);
		CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
		mgt.removeCollection(dbPrefix + dbCollectionName);
		parent.close();
	}

	public void incrementAccountingId(Collection collection) {
		String xQuery = "let $metadata := doc('" + metadataResourceName +"')" +
				"let $next-id := $metadata//invoice-next-id/text() + 1" +
				"return update replace $metadata//invoice-next-id/text() with $next-id";
		incrementId(collection, xQuery);
	}

	public void incrementPersonId(Collection collection) {
		String xQuery = "let $metadata := doc('" + metadataResourceName +"')" +
				"let $next-id := $metadata//person-next-id/text() + 1" +
				"return update replace $metadata//person-next-id/text() with $next-id";
		incrementId(collection, xQuery);

	}

	private void incrementId(Collection collection, String query) {
		try {
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			CompiledExpression compiledExpression = service.compile(query);
			service.execute(compiledExpression);
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}
	}

	public List<Person> getAllPerson(Collection collection) throws XMLDBException{
		List<Person> personList = new ArrayList<>();
		String xQuery = "let $personList := doc('" + accountingResourceName + "')" +
				"return $personList//person";
		XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
		service.setProperty("indent", "yes");
		CompiledExpression compiledExpression = service.compile(xQuery);

		ResourceSet resourceSet = service.execute(compiledExpression);
		ResourceIterator resourceIterator = resourceSet.getIterator();

		while (resourceIterator.hasMoreResources()) {
			Resource resource = resourceIterator.nextResource();
			personList.add(personXMLtoPerson(resource.getContent().toString()));
		}

		return personList;
	}

	private Person personXMLtoPerson(String personXML) {
		Person person = new Person();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(personXML));
			Document document = documentBuilder.parse(inputSource);
			NodeList nodeList = document.getElementsByTagName("person");
			Element element = (Element) nodeList.item(0);
			person.setId(Long.parseLong(element.getAttribute("pid")));

			if (element.getElementsByTagName("name").getLength() != 1) {
				logger.log(Level.SEVERE, "Error while parsing name!");
				throw new PersonException("Error while parsing name!");
			}
			person.setName(element.getElementsByTagName("name").item(0).getTextContent());

			if (element.getElementsByTagName("email").getLength() != 1) {
				logger.log(Level.SEVERE, "Error while parsing email!");
				throw new PersonException("Error while parsing email!");
			}
			person.setEmail(element.getElementsByTagName("email").item(0).getTextContent());

			Address address = new Address();
			if (element.getElementsByTagName("address").getLength() != 1) {
				logger.log(Level.SEVERE, "Error while parsing address!");
				throw new PersonException("Error while parsing address!");
			}
			Element addressElement = (Element) element.getElementsByTagName("address").item(0);
			if (addressElement.getElementsByTagName("streetAddress").getLength() != 1 ||
					addressElement.getElementsByTagName("city").getLength() != 1 ||
					addressElement.getElementsByTagName("country").getLength() != 1 ||
					addressElement.getElementsByTagName("postalCode").getLength() != 1) {
				logger.log(Level.SEVERE, "Error while parsing address!");
				throw new PersonException("Error while parsing address!");
			}
			address.setStreetAddress(addressElement.getElementsByTagName("streetAddress").item(0).getTextContent());
			address.setCity(addressElement.getElementsByTagName("city").item(0).getTextContent());
			address.setCountry(addressElement.getElementsByTagName("country").item(0).getTextContent());
			address.setPostCode(addressElement.getElementsByTagName("postalCode").item(0).getTextContent());
			person.setAddress(address);

			if (element.getElementsByTagName("accountNumber").getLength() != 1) {
				logger.log(Level.SEVERE, "Error while parsing accountNumber!");
				throw new PersonException("Error while parsing accountNumber!");
			}

			person.setAccountNumber(element.getElementsByTagName("accountNumber").item(0).getTextContent());

		} catch (ParserConfigurationException e) {
			logger.log(Level.SEVERE, "ParserConfigurationException: " + e.getMessage() );
		} catch (SAXException ex) {
			logger.log(Level.SEVERE, "SAXException: " + ex.getMessage() );
		} catch (IOException ioEx) {
			logger.log(Level.SEVERE, "IOException: " + ioEx.getMessage());
		}

		return person;
	}

	private Invoice invoiceXMLToInvoice(String InvoiceXML) {

	}
}
