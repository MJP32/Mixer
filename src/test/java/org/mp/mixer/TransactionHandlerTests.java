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
import org.mp.mixer.domain.Transaction;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


public class TransactionHandlerTests {

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
	public void getTransaction() throws Exception {
		this.testClient.get()
				.uri("/1")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Transaction.class).hasSize(1).returnResult();
	}

	@Test
	public void getTransactionNotFound() throws Exception {
		this.testClient.get()
				.uri("/42")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void listTransactions() throws Exception {
		this.testClient.get()
				.uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Transaction.class).hasSize(2).returnResult();
	}

	@Test
	public void createTransaction() throws Exception {
		Transaction transaction = new Transaction("Mike", "Bob", 25f);
		Transaction transaction2 = new Transaction("Mike", "Bob", 25f);

		this.testClient.post()
				.uri("/")
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(transaction)
				.exchange()
				.expectStatus().isOk();
	}
}