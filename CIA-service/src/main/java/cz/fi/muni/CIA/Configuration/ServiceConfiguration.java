package cz.fi.muni.CIA.Configuration;

import cz.fi.muni.CIA.PersistenceApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * class ServiceConfiguration
 *
 * @author Peter Balcirak <peter.balcirak@gmail.com>
 */
@Configuration
@Import(PersistenceApplicationContext.class)
@ComponentScan(basePackages = "cz.fi.muni.CIA")
public class ServiceConfiguration {

}
