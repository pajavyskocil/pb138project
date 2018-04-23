package cz.fi.muni.CIA;

import cz.fi.muni.CIA.Exceptions.PropertyException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class DbUtils {
	private static final Logger logger = Logger.getLogger(DbUtils.class.getName());

	private final String DB_USER_NAME = "db_user_name";
	private final String DB_USER_PASSWORD = "db_user_password";
	private final String DB_DRIVER = "db_driver";
	private final String DB_PREFIX = "db_prefix";
	private final String DB_COLLECTION_NAME = "db_collectionName";
	private final String DB_RESOURCE_NAME = "db_resourceName";



	private Properties properties = new Properties();
	private InputStream inputStream = null;

	public DbUtils() {
		try {
			loadConfig();
		} catch (PropertyException ex) {

		}
	}

	public Collection loadCollection() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			XMLDBException {
		Database database = (Database) Class.forName(properties.getProperty(DB_DRIVER)).newInstance();
		database.setProperty("create-database", "true");
		DatabaseManager.registerDatabase(database);
		Collection collection =  DatabaseManager.getCollection(properties.getProperty(DB_PREFIX) +
				properties.getProperty(DB_COLLECTION_NAME), properties.getProperty(DB_USER_NAME),
				properties.getProperty(DB_USER_PASSWORD));
		if (collection == null) {
			collection = createCollection();
		}
		return collection;
	}

	private Collection createCollection() throws XMLDBException{
		Collection parent =  DatabaseManager.getCollection(properties.getProperty(DB_PREFIX), properties.getProperty(DB_USER_NAME),
				properties.getProperty(DB_USER_PASSWORD));
		CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
		mgt.createCollection(properties.getProperty(DB_COLLECTION_NAME));
		parent.close();
		Collection collection =  DatabaseManager.getCollection(properties.getProperty(DB_PREFIX) +
						properties.getProperty(DB_COLLECTION_NAME), properties.getProperty(DB_USER_NAME),
				properties.getProperty(DB_USER_PASSWORD));

		XMLResource resource = (XMLResource) collection.createResource(properties.getProperty(DB_RESOURCE_NAME), "XMLResource");
		resource.setContent("<accounting></accounting>");
		collection.storeResource(resource);

		return collection;
	}

	private void loadConfig() throws PropertyException {
		try{
			InputStream inputStream = DbUtils.class.getClassLoader().getResourceAsStream("config.conf");
			this.properties.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					throw new PropertyException("Cannot load config properties from input stream.");
				}
			} else {
				throw new PropertyException("Cannot load config file.");
			}
		}
	}

}
