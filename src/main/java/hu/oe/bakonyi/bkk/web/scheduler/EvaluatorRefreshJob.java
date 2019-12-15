package hu.oe.bakonyi.bkk.web.scheduler;

import hu.oe.bakonyi.bkk.web.exception.BkkEvaluatorException;
import hu.oe.bakonyi.bkk.web.model.BkkEvaluator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.IOException;

@Component
@Log4j2
public class EvaluatorRefreshJob {

    @Autowired
    BkkEvaluator evaluator;

    @PostConstruct
    void init() {
        initEvaluator();
    }

    @Scheduled(fixedDelay = 300000)
    public void initEvaluator() {

        boolean complete = false;
        while (!complete) {
            try {
                evaluator.refreshModel();
                complete = true;
            } catch (JAXBException | IOException | SAXException e) {
                evaluator.setEvaluator(null);
                log.error("Hiba a szolgáltatás betöültése közben");
                throw new BkkEvaluatorException("Hiba a szolgáltatás betöültése közben");
            }
        }
    }
}
