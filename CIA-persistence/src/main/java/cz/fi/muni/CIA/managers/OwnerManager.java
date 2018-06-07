package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Owner;

/**
 * Manager for working with Owner object
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public interface OwnerManager {

    /**
     * This method creates given owned
     * @param owner Owner to create
     */
    void createOwner(Owner owner);

    /**
     * This method updates owner with  given owned
     * @param owner Owner to update
     */
    void updateOwner(Owner owner);

    /**
     * This method retuns Owner
     * @return Owner
     */
    Owner getOwner();
}
