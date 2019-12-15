package hu.oe.bakonyi.bkk.web.model;

import hu.oe.bakonyi.bkk.web.exception.BkkEvaluatorException;
import lombok.Data;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@ApplicationScope
@Service
@Data
public class BkkEvaluator {

    @Value("${longTermModelPmml}")
    private String pmmlPath;

    private Evaluator evaluator;

    public void refreshModel() throws JAXBException, IOException, SAXException {
        setUpEvaluator();
    }

    private PMML getPMML() throws IOException, JAXBException, SAXException {
        PMML pmml = null;

        try (InputStream is = new FileInputStream(pmmlPath)) {
            return PMMLUtil.unmarshal(is);
        }
    }

    private void setUpEvaluator() throws JAXBException, IOException, SAXException {
        ModelEvaluatorBuilder modelEvaluatorBuilder = new ModelEvaluatorBuilder(getPMML());
        evaluator = modelEvaluatorBuilder.build();
        evaluator.verify();
    }

    public Map<FieldName, Double> evaluate(Map<FieldName, FieldValue> arguments) {
        if (evaluator != null) {
            return (Map<FieldName, Double>) evaluator.evaluate(arguments);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    public List<InputField> getInputFields() {
        if (evaluator != null) {
            return evaluator.getInputFields();
        }
        throw new BkkEvaluatorException("Hiba a szolgáltatás betöültése közben");
    }

    public List<? extends TargetField> getTargetFields() {
        if (evaluator != null) {
            return evaluator.getTargetFields();
        }
        throw new BkkEvaluatorException("Hiba a szolgáltatás betöültése közben");
    }

    public List<? extends OutputField> getOutputFields() {
        if (evaluator != null) {
            return evaluator.getOutputFields();
        }
        throw new BkkEvaluatorException("Hiba a szolgáltatás betöültése közben");
    }

    public void checkEvaluatorAvailability() {
        if (evaluator == null) {
            throw new BkkEvaluatorException("Hiba a szolgáltatás betöültése közben");
        }
    }

}
