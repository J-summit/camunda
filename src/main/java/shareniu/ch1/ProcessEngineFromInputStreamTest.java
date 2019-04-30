package shareniu.ch1;

import org.camunda.bpm.engine.ProcessEngineConfiguration;

import java.io.InputStream;

public class ProcessEngineFromInputStreamTest {

    public static void main(String[] args) {
        String resource="activiti.cfg.xml";
        InputStream inputStream = ProcessEngineFromInputStreamTest.class.getClassLoader()
            .getResourceAsStream(resource);

        String beanName="processEngineConfiguration1";


//        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration
//            .createProcessEngineConfigurationFromInputStream(inputStream,beanName);
//        System.out.println(processEngineConfigurationFromResource);

        ProcessEngineConfiguration processEngineConfigurationFromResource = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromInputStream(inputStream);
        System.out.println(processEngineConfigurationFromResource);
    }

}
