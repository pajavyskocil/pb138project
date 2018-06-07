package cz.fi.muni.CIA;

import cz.fi.muni.CIA.Exceptions.*;
import cz.fi.muni.CIA.entities.*;
import cz.fi.muni.CIA.managers.OwnerManager;
import cz.fi.muni.CIA.managers.OwnerManagerImpl;
import cz.fi.muni.CIA.managers.PersonManager;
import cz.fi.muni.CIA.managers.PersonManagerImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;


import javax.xml.bind.ValidationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper methods for working with database
 *
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class DbUtils {

	private static final Logger logger = Logger.getLogger(DbUtils.class.getName());

	public DbUtils() {
	}

	/**
	 * Load collection
	 * @param configuration describing connection to DB
	 * @return loaded collection
	 */
	public static Collection loadCollection(Configuration configuration) {
		logger.log(Level.INFO, "Loading collection with name " + configuration.getDbCollectionName());
		Collection collection;
		boolean init = false;
		try {
			Database database = (Database) Class.forName(configuration.getDbDriver()).newInstance();
			database.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(database);
			collection =  DatabaseManager.getCollection(configuration.getDbPrefix() + configuration.getDbCollectionName(), configuration.getDbUserName(), configuration.getDbUserPassword());
			if (collection == null) {
				logger.log(Level.WARNING, "Collection with name " + configuration.getDbCollectionName() + " does not exist");
				collection = createCollection(configuration);
			}
			if (collection.getResource(configuration.getAccountingResourceName()) == null && collection.getResource(configuration.getMetadataResourceName()) == null) {
				logger.log(Level.WARNING, "The collection does not have resources");
				createAccountingResource(configuration, collection);
				createMetadataResource(configuration, collection);
				init = true;
			} else if (collection.getResource(configuration.getAccountingResourceName()) == null || collection.getResource(configuration.getMetadataResourceName()) == null) {
				logger.log(Level.WARNING, "One of the resource is missing.");
				throw new DatabaseException("One of the resource is missing.");
			}

			if (!init) {
				if (!validate(collection.getResource(configuration.getAccountingResourceName()).getContent().toString())) {
					logger.log(Level.SEVERE, "Resource is not valid");
					throw new ValidationException("Resource is not valid");
				}
			}
			logger.log(Level.INFO, "Resource is valid");

		} catch (ClassNotFoundException ex) {
			logger.log(Level.SEVERE, "ClassNotFoundException during load collection: " + ex.getMessage());
			throw new DatabaseException("Error during load collection");
		} catch (InstantiationException ex) {
			logger.log(Level.SEVERE, "InstantiationException during load collection: " + ex.getMessage());
			throw new DatabaseException("Error during load collection");
		} catch (IllegalAccessException ex) {
			logger.log(Level.SEVERE, "IllegalAccessException during load collection: " + ex.getMessage());
			throw new DatabaseException("Error during load collection");
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during load collection: " + ex.getMessage());
			throw new DatabaseException("Error during load collection");
		} catch (ValidationException ex) {
			logger.log(Level.SEVERE, "ValidationException during load collection: " + ex.getMessage());
			throw new DatabaseException("Error during load collection");
		}

		return collection;
	}

	/**
	 * Create collection
	 * @param configuration describing connection to DB
	 * @return created collection
	 */
	private static Collection createCollection(Configuration configuration) {
		logger.log(Level.INFO, "Creating collection with name " + configuration.getDbCollectionName());
		Collection collection;
		try {
			Collection parent =  DatabaseManager.getCollection(configuration.getDbPrefix(), configuration.getDbUserName(), configuration.getDbUserPassword());
			CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
			mgt.createCollection(configuration.getDbCollectionName());
			parent.close();
			collection =  DatabaseManager.getCollection(configuration.getDbPrefix() + configuration.getDbCollectionName(), configuration.getDbUserName(), configuration.getDbUserPassword());

			createAccountingResource(configuration, collection);
			createMetadataResource(configuration, collection);
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "Problem during create collection: " + ex.getMessage());
			throw new DatabaseException("Problem during create collection with name " + configuration.getDbCollectionName());
		}
		return collection;
	}

	/**
	 * Create resource for accounting collection
	 * @param configuration describing db
	 * @param collection where resource should be stored
	 */
	private static void createAccountingResource(Configuration configuration, Collection collection) {
		logger.log(Level.INFO, "Creating resource with name " + configuration.getAccountingResourceName());
		try {
			XMLResource accountingResource = (XMLResource) collection.createResource(configuration.getAccountingResourceName(), "XMLResource");
			accountingResource.setContent("<accounting><invoices></invoices><addressBook></addressBook></accounting>");
			collection.storeResource(accountingResource);
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "Problem during create resource: " + ex.getMessage());
			throw new DatabaseException("Problem during create resource with name " + configuration.getAccountingResourceName());
		}
	}

	/**
	 * Create resource for metadata collection
	 * @param configuration describing db
	 * @param collection where resource should be stored
	 */
	private static  void createMetadataResource(Configuration configuration, Collection collection) {
		logger.log(Level.INFO, "Creating resource with name " + configuration.getMetadataResourceName());
		try {
			XMLResource metadataResource = (XMLResource) collection.createResource(configuration.getMetadataResourceName(), "XMLResource");
			metadataResource.setContent("<metadata><invoice-next-id>1</invoice-next-id><person-next-id>1</person-next-id></metadata>");
			collection.storeResource(metadataResource);
		} catch (XMLDBException  ex) {
			logger.log(Level.SEVERE, "Problem during create resource: " + ex.getMessage());
			throw new DatabaseException("Problem during create resource with name " + configuration.getMetadataResourceName());
		}
	}

	/**
	 * Drop collection
	 * @param configuration configuration describing db
	 */
	public static void dropCollection(Configuration configuration) {
		logger.log(Level.INFO, "Dropping collection with name " + configuration.getDbCollectionName());
		try {
			Collection parent =  DatabaseManager.getCollection(configuration.getDbPrefix(), configuration.getDbUserName(), configuration.getDbUserPassword());
			CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
			mgt.removeCollection(configuration.getDbPrefix() + configuration.getDbCollectionName());
			parent.close();
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "Problem during drop collection: " + ex.getMessage());
			throw new DatabaseException("Problem during drop collection with name " + configuration.getDbCollectionName());
		}

	}

	/**
	 * Get next ID for Invoice
	 * @param configuration describing db
	 * @param collection where ID should be incremented
	 * @return next ID
	 */
	public static Long getInvoiceNextId(Configuration configuration, Collection collection) {
		String xQuery = "let $metadata := doc('" + configuration.getMetadataResourceName() +"')" +
				"return $metadata//invoice-next-id/text()";
		Long id =  getNextId(collection, xQuery);
		incrementInvoiceId(configuration, collection);
		return id;
	}

	/**
	 * Get next ID for Person
	 * @param configuration describing db
	 * @param collection where ID should be incremented
	 * @return next ID
	 */
	public static Long getPersonNextId(Configuration configuration, Collection collection) {
		String xQuery = "let $metadata := doc('" + configuration.getMetadataResourceName() +"')" +
				"return $metadata//person-next-id/text()";
		Long id = getNextId(collection, xQuery);
		incrementPersonId(configuration, collection);
		return id;
	}

	/**
	 * Convert XML to Person
	 * @param personXML string from DB
	 * @return parsed Person object
	 */
	public static Person personXMLtoPerson(String personXML) {
		Person person = new Person();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(personXML));
			Document document = documentBuilder.parse(inputSource);
			NodeList nodeList = document.getElementsByTagName("person");
			Element element = (Element) nodeList.item(0);
			Element addressElement = (Element) element.getElementsByTagName("address").item(0);

			person.setXmlRepresentation(personXML);
			person.setId(Long.parseLong(element.getAttribute("pid")));
			person.setName(element.getElementsByTagName("name").item(0).getTextContent());
			person.setEmail(element.getElementsByTagName("email").item(0).getTextContent());
			person.setPhoneNumber(element.getElementsByTagName("phone").item(0).getTextContent());
			person.setAddress(addressXMLToAddress(addressElement));
			person.setAccountNumber(element.getElementsByTagName("accountNumber").item(0).getTextContent());
		} catch (ParserConfigurationException ex) {
			logger.log(Level.SEVERE, "ParserConfigurationException during convert personXML to Person: " + ex.getMessage() );
			throw new PersonException("Error during parsing XML to Person");
		} catch (SAXException ex) {
			logger.log(Level.SEVERE, "SAXException during convert personXML to Person: " + ex.getMessage() );
			throw new PersonException("Error during parsing XML to Person");
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "IOException during convert personXML to Person: " + ex.getMessage());
			throw new PersonException("Error during parsing XML to Person");
		}
		return person;
	}

	/**
	 * Convert XML to Owner
	 * @param ownerXML string from DB
	 * @return parsed Owner object
	 */
	public static Owner ownerXMLToOwner(String ownerXML) {
		Owner owner = new Owner();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(ownerXML));
			Document document = documentBuilder.parse(inputSource);
			NodeList nodeList = document.getElementsByTagName("owner");
			Element element = (Element) nodeList.item(0);
			Element addressElement = (Element) element.getElementsByTagName("address").item(0);

			owner.setXmlRepresentation(ownerXML);
			owner.setId(Long.parseLong(element.getAttribute("pid")));
			owner.setName(element.getElementsByTagName("name").item(0).getTextContent());
			owner.setEmail(element.getElementsByTagName("email").item(0).getTextContent());
			owner.setPhoneNumber(element.getElementsByTagName("phone").item(0).getTextContent());
			owner.setAddress(addressXMLToAddress(addressElement));
			owner.setAccountNumber(element.getElementsByTagName("accountNumber").item(0).getTextContent());
			owner.setLogoBASE64(element.getElementsByTagName("logo").item(0).getTextContent());
		} catch (ParserConfigurationException ex) {
			logger.log(Level.SEVERE, "ParserConfigurationException during convert ownerXML to Owner: " + ex.getMessage() );
			throw new OwnerException("Error during parsing XML to Owner");
		} catch (SAXException ex) {
			logger.log(Level.SEVERE, "SAXException during convert ownerXML to Owner: " + ex.getMessage() );
			throw new OwnerException("Error during parsing XML to Owner");
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "IOException during convert ownerXML to Owner: " + ex.getMessage());
			throw new OwnerException("Error during parsing XML to Owner");
		}
		return owner;

	}

	/**
	 * Convert XML to Invoice
	 * @param invoiceXML string from DB
	 * @return parsed Invoice object
	 */
	public static Invoice invoiceXMLToInvoice(String invoiceXML) {
		Invoice invoice = new Invoice();
		PersonManager personManager = new PersonManagerImpl();
		OwnerManager ownerManager = new OwnerManagerImpl();

		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(invoiceXML));
			Document document = documentBuilder.parse(inputSource);
			NodeList nodeList = document.getElementsByTagName("invoice");
			Element element = (Element) nodeList.item(0);
			invoice.setId(Long.parseLong(element.getAttribute("id")));

			if (element.getAttribute("type").equalsIgnoreCase(InvoiceType.INCOME.toString())) {
				invoice.setInvoiceType(InvoiceType.INCOME);
				invoice.setPayer(personManager.findById(Long.parseLong(element.getElementsByTagName("payerID").item(0).getTextContent())));
				invoice.setRecipient(ownerManager.getOwner());
			} else {
				invoice.setInvoiceType(InvoiceType.EXPENSE);
				invoice.setRecipient(personManager.findById(Long.parseLong(element.getElementsByTagName("recipientID").item(0).getTextContent())));
				invoice.setPayer(ownerManager.getOwner());
			}

			invoice.setIssueDate(LocalDate.parse(element.getElementsByTagName("issueDate").item(0).getTextContent()));
			invoice.setDueDate(LocalDate.parse(element.getElementsByTagName("dueDate").item(0).getTextContent()));
			invoice.setPrice(Double.parseDouble(element.getElementsByTagName("totalPrice").item(0).getTextContent()));
			invoice.setXmlRepresentation(invoiceXML);

			Element itemsElement = (Element) element.getElementsByTagName("items").item(0);
			for ( int i = 0; i < itemsElement.getElementsByTagName("item").getLength(); i++) {
				Element itemElement = (Element) element.getElementsByTagName("item").item(i);
				invoice.addItem(itemXMLToItem(itemElement));
			}
		} catch (ParserConfigurationException ex) {
			logger.log(Level.SEVERE, "ParserConfigurationException during convert invoice to Invoice:  " + ex.getMessage() );
			throw new InvoiceException("Error during parsing XML to Invoice");
		} catch (SAXException ex) {
			logger.log(Level.SEVERE, "SAXException during convert invoice to Invoice: " + ex.getMessage() );
			throw new InvoiceException("Error during parsing XML to Invoice");
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "IOException during convert invoice to Invoice: " + ex.getMessage());
			throw new InvoiceException("Error during parsing XML to Invoice");
		}
		return invoice;
	}

	/**
	 * Load configuration from file
	 * @return parsed Configuration object
	 */
	public static Configuration loadConfig() {
		logger.log(Level.INFO, "Loading config file.");
		Properties properties = new Properties();
		Configuration configuration = new Configuration();
		try (InputStream inputStream = DbUtils.class.getClassLoader().getResourceAsStream("config.conf")){
			properties.load(inputStream);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		configuration.setDbUserName(properties.getProperty("db_user_name"));
		configuration.setDbUserPassword(properties.getProperty("db_user_password"));
		configuration.setDbPrefix(properties.getProperty("db_prefix"));
		configuration.setDbDriver(properties.getProperty("db_driver"));
		configuration.setDbCollectionName(properties.getProperty("db_collectionName"));
		configuration.setAccountingResourceName(properties.getProperty("db_accounting_resourceName"));
		configuration.setMetadataResourceName(properties.getProperty("db_metadata_resourceName"));

		return configuration;
	}

	private static void incrementInvoiceId(Configuration configuration, Collection collection) {
		String xQuery = "let $metadata := doc('" + configuration.getMetadataResourceName() +"')" +
				"let $next-id := $metadata//invoice-next-id/text() + 1" +
				"return update replace $metadata//invoice-next-id/text() with $next-id";
		incrementId(collection, xQuery);
	}

	private static void incrementPersonId(Configuration configuration, Collection collection) {
		String xQuery = "let $metadata := doc('" + configuration.getMetadataResourceName() +"')" +
				"let $next-id := $metadata//person-next-id/text() + 1" +
				"return update replace $metadata//person-next-id/text() with $next-id";
		incrementId(collection, xQuery);
	}

	private static void incrementId(Collection collection, String query) {
		try {
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			CompiledExpression compiledExpression = service.compile(query);
			service.execute(compiledExpression);
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during increment id: " + ex.getMessage());
			throw new DatabaseException("Error during increment id");
		}
	}

	private static Long getNextId(Collection collection, String query) {
		try {
			XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
			service.setProperty("indent", "yes");
			CompiledExpression compiledExpression = service.compile(query);
			ResourceSet resourceSet =  service.execute(compiledExpression);
			return Long.parseLong(resourceSet.getIterator().nextResource().getContent().toString());
		} catch (XMLDBException ex) {
			logger.log(Level.SEVERE, "XMLDBException during get next id: " + ex.getMessage());
			throw new DatabaseException("Error during get next id");
		}
	}

	private static Address addressXMLToAddress(Element addressElement) {
		Address address = new Address();
		address.setStreetAddress(addressElement.getElementsByTagName("streetAddress").item(0).getTextContent());
		address.setCity(addressElement.getElementsByTagName("city").item(0).getTextContent());
		address.setCountry(addressElement.getElementsByTagName("country").item(0).getTextContent());
		address.setPostCode(addressElement.getElementsByTagName("postalCode").item(0).getTextContent());

		return address;
	}

	private static Item itemXMLToItem(Element itemElement) {
		Item item = new Item();
		item.setName(itemElement.getElementsByTagName("name").item(0).getTextContent());
		item.setDescription(itemElement.getElementsByTagName("description").item(0).getTextContent());
		item.setCount(Integer.parseInt(itemElement.getElementsByTagName("count").item(0).getTextContent()));
		item.setPrice(Double.parseDouble(itemElement.getElementsByTagName("price").item(0).getTextContent()));
		item.setTotalPrice(Double.parseDouble(itemElement.getElementsByTagName("totalPrice").item(0).getTextContent()));

		return item;
	}

	private static boolean validate(String inputXml) {
		// build the schema
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		try {
			Schema schema = factory.newSchema(new File(".XML/cia-schema.xsd"));
			Validator validator = schema.newValidator();
			Source source = new StreamSource(new StringReader(inputXml));
			validator.validate(source);
		} catch (SAXException e) {
			logger.log(Level.SEVERE, "SAXException thrown in validate: " + e.getMessage());
			return false;
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IOException thrown in validate: " + e.getMessage());
			return false;
		}
		return true;
	}
}
