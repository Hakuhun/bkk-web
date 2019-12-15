package hu.oe.bakonyi.bkk.web.model.weather;

import hu.oe.bakonyi.bkk.web.model.Location;
import lombok.Data;

@Data
public class BasicWeatherModel {
    private double temperature;
    private double feelsLikeTemperature;
    private double humidity;
    private double pressure;
    private double rainIntensity;
    private double snowIntensity;
    private int visibility;
    private Location location;
}
