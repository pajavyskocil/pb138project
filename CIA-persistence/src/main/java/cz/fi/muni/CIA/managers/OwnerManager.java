package cz.fi.muni.CIA.managers;

import cz.fi.muni.CIA.entities.Owner;

public interface OwnerManager {

    void createOwner(Owner owner);

    void updateOwner(Owner owner);

    Owner getOwner();
}
