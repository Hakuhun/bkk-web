package hu.oe.bakonyi.bkk.web.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class BkkResponseData {
    private SimpleWeatherModel weather;
    private String routeId;
    private Instant time;
    private double prediction;
}
