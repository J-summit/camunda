package shareniu.ch1.spring;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

public class SpringProcessengineTest {

    public static void main(String[] args) {
      //  ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
       // System.out.println(defaultProcessEngine);

        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-context.xml");
        ProcessEngine processEngine = processEngineConfigurationFromResource.buildProcessEngine();
        System.out.println(processEngine);
    }
}
