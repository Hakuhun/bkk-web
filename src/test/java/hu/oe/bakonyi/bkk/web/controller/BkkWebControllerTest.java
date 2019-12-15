package hu.oe.bakonyi.bkk.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.web.client.WeatherClient;
import hu.oe.bakonyi.bkk.web.model.BkkRequestModel;
import hu.oe.bakonyi.bkk.web.model.BkkResponseData;
import hu.oe.bakonyi.bkk.web.model.weather.Model200;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integration")
public class BkkWebControllerTest {

    @Autowired
    private WebTestClient webApiClient;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    WeatherClient weatherClient0;

    @BeforeEach
    public void init() throws IOException {
        Model200 weather = mapper.readValue(ResourceUtils.getFile("classpath:model200_ok.json"), Model200.class);
        Mockito.when(weatherClient0.getWeatherByCoord(Mockito.any())).thenReturn(weather);
    }

    @Test
    public void getPrediction_allOk_allOk() throws IOException {
        mapper.findAndRegisterModules();
        BkkRequestModel model = mapper.readValue(ResourceUtils.getFile("classpath:bkkrequest_ok.json"), BkkRequestModel.class);

        webApiClient.post()
                .uri(uriBuilder -> uriBuilder.path("/bkk/prod/predict").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(model))
                .exchange()
                .expectBody(BkkResponseData.class)
                .consumeWith(((Consumer<EntityExchangeResult<BkkResponseData>>) response -> {
                    BkkResponseData body = response.getResponseBody();
                    assertNotNull(body, "Response body is null");
                    assertEquals(HttpStatus.OK, response.getStatus());
                }));
    }

    @Test
    public void getPrediction_missingRequest_throwsBadRequest() throws IOException {
        mapper.findAndRegisterModules();
        BkkRequestModel model = mapper.readValue(ResourceUtils.getFile("classpath:bkkrequest_nok_missing_location.json"), BkkRequestModel.class);

        webApiClient.post()
                .uri(uriBuilder -> uriBuilder.path("/bkk/prod/predict").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(model))
                .exchange()
                .expectBody(BkkResponseData.class)
                .consumeWith(((Consumer<EntityExchangeResult<BkkResponseData>>) response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
                }));

        model = mapper.readValue(ResourceUtils.getFile("classpath:bkkrequest_nok_missing_time.json"), BkkRequestModel.class);

        webApiClient.post()
                .uri(uriBuilder -> uriBuilder.path("/bkk/prod/predict").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(model))
                .exchange()
                .expectBody(BkkResponseData.class)
                .consumeWith(((Consumer<EntityExchangeResult<BkkResponseData>>) response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
                }));

        model = mapper.readValue(ResourceUtils.getFile("classpath:bkkrequest_nok_missing_location.json"), BkkRequestModel.class);

        webApiClient.post()
                .uri(uriBuilder -> uriBuilder.path("/bkk/prod/predict").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(model))
                .exchange()
                .expectBody(BkkResponseData.class)
                .consumeWith(((Consumer<EntityExchangeResult<BkkResponseData>>) response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
                }));
    }

    @Test
    public void getPrediction_badRequest_throwsBadRequest() throws IOException {
        mapper.findAndRegisterModules();
        BkkRequestModel model = mapper.readValue(ResourceUtils.getFile("classpath:bkkrequest_nok_wrongRouteInfo.json"), BkkRequestModel.class);

        webApiClient.post()
                .uri(uriBuilder -> uriBuilder.path("/bkk/prod/predict").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(model))
                .exchange()
                .expectBody(BkkResponseData.class)
                .consumeWith(((Consumer<EntityExchangeResult<BkkResponseData>>) response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
                }));

        model = mapper.readValue(ResourceUtils.getFile("classpath:bkkrequest_nok_wrongRouteInfo2.json"), BkkRequestModel.class);

        webApiClient.post()
                .uri(uriBuilder -> uriBuilder.path("/bkk/prod/predict").build())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(model))
                .exchange()
                .expectBody(BkkResponseData.class)
                .consumeWith(((Consumer<EntityExchangeResult<BkkResponseData>>) response -> {
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatus());
                }));

    }


}
