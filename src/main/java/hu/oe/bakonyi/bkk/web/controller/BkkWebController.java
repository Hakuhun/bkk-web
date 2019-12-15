package hu.oe.bakonyi.bkk.web.controller;

import hu.oe.bakonyi.bkk.web.model.BkkRequestModel;
import hu.oe.bakonyi.bkk.web.model.BkkResponseData;
import hu.oe.bakonyi.bkk.web.scheduler.EvaluatorRefreshJob;
import hu.oe.bakonyi.bkk.web.service.BkkPredictionService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@RestController("bkk")
public class BkkWebController {

    @Autowired
    BkkPredictionService service;

    @Autowired
    EvaluatorRefreshJob evaluator;

    @PostMapping(path = "/prod/predict")
    public ResponseEntity<BkkResponseData> predict(@RequestBody BkkRequestModel data) {
        checkValidity(data);
        return ResponseEntity.ok(service.predictByLongFormat(data));
    }

    private void checkValidity(BkkRequestModel data) {
        if (data == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kötelező megadni a helyadatokat, Útadatokat, és időpontot");
        }
        if (data.getLocation() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kötelező megadni a helyadatokat");
        }
        if (data.getRouteInfo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kötelező megadni a útadatokat");
        }else{
            if(!data.getRouteInfo().getRoute().contains("BKK")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nem validált útadat");
            }
            if(!data.getRouteInfo().getStop().contains("BKK")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nem validált útadat");
            }
        }
        if (data.getTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kötelező megadni a időadatot");
        }
    }
}
