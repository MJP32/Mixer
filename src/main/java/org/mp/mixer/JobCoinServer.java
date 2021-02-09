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

import org.mp.mixer.client.JobCoinClient;
import org.mp.mixer.handler.TransactionHandler;
import org.mp.mixer.repo.TransactionRepository;
import org.mp.mixer.repo.TransactionRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import reactor.ipc.netty.http.server.HttpServer;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

public class JobCoinServer {
    private static final Logger log = LoggerFactory.getLogger(new JobCoinServer().getClass().getName());
    @Value("${host}")
    public static final String HOST="localhost";

    @Value("${port}")
    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        JobCoinServer jobCoinServer = new JobCoinServer();
        jobCoinServer.startReactorServer();
        log.info("JobCoinServer Started. ");

        new JobCoinClient().main(new String[]{});

    }

    public RouterFunction<ServerResponse> routingFunction() {
        TransactionRepository repository = new TransactionRepositoryImpl();
        TransactionHandler handler = new TransactionHandler(repository);
        return nest(path("/transaction"),
                nest(accept(APPLICATION_JSON),
                        route(GET("/{id}"), handler::getTransaction)
                                .andRoute(method(HttpMethod.GET), handler::listTransactions)
                ).andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::createTransaction));
        //.andRoute(POST("/mix").and(contentType(APPLICATION_JSON)), handler::createMix));
    }

    public void startReactorServer()  {
        try {
            log.info("Server started on: " + String.format("%s %d", JobCoinServer.HOST, JobCoinServer.PORT));
            RouterFunction<ServerResponse> route = routingFunction();
            HttpHandler httpHandler = toHttpHandler(route);
            ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
            HttpServer server = HttpServer.create(HOST, PORT);
            server.newHandler(adapter).block();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
