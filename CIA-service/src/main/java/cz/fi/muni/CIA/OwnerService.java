package cz.fi.muni.CIA;

import cz.fi.muni.CIA.entities.Owner;

/**
 * interface for Owner service
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public interface OwnerService {

    /**
     * Create owner entity
     *
     * @param owner
     */
    void createOwner(Owner owner);

    /**
     * Edit owner entity
     *
     * @param owner
     */
    void updateOwner(Owner owner);

    /**
     * Get owner
     *
     * @return owner entity
     */
    Owner getOwner();
}
