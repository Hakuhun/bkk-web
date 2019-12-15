package hu.oe.bakonyi.bkk.web.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.web.client.WeatherClient;
import hu.oe.bakonyi.bkk.web.model.BkkRequestModel;
import hu.oe.bakonyi.bkk.web.model.weather.Model200;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class InputDataConverterTest {



    @Test
    public void test_InputDataConverter_ConvertsCorrectly() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        InputDataConverter converter = new InputDataConverter();
        BkkRequestModel model = mapper.readValue(ResourceUtils.getFile("classpath:bkkrequest_ok.json"), BkkRequestModel.class);
        Model200 weather = mapper.readValue(ResourceUtils.getFile("classpath:model200_ok.json"), Model200.class);
        Map<String, Double> testCase = converter.convertLongTermModelData(model, weather);
        assertEquals(testCase.get("field_0").doubleValue(), 9795.0);
        assertEquals(testCase.get("field_1").doubleValue(), 4315.0);
        assertEquals(testCase.get("field_2").doubleValue(), 12);
        assertEquals(testCase.get("field_3").doubleValue(), 3.0);
        assertEquals(testCase.get("field_4").doubleValue(), 0);
        assertEquals(testCase.get("field_5").doubleValue(), 0);
        assertEquals(testCase.get("field_6").doubleValue(), 3.04);
        assertEquals(testCase.get("field_7").doubleValue(), 0);
        assertEquals(testCase.get("field_8").doubleValue(), 200);
    }
}