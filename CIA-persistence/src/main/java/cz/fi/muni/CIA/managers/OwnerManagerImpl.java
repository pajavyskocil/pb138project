package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.DbUtils;
import cz.fi.muni.CIA.Exceptions.OwnerException;
import cz.fi.muni.CIA.entities.Configuration;
import cz.fi.muni.CIA.entities.Owner;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XQueryService;

import javax.inject.Named;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manager for Person entity
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Named
public class OwnerManagerImpl implements OwnerManager {

    private static final Logger logger = Logger.getLogger(OwnerManagerImpl.class.getName());
    private static final Long OWNER_ID = 1L;

    private Configuration configuration = DbUtils.loadConfig();

    private Collection collection;

    public OwnerManagerImpl(Collection collection) {
        this.collection = collection;
    }

    @Override
    public void createOwner(Owner owner) {
        Owner oldOwner = getOwner();
        if (oldOwner != null) {
            throw new OwnerException("Owner can be only one!");
        }

        logger.log(Level.INFO, "Creating new Owner: " + owner.toString());
        try {
            if (owner.getId() != null) {
                throw new OwnerException("Owner id is not null!");
            }
            owner.setId(DbUtils.getPersonNextId(configuration, collection));

            String xQuery = "let $accounting := doc('" + configuration.getAccountingResourceName()+ "')" +
                    "return update insert element owner { " +
                    "attribute pid {'" + owner.getId()+ "'}, " +
                    "element name {'" + owner.getName() + "'}, " +
                    "element email {'" + owner.getEmail() + "'}, " +
                    "element phone {'" + owner.getPhoneNumber() + "'}, " +
                    "element address {" +
                    "element streetAddress {'" + owner.getAddress().getStreetAddress() + "'}, " +
                    "element city {'" + owner.getAddress().getCity() + "'}, " +
                    "element country {'" + owner.getAddress().getCountry() + "'}, " +
                    "element postalCode {'" + owner.getAddress().getPostCode() + "'}" +
                    "}, " +
                    "element accountNumber {'" + owner.getAccountNumber() + "'}," +
                    "element logo {'" + owner.getLogoBASE64() + "'}" +
                    "} into $accounting/accounting";
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty("indent", "yes");
            service.setProperty("encoding", "UTF-8");
            CompiledExpression compiled = service.compile(xQuery);

            service.execute(compiled);
        } catch (XMLDBException ex){
            logger.log(Level.SEVERE, "XMLDBException during create new owner: " + ex.getMessage());
            throw new OwnerException("Error during create new owner");
        }
    }

    @Override
    public void updateOwner(Owner owner) {
        logger.log(Level.INFO, "Updating Owner with id: " + owner.getId());
        try {
            String xQuery = "let $accounting := doc('" + configuration.getAccountingResourceName()+ "')" +
                    "return update replace $accounting//owner[@pid='" + owner.getId() + "'] with element owner { " +
                    "attribute pid {'" + owner.getId()+ "'}, " +
                    "element name {'" + owner.getName() + "'}, " +
                    "element email {'" + owner.getEmail() + "'}, " +
                    "element phone {'" + owner.getPhoneNumber() + "'}, " +
                    "element address {" +
                    "element streetAddress {'" + owner.getAddress().getStreetAddress() + "'}, " +
                    "element city {'" + owner.getAddress().getCity() + "'}, " +
                    "element country {'" + owner.getAddress().getCountry() + "'}, " +
                    "element postalCode {'" + owner.getAddress().getPostCode() + "'}" +
                    "}, " +
                    "element accountNumber {'" + owner.getAccountNumber() + "'}," +
                    "element logo {'" + owner.getLogoBASE64() + "'}" +
                    "}";
            System.out.println(xQuery);
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty("indent", "yes");
            service.setProperty("encoding", "UTF-8");
            CompiledExpression compiled = service.compile(xQuery);

            service.execute(compiled);
        } catch (XMLDBException ex){
            logger.log(Level.SEVERE, "XMLDBException during update owner: " + ex.getMessage());
            throw new OwnerException("Error during update owner with id" + owner.getId());
        }
    }

    @Override
    public Owner getOwner() {
        logger.log(Level.INFO, "Getting Owner");
        try {
            String xQuery = "let $doc := doc('" + configuration.getAccountingResourceName() + "')" +
                    "return $doc//owner";
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            service.setProperty("indent", "yes");
            service.setProperty("encoding", "UTF-8");
            CompiledExpression compiledExpression = service.compile(xQuery);
            ResourceSet resourceSet = service.execute(compiledExpression);
            ResourceIterator resourceIterator = resourceSet.getIterator();
            Resource res = resourceIterator.nextResource();
            if (res == null) {
                return null;
            } else {
                return DbUtils.ownerXMLToOwner(res.getContent().toString());
            }
        } catch (XMLDBException ex) {
            logger.log(Level.SEVERE, "XMLDBException during get owner: " + ex.getMessage());
            throw new OwnerException("Error during get owner");
        }
    }
}
