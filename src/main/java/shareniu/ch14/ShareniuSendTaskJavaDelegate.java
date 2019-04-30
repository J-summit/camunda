package shareniu.ch14;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ShareniuSendTaskJavaDelegate implements JavaDelegate {

    public  static  boolean wasExcuted=false;
    public void execute(DelegateExecution execution) throws Exception {
        wasExcuted=true;
    }
}
