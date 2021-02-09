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

package org.mp.mixer.handler;

import org.mp.mixer.domain.Transaction;
import org.mp.mixer.repo.TransactionRepository;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class TransactionHandler {

	private final TransactionRepository repository;

	public TransactionHandler(TransactionRepository repository) {
		this.repository = repository;
	}

	public Mono<ServerResponse> getTransaction(ServerRequest request) {
		int transactionId = Integer.parseInt(request.pathVariable("id"));
		Mono<ServerResponse> notFound = ServerResponse.notFound().build();
		Mono<Transaction> transactionMono = this.repository.getTransaction(transactionId);
		return transactionMono
				.flatMap(transaction -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(transaction)))
				.switchIfEmpty(notFound);
	}

	public Mono<ServerResponse> createTransaction(ServerRequest request) {
		Mono<Transaction> transaction = request.bodyToMono(Transaction.class);
		return ServerResponse.ok().build(this.repository.saveTransaction(transaction));
	}

	public Mono<ServerResponse> listTransactions(ServerRequest request) {
		Flux<Transaction> transactions = this.repository.allTransactions();
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(transactions, Transaction.class);
	}

	public Mono<ServerResponse> createMix() {
		Flux<Transaction> transactions = this.repository.allTransactions();
		return ServerResponse.ok().contentType(APPLICATION_JSON).body(transactions, Transaction.class);
	}
}
