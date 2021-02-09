package org.mp.mixer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mp.mixer.domain.Address;
import org.mp.mixer.domain.HouseWallet;
import org.mp.mixer.domain.Transaction;
import org.mp.mixer.dto.MixRequestDTO;
import org.mp.mixer.services.MixerService;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MixTest {
    private WebTestClient testClient;

    @Before
    public void createTestClient() {
        JobCoinServer jobCoinServer = new JobCoinServer();
        this.testClient = WebTestClient.bindToRouterFunction(jobCoinServer.routingFunction())
                .configureClient()
                .baseUrl("http://localhost/transaction")
                .build();
    }

    @Test
    public void MixTest() {
        List<MixRequestDTO> mixes= new ArrayList<>();
        MixRequestDTO mixRequestDTO = new MixRequestDTO(Arrays.asList(new Address("addr1"), new Address("addr2"), new Address("addr3")), 50f, 10);
        //mixes.add(new MixRequestDTO(Arrays.asList(new Address("addr4"), new Address("addr5"), new Address("addr6")),100f, 20));
        HouseWallet house = new HouseWallet(new Address("house"), 50f);

        int count = new MixerService().doMix(mixRequestDTO, house);
        Assert.assertEquals(33, count);
        this.testClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Transaction.class).hasSize(2).returnResult();
    }

}
