package shareniu.ch1.spring;

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;

public class ShareniuSpringProcessEngineConfiguration extends SpringProcessEngineConfiguration {

    @Override
    protected void init() {
        System.out.println("#######开始init");
        super.init();
        System.out.println("#######结束init");
    }
}
