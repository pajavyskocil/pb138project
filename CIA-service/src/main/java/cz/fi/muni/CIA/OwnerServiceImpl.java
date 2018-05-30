package cz.fi.muni.CIA;

import cz.fi.muni.CIA.entities.Owner;
import cz.fi.muni.CIA.managers.OwnerManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerManager ownerManager;

    @Inject
    public OwnerServiceImpl(OwnerManager ownerManager) {
        this.ownerManager = ownerManager;
    }

    @Override
    public void createOwner(Owner owner) {
        ownerCheck(owner);
        if (owner.getId() != null) throw new IllegalArgumentException("Owners id has to be be null!");
        ownerManager.createOwner(owner);
    }

    @Override
    public void updateOwner(Owner owner) {
        ownerCheck(owner);
        idCheck(owner.getId());
        ownerManager.updateOwner(owner);
    }

    @Override
    public Owner getOwner() {
        return ownerManager.getOwner();
    }

    private void ownerCheck(Owner owner) {
        if (owner.getName() == null) throw new IllegalArgumentException("Owner name cannot be null!");
        if (owner.getEmail() == null) throw new IllegalArgumentException("Owner email cannot be null!");
        if (owner.getAccountNumber() == null) throw new IllegalArgumentException("Owner account number cannot be null!");
        if (owner.getPhoneNumber() == null) throw new IllegalArgumentException("Owner phone number cannot be null!");
        if (owner.getAddress() == null) throw new IllegalArgumentException("Owner address cannot be null!");
        if (owner.getAddress().getCity() == null) throw new IllegalArgumentException("Owner city cannot be null!");
        if (owner.getAddress().getCountry() == null) throw new IllegalArgumentException("Owner country cannot be null!");
        if (owner.getAddress().getPostCode() == null) throw new IllegalArgumentException("Owner post code cannot be null!");
        if (owner.getAddress().getStreetAddress() == null) throw new IllegalArgumentException("Owner street address cannot be null!");
    }

    private void idCheck(Long id) {
        if (id == null) throw new IllegalArgumentException("Id cannot be null!");
        if (id < 0) throw new IllegalArgumentException("Id cannot be less than zero!");
    }
}
