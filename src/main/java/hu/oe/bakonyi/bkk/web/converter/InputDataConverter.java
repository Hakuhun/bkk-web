package hu.oe.bakonyi.bkk.web.converter;

import hu.oe.bakonyi.bkk.web.model.BkkRequestModel;
import hu.oe.bakonyi.bkk.web.model.weather.Model200;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

@Service
public class InputDataConverter {


    public Map<String, Double> convertLongTermModelData(BkkRequestModel model, Model200 weather) {
        Map<String, Double> datas = new HashMap<>();
        //route
        datas.put("field_0", Double.valueOf(model.getRouteInfo().getRoute().split("_")[1]));
        //stop
        datas.put("field_1", Double.valueOf(model.getRouteInfo().getStop().split("_")[1]));
        //Idő adatok
        //month
        datas.put("field_2", (double) model.getTime().atZone(ZoneId.of("Europe/Budapest")).get(ChronoField.MONTH_OF_YEAR));
        //day
        datas.put("field_3", (double) model.getTime().atZone(ZoneId.of("Europe/Budapest")).get(ChronoField.DAY_OF_WEEK));
        //hour
        datas.put("field_4", (double) model.getTime().atZone(ZoneId.of("Europe/Budapest")).get(ChronoField.HOUR_OF_DAY));

        //Van e alert?
        datas.put("field_5", model.getRouteInfo().isAlert() ? 1.0 : 0.0);

        //Időjárási adatok

        datas.put("field_6", weather.getMain().getTemp());
        datas.put("field_7", weather.getRain().get_3h() + weather.getSnow().get_3h());
        datas.put("field_8", Double.valueOf(weather.getVisibility()));

        return datas;
    }
}
