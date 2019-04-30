package shareniu.ch1;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

public class ProcessEngineTest6 {

    public static void main(String[] args) {
        String resource="activiti2.cfg.xml";
        String beanName="processEngineConfiguration1";
//        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration
//            .createProcessEngineConfigurationFromResource(resource, beanName);
//        System.out.println(processEngineConfigurationFromResource);

        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource(resource,beanName);
        System.out.println(processEngineConfigurationFromResource);
        ProcessEngine processEngine = processEngineConfigurationFromResource.buildProcessEngine();
        System.out.println(processEngine);
    }

}
