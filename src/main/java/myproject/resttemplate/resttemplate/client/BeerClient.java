package myproject.resttemplate.resttemplate.client;

import myproject.resttemplate.resttemplate.model.BeerDTO;
import org.springframework.data.domain.Page;

public interface BeerClient {

    Page<BeerDTO> listBeers();
}
