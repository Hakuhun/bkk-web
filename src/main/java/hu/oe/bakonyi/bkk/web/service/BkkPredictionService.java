package hu.oe.bakonyi.bkk.web.service;

import hu.oe.bakonyi.bkk.web.client.WeatherClient;
import hu.oe.bakonyi.bkk.web.converter.InputDataConverter;
import hu.oe.bakonyi.bkk.web.model.BkkEvaluator;
import hu.oe.bakonyi.bkk.web.model.BkkRequestModel;
import hu.oe.bakonyi.bkk.web.model.BkkResponseData;
import hu.oe.bakonyi.bkk.web.model.SimpleWeatherModel;
import hu.oe.bakonyi.bkk.web.model.weather.Coord;
import hu.oe.bakonyi.bkk.web.model.weather.Model200;
import lombok.extern.log4j.Log4j2;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class BkkPredictionService {

    @Autowired
    private BkkEvaluator evaluator;

    @Autowired
    private InputDataConverter converter;

    @Autowired
    WeatherClient client;

    public BkkResponseData predictByLongFormat(BkkRequestModel features) {
        Model200 weather = client.getWeatherByCoord(Coord.builder().lat(features.getLocation().getLat()).lon(features.getLocation().getLon()).build());

        evaluator.checkEvaluatorAvailability();

        List<? extends InputField> inputFields = evaluator.getInputFields();
        List<? extends TargetField> targetFields = evaluator.getTargetFields();
        List<? extends OutputField> outputFields = evaluator.getOutputFields();

        Map<String, ?> inputRecord = converter.convertLongTermModelData(features, weather);
        if (inputRecord == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();

        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();

            Object rawValue = inputRecord.get(inputName.getValue());

            FieldValue inputValue = inputField.prepare(rawValue);

            arguments.put(inputName, inputValue);
        }

        Map<FieldName, Double> results = evaluator.evaluate(arguments);

        Map<String, Double> resultRecord = (Map<String, Double>) EvaluatorUtil.decodeAll(results);

        double label = Math.abs(resultRecord.get("target"));
        log.info("Features: " + features + " label: " + label);

        SimpleWeatherModel weatherModel = SimpleWeatherModel.builder().precip(weather.getSnow().get_3h() + weather.getRain().get_3h() + " mm").temperature(weather.getMain().getTemp() + " C°").visibility(weather.getVisibility() + " m").build();

        return BkkResponseData.builder()
                .prediction(label)
                .time(Instant.now()).routeId(features.getRouteInfo().getRoute())
                .weather(weatherModel)
                .build();
    }

}
