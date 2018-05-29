package cz.fi.muni.CIA.entities;

import java.util.Objects;

public class Owner extends Person {

    private String logoBASE64;

    public void setLogoBASE64(String logoBASE64) {
        this.logoBASE64 = logoBASE64;
    }

    public String getLogoBASE64() {
        return this.logoBASE64;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner)) return false;
        Owner owner = (Owner) o;
        return	super.equals(o) && Objects.equals(getLogoBASE64(), owner.getLogoBASE64());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getPhoneNumber(), getAddress(), getAccountNumber());
    }

    @Override
    public String toString() {
        return "Owner{" +
                " name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", address=" + getAddress() +
                ", accountNumber='" + getAccountNumber() + '\'' +
                ", logo='" + logoBASE64 + '\'' +
                '}';
    }
}
