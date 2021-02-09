package org.mp.mixer.dto;

import org.mp.mixer.domain.Address;

import java.util.List;

public class MixRequestDTO {
    List<Address> addressList;
    float amount;
    int time;

    public MixRequestDTO(List<Address> addressList, float amount, int time) {
        this.addressList = addressList;
        this.amount = amount;
        this.time = time;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MixRequestDTO{" +
                "amount=" + amount +
                ", time=" + time +
                '}';
    }
}
