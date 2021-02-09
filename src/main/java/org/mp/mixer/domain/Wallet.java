package org.mp.mixer.domain;


public class Wallet {
    Address address;
    Float amount;
    public Wallet(Address address) {
        this.address = address;
    }

    public Wallet(Address address, Float amount) {
        this.address = address;
        this.amount = amount;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }


}
