package shareniu.ch18;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class ShareniuFormFieldValidator implements FormFieldValidator {

    public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {

        if (null==submittedValue){
            return  true;
        }

        if (submittedValue.equals("a")||submittedValue.equals("b")){
            return  true;
        }
        if (submittedValue.equals("c")){
            throw  new FormFieldValidationException("出现异常了，校验不通过");
        }


        return false;
    }
}
