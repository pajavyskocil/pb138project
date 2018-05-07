package cz.fi.muni.CIA.entities;

/**
 * @author Pavel Vyskocil <vyskocilpavel@muni.cz>
 */
public class Configuration {

	private String dbUserName;
	private String dbUserPassword;
	private String dbDriver;
	private String dbPrefix;
	private String dbCollectionName;
	private String accountingResourceName;
	private String metadataResourceName;

	public Configuration() {
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbUserPassword() {
		return dbUserPassword;
	}

	public void setDbUserPassword(String dbUserPassword) {
		this.dbUserPassword = dbUserPassword;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbPrefix() {
		return dbPrefix;
	}

	public void setDbPrefix(String dbPrefix) {
		this.dbPrefix = dbPrefix;
	}

	public String getDbCollectionName() {
		return dbCollectionName;
	}

	public void setDbCollectionName(String dbCollectionName) {
		this.dbCollectionName = dbCollectionName;
	}

	public String getAccountingResourceName() {
		return accountingResourceName;
	}

	public void setAccountingResourceName(String accountingResourceName) {
		this.accountingResourceName = accountingResourceName;
	}

	public String getMetadataResourceName() {
		return metadataResourceName;
	}

	public void setMetadataResourceName(String metadataResourceName) {
		this.metadataResourceName = metadataResourceName;
	}
}
