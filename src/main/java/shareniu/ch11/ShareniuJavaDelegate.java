package shareniu.ch11;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ShareniuJavaDelegate implements JavaDelegate {

    private Expression expression;
    private Expression fixed;
    public void execute(DelegateExecution execution) throws Exception {
        Object value = expression.getValue(execution);
        execution.setVariable("result",value);
        Object fixedValue = fixed.getValue(execution);
        execution.setVariable("fixedValue",fixedValue);

    }
}
