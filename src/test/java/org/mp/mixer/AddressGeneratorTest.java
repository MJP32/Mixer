package org.mp.mixer;

import org.junit.Test;
import org.mp.mixer.domain.Address;
import org.mp.mixer.services.GenerateAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AddressGeneratorTest {

     GenerateAddress ga = new GenerateAddress();

    @Test
    public void getAddressHashedTest() {
        String name = ga.getHashedAddress("texxt");
        System.out.println(name);
        assertNotNull(name);
        assertEquals(64, name.length());
    }
}
