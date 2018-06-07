package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Owner;

/**
 * Manager for working with Owner object
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public interface OwnerManager {

    void createOwner(Owner owner);

    void updateOwner(Owner owner);

    Owner getOwner();
}
