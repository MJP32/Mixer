package org.mp.mixer.domain;

public class HouseWallet {
    private Address address;
    private Float amount;
//    private List<HouseWalletObserver> observers = new ArrayList<HouseWalletObserver>();

//    public void setObservers(List<HouseWalletObserver> observers) {
//        this.observers = observers;
//    }


    public HouseWallet(Address address, Float amount) {
        this.address = address;
        this.amount = amount;
    }

    //    public List<HouseWalletObserver> getObservers() {
//        return observers;
//    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
//        if(amount > 0)
//            notifyAllObservers();
    }

//    public void attach(HouseWalletObserver observer){
//        observers.add(observer);
//    }
//
//    public void notifyAllObservers(){
//        for (HouseWalletObserver observer : observers) {
//            observer.update();
//        }
//    }


}
