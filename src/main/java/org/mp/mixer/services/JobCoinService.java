package org.mp.mixer.services;

import org.mp.mixer.JobCoinServer;
import org.mp.mixer.domain.Transaction;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.ExchangeFunctions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

public class JobCoinService {
    private final ExchangeFunction exchange = ExchangeFunctions.create(new ReactorClientHttpConnector());
    public void printAllTransactions() {
        URI uri = URI.create(String.format("http://%s:%d/transaction", JobCoinServer.HOST, JobCoinServer.PORT));
        ClientRequest request = ClientRequest.method(HttpMethod.GET, uri).build();
        Flux<Transaction> transactions = exchange.exchange(request)
                .flatMapMany(response -> response.bodyToFlux(Transaction.class));
        Mono<List<Transaction>> transactionsList = transactions.collectList();

        transactionsList.block().forEach(System.out::println);
    }
    public void createTransaction(List<Transaction> mixes) {
        URI uri = URI.create(String.format("http://%s:%d/transaction", JobCoinServer.HOST, JobCoinServer.PORT));
        for(Transaction mix : mixes) {
            ClientRequest request = ClientRequest.method(HttpMethod.POST, uri)
                    .body(BodyInserters.fromObject(mix)).build();
            Mono<ClientResponse> response = exchange.exchange(request);
            response.block();
        }
    }
}
