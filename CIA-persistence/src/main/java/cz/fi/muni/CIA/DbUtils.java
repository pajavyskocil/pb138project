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
import java.time.LocalDate;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class DbUtils {
	private static final Logger logger = Logger.getLogger(DbUtils.class.getName());

	public DbUtils() {
	}

	public static Collection loadCollection(Configuration configuration) {
		logger.log(Level.INFO, "Loading collection with name " + configuration.getDbCollectionName());
		Collection collection = null;
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

			} else if (collection.getResource(configuration.getAccountingResourceName()) == null || collection.getResource(configuration.getMetadataResourceName()) == null) {
				logger.log(Level.WARNING, "One of the resource is missing.");
				throw new DatabaseException("One of the resource is missing.");
			}

			if (!validate(collection.getResource(configuration.getAccountingResourceName()).getContent().toString(), "CIA-persistence/src/main/resources/cia-schema.xsd")){
				logger.log(Level.SEVERE, "Resource is not valid");
				throw new ValidationException("Resource is not valid");
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
		} catch (Exception ex) {

		}
		return collection;

	}

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

	public static void dropCollection(Configuration configuration) throws XMLDBException {
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

	public static void incrementInvoiceId(Configuration configuration, Collection collection) {
		String xQuery = "let $metadata := doc('" + configuration.getMetadataResourceName() +"')" +
				"let $next-id := $metadata//invoice-next-id/text() + 1" +
				"return update replace $metadata//invoice-next-id/text() with $next-id";
		incrementId(collection, xQuery);
	}

	public static void incrementPersonId(Configuration configuration, Collection collection) {
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

	public static Long getInvoiceNextId(Configuration configuration, Collection collection) {
		String xQuery = "let $metadata := doc('" + configuration.getMetadataResourceName() +"')" +
				"return $metadata//invoice-next-id/text()";
		Long id =  getNextId(collection, xQuery);
		incrementInvoiceId(configuration, collection);
		return id;
	}

	public static Long getPersonNextId(Configuration configuration, Collection collection) {
		String xQuery = "let $metadata := doc('" + configuration.getMetadataResourceName() +"')" +
				"return $metadata//person-next-id/text()";
		Long id = getNextId(collection, xQuery);
		incrementPersonId(configuration, collection);
		return id;
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


	public static Person personXMLtoPerson(String personXML) {
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

			person.setName(element.getElementsByTagName("name").item(0).getTextContent());

			person.setEmail(element.getElementsByTagName("email").item(0).getTextContent());

			person.setPhoneNumber(element.getElementsByTagName("phone").item(0).getTextContent());

			Address address = new Address();
			Element addressElement = (Element) element.getElementsByTagName("address").item(0);

			address.setStreetAddress(addressElement.getElementsByTagName("streetAddress").item(0).getTextContent());
			address.setCity(addressElement.getElementsByTagName("city").item(0).getTextContent());
			address.setCountry(addressElement.getElementsByTagName("country").item(0).getTextContent());
			address.setPostCode(addressElement.getElementsByTagName("postalCode").item(0).getTextContent());

			person.setAddress(address);

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

	public static Owner ownerXMLToOwner(String ownerXML) {
		Owner owner = new Owner();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(ownerXML));
			Document document = documentBuilder.parse(inputSource);
			NodeList nodeList = document.getElementsByTagName("owner");
			Element element = (Element) nodeList.item(0);

			owner.setId(Long.parseLong(element.getAttribute("pid")));

			owner.setName(element.getElementsByTagName("name").item(0).getTextContent());

			owner.setEmail(element.getElementsByTagName("email").item(0).getTextContent());

			owner.setPhoneNumber(element.getElementsByTagName("phone").item(0).getTextContent());

			Address address = new Address();
			Element addressElement = (Element) element.getElementsByTagName("address").item(0);

			address.setStreetAddress(addressElement.getElementsByTagName("streetAddress").item(0).getTextContent());
			address.setCity(addressElement.getElementsByTagName("city").item(0).getTextContent());
			address.setCountry(addressElement.getElementsByTagName("country").item(0).getTextContent());
			address.setPostCode(addressElement.getElementsByTagName("postalCode").item(0).getTextContent());

			owner.setAddress(address);

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


	public static Invoice invoiceXMLToInvoice(Collection collection, String invoiceXML) {
		Invoice invoice = new Invoice();
		PersonManager personManager = new PersonManagerImpl(collection);
		OwnerManager ownerManager = new OwnerManagerImpl(collection);
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(invoiceXML));
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

			Element itemsElement = (Element) element.getElementsByTagName("items").item(0);

			for ( int i = 0; i < itemsElement.getElementsByTagName("item").getLength(); i++) {
				Item item = new Item();

				Element itemElement = (Element) element.getElementsByTagName("item").item(i);

				item.setName(itemElement.getElementsByTagName("name").item(0).getTextContent());

				item.setDescription(itemElement.getElementsByTagName("description").item(0).getTextContent());

				item.setCount(Integer.parseInt(itemElement.getElementsByTagName("count").item(0).getTextContent()));

				item.setPrice(Double.parseDouble(itemElement.getElementsByTagName("price").item(0).getTextContent()));

				item.setTotalPrice(Double.parseDouble(itemElement.getElementsByTagName("totalPrice").item(0).getTextContent()));

				invoice.addItem(item);
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

	public static Configuration loadConfig() {
		logger.log(Level.INFO, "Loading config file.");
		Properties properties = new Properties();
		Configuration configuration = new Configuration();
		InputStream inputStream = null;
		try{
			inputStream = DbUtils.class.getClassLoader().getResourceAsStream("config.conf");
			properties.load(inputStream);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, "Cannot load config properties from input stream." );
					throw new ConfigException("Cannot load config properties from input stream.");
				}
			} else {
				logger.log(Level.SEVERE, "Cannot load config file." );
				throw new ConfigException("Cannot load config file.");
			}
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


	private static boolean validate(String inputXml, String schemaLocation)
			throws SAXException, IOException {
		// build the schema
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaFile = new File(schemaLocation);
		Schema schema = factory.newSchema(schemaFile);
		Validator validator = schema.newValidator();

		// create a source from a string
		Source source = new StreamSource(new StringReader(inputXml));

		// check input
		boolean isValid = true;
		try  {
			validator.validate(source);
		}
		catch (SAXException e) {

			isValid = false;
		}

		return isValid;
	}
}
