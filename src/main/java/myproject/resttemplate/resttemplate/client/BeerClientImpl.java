package myproject.resttemplate.resttemplate.client;

import lombok.RequiredArgsConstructor;
import myproject.resttemplate.resttemplate.model.BeerDTO;
import myproject.resttemplate.resttemplate.model.BeerDTOPageImpl;
import myproject.resttemplate.resttemplate.model.BeerStyle;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    private static final String BASE_URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";
    private static final String GET_BEER_BY_ID_PATH = GET_BEER_PATH + "/{beerID}";

    @Override
    public Page<BeerDTO> listBeers() {
        return this.listBeers(null, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                                   Integer pageNumber, Integer pageSize) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        //TESTOWE

        //Żądanie wyniku w postaci string dla podanego adresu url zakodowane w projekcie beerRest
        /*ResponseEntity<String> stringResponse =
                restTemplate.getForEntity("http://localhost:8080/api/v1/beer", String.class);
        System.out.println(stringResponse.getBody());*/

        //-----------------------------------------------------------------------------------------------------

        // To samo w postaci mapy
        /*ResponseEntity<Map> mapResponse =
                restTemplate.getForEntity(GET_BEER_PATH, Map.class);
        System.out.println(mapResponse.getBody());*/

        //-----------------------------------------------------------------------------------------------------

        // Przekazuje informcje do Springa aby pracować z Jackson żeby zrobić
        // deserializację do objektu JsonNode
        /*ResponseEntity<JsonNode> jsonResponse =
                restTemplate.getForEntity(GET_BEER_PATH, JsonNode.class);
//        System.out.println(jsonResponse.getBody());// Zwrócony wynik daje pełną informację tak jak po testowaniu w postman

        jsonResponse.getBody().findPath("content") //W json szukanie "content"
                .elements() // Elementy wewnątrz "content"
                .forEachRemaining(node -> {
                    System.out.println(node.get("beerName").asText()); // Dla każdego wystąpienia
                }); */                                                   // beerName wypisanie jego wartości

        //-----------------------------------------------------------------------------------------------------

        // Jackson nie wie jak to zwrócić dla typu PageImpl, trzeba utworzyć własny
        /*ResponseEntity<PageImpl> stringResponse =
                restTemplate.getForEntity(GET_BEER_PATH, PageImpl.class);*/

        //-----------------------------------------------------------------------------------------------------


        // Odpowiedź z własną implementacją Page dla BeerDTO
        /*ResponseEntity<BeerDTOPageImpl> responseEntity =
                restTemplate.getForEntity(GET_BEER_PATH, BeerDTOPageImpl.class);*/


        //-----------------------------------------------------------------------------------------------------

        // To pozwala na budowanie ścieżki z uwzględnieniem queryParams
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if (beerName != null) {
            uriComponentsBuilder.queryParam("beerName", beerName);
        }

        if (beerStyle != null) {
            uriComponentsBuilder.queryParam("beerStyle", beerStyle);
        }

        if (showInventory != null) {
            uriComponentsBuilder.queryParam("showInventory", showInventory);
        }

        if (pageNumber != null) {
            uriComponentsBuilder.queryParam("pageNumber", pageNumber);
        }

        if (pageSize != null) {
            uriComponentsBuilder.queryParam("pageSize", pageSize);
        }

        ResponseEntity<BeerDTOPageImpl> responseEntity =
                restTemplate.getForEntity(uriComponentsBuilder.toUriString(), BeerDTOPageImpl.class);

        return responseEntity.getBody();
    }

    @Override
    public BeerDTO getBeerById(UUID id) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        // Żądanie otrzymania obiektu poprzez podanie ścieżki, żądanego typu, i klucza po którym szukać czyli id
        return restTemplate.getForObject(GET_BEER_BY_ID_PATH, BeerDTO.class, id);
    }

    @Override
    public BeerDTO creatBeer(BeerDTO beerDTO) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<BeerDTO> responseEntity = restTemplate.postForEntity(GET_BEER_PATH, beerDTO, BeerDTO.class);

        URI uri = responseEntity.getHeaders().getLocation(); // Przechwycenie zwrócenej lokacji zawierającej id

        /*String id = uri.getPath().split("/")[4]; // Wyodrębnienie id
        return getBeerById(UUID.fromString(id));*/

        return restTemplate.getForObject(uri.getPath(), BeerDTO.class);
    }

    @Override
    public BeerDTO updateBeer(BeerDTO beerDTO) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.put(GET_BEER_BY_ID_PATH, beerDTO, beerDTO.getId());

        return getBeerById(beerDTO.getId());
    }

    @Override
    public void deleteBeer(UUID id) {

        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.delete(GET_BEER_BY_ID_PATH, id);
    }
}
