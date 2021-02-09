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

package org.mp.mixer.repo;

import org.mp.mixer.domain.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


public class TransactionRepositoryImpl implements TransactionRepository {

	private final Map<Integer, Transaction> transactions = new HashMap<>();

	public TransactionRepositoryImpl() {
		this.transactions.put(1, new Transaction("Mike", "Bob", 25f));
		this.transactions.put(2, new Transaction("Mike", "Bob", 25f));
	}

	@Override
	public Mono<Transaction> getTransaction(int id) {
		return Mono.justOrEmpty(this.transactions.get(id));
	}

	@Override
	public Flux<Transaction> allTransactions() {
		return Flux.fromIterable(this.transactions.values());
	}

	@Override
	public Mono<Void> saveTransaction(Mono<Transaction> transactionMono) {
		return transactionMono.doOnNext(transaction -> {
			int id = transactions.size() + 1;
			transactions.put(id, transaction);
			System.out.format("Saved %s with id %d%n", transaction, id);
		}).thenEmpty(Mono.empty());
	}
}
