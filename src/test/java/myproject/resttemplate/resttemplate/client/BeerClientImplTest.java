package myproject.resttemplate.resttemplate.client;

import myproject.resttemplate.resttemplate.model.BeerDTO;
import myproject.resttemplate.resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;

    @Test
    void testDeleteBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("12.99"))
                .beerName("Kolejne Piwo")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();

        BeerDTO savedBeer = beerClient.creatBeer(newDto);

        beerClient.deleteBeer(savedBeer.getId());

        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.getBeerById(savedBeer.getId());
        });
    }

    @Test
    void testUpdateBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("12.34"))
                .beerName("Nowe Moje Własne")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123456")
                .build();

        BeerDTO beerDTO = beerClient.creatBeer(newDto);

        final String newName = "Kwas XY";
        beerDTO.setBeerName(newName);
        BeerDTO updatedBeer = beerClient.updateBeer(beerDTO);

        assertEquals(newName, updatedBeer.getBeerName());
    }

    @Test
    void testCreateNewBeer() {

        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("Papieżowe Mocne")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("2137")
                .price(new BigDecimal("21.37"))
                .quantityOnHand(2137)
                .build();

        BeerDTO savedBeer = beerClient.creatBeer(beerDTO);
        assertNotNull(savedBeer);
    }

    @Test
    void testGetBeerById() {

        Page<BeerDTO> beerDTOS = beerClient.listBeers();

        BeerDTO dto = beerDTOS.getContent().get(0);

        BeerDTO byId = beerClient.getBeerById(dto.getId());

        assertNotNull(byId);
    }

    @Test
    void testListBeersNoName() {

        Page<BeerDTO> dtos =
        beerClient.listBeers(null, null, null , null, null);

        System.out.println(dtos.getTotalElements());
    }

    @Test
    void testListBeers() {

        beerClient.listBeers("ALE", null, null , null, null);
    }
}