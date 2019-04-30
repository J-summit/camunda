package shareniu.ch19;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ShareniuServiceTask implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        throw  new ProcessEngineException("分享牛测试异常");
    }
}
