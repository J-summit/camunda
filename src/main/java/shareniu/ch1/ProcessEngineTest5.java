package shareniu.ch1;

import org.camunda.bpm.engine.ProcessEngineConfiguration;

public class ProcessEngineTest5 {

    public static void main(String[] args) {
        String resource="activiti.cfg.xml";
        String beanName="processEngineConfiguration1";
//        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration
//            .createProcessEngineConfigurationFromResource(resource, beanName);
//        System.out.println(processEngineConfigurationFromResource);

        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource(resource);
        System.out.println(processEngineConfigurationFromResource);


    }

}
