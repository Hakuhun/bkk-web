package hu.oe.bakonyi.bkk.web.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleWeatherModel {
    private String temperature, precip, visibility;
}
