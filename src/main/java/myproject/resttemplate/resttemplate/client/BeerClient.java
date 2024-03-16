package myproject.resttemplate.resttemplate.client;

import myproject.resttemplate.resttemplate.model.BeerDTO;
import myproject.resttemplate.resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {

    Page<BeerDTO> listBeers();

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                            Integer pageNumber, Integer pageSize);

    BeerDTO getBeerById(UUID id);

    BeerDTO creatBeer(BeerDTO beerDTO);

    BeerDTO updateBeer(BeerDTO beerDTO);

    void deleteBeer(UUID id);
}
