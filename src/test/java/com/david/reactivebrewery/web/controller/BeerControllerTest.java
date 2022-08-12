package com.david.reactivebrewery.web.controller;

import com.david.reactivebrewery.services.BeerService;
import com.david.reactivebrewery.web.model.BeerDto;
import com.david.reactivebrewery.web.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ConnectionFactoryInitializer connectionFactoryInitializer;

    @MockBean
    BeerService beerService;

    BeerDto validBeer;

    BeerPagedList beerPagedList;

    @BeforeEach
    void setup() {
        validBeer = BeerDto.builder()
                .beerName("TestBeer")
                .beerStyle("IPA")
                .upc("123123123")
                .build();

        beerPagedList = new BeerPagedList(List.of(
                BeerDto.builder()
                        .id(1L)
                        .beerName("TestBeer_1")
                        .beerStyle("IPA")
                        .upc("123123123")
                        .build(),
                BeerDto.builder()
                        .id(2L)
                        .beerName("TestBeer_2")
                        .beerStyle("WHEAT")
                        .upc("111111111")
                        .build(),
                BeerDto.builder()
                        .id(3L)
                        .beerName("TestBeer_3")
                        .beerStyle("ALE")
                        .upc("321321321")
                        .build()));
    }

    @Test
    void getBeerById() {

        Long beerId = 1L;

        given(beerService.getById(any(), any())).willReturn(Mono.just(validBeer));

        webTestClient.get()
                .uri("/api/v1/beer/" + beerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(beerDto -> beerDto.getBeerName(), equalTo(validBeer.getBeerName()));
    }

    @Test
    void listBeers() {

        given(beerService.listBeers(any(), any(), any(), any())).willReturn(beerPagedList);

        webTestClient.get()
                .uri("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerPagedList.class)
                .value(beerPagedList -> beerPagedList.getSize(), equalTo(3))
                .value(beerPagedList -> beerPagedList.getContent().get(0).getBeerName(), equalTo("TestBeer_1"))
                .value(beerPagedList -> beerPagedList.getContent().get(1).getBeerName(), equalTo("TestBeer_2"))
                .value(beerPagedList -> beerPagedList.getContent().get(2).getBeerName(), equalTo("TestBeer_3"));

    }

    @Test
    void getBeerByUpc() {

        String upc = "123123123";

        given(beerService.getByUpc(upc)).willReturn(validBeer);

        webTestClient.get()
                .uri("/api/v1/beerUpc/" + upc)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(beerDto -> beerDto.getBeerName(), equalTo(validBeer.getBeerName()));
    }
}