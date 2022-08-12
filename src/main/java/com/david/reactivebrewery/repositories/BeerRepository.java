package com.david.reactivebrewery.repositories;


import com.david.reactivebrewery.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface BeerRepository extends ReactiveCrudRepository<Beer, Long> {
//    Page<Beer> findAllByBeerName(String beerName, Pageable pageable);
//
//    Page<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, Pageable pageable);
//
//    Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyleEnum beerStyle, Pageable pageable);

    Beer findByUpc(String upc);
}
