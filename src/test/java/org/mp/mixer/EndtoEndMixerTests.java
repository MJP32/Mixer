/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mp.mixer;

import org.junit.Before;
import org.junit.Test;
import org.mp.mixer.domain.Address;
import org.mp.mixer.domain.Transaction;
import org.mp.mixer.dto.MixRequestDTO;
import org.mp.mixer.services.MixerService;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EndtoEndMixerTests {

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
	public void MixerTest() {
		List<MixRequestDTO> mixes= new ArrayList<>();
		mixes.add(new MixRequestDTO(Arrays.asList(new Address("addr1"), new Address("addr2"), new org.mp.mixer.domain.Address("addr3")),50f, 10));
		mixes.add(new MixRequestDTO(Arrays.asList(new Address("addr4"), new Address("addr5"), new Address("addr6")),100f, 20));
		try {
			new MixerService().processRequest(mixes).subscribe(System.out::println);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.testClient.get()
				.uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Transaction.class).hasSize(2).returnResult();
	}


}