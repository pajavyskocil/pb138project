package cz.fi.muni.CIA;

import cz.fi.muni.CIA.managers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.xmldb.api.base.Collection;

/**
 * class PersistenceApplicationContext
 *
 * @author Peter Balcirak <peter.balcirak@gmail.com>
 */
@Configuration
@ComponentScan(basePackages = "cz.fi.muni.CIA")
public class PersistenceApplicationContext {

	@Bean
	public Collection dataSource(){
		return DbUtils.loadCollection(DbUtils.loadConfig());
	}

	@Bean
	public PersonManager personManager() {
		return new PersonManagerImpl(dataSource());
	}

	@Bean
	public InvoiceManager invoiceManager() {
		return new InvoiceManagerImpl(dataSource());
	}

	@Bean
	public OwnerManager ownerManager() { return new OwnerManagerImpl(dataSource()); }
}
