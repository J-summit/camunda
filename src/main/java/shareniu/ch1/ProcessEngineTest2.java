package shareniu.ch1;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;

import java.util.Map;

/**
 * deployment.lock	0	1
 * history.cleanup.job.lock	0	1
 * historyLevel	2	1
 * next.dbid	1	1
 * schema.history	create(fox)	1
 * startup.lock	0	1
 */
public class ProcessEngineTest2 {

    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();

        Map<Object, Object> beans = processEngineConfiguration.getBeans();
        Object testA = beans.get("testA");
        System.out.println(testA);
        boolean testA1 = beans.containsKey("testA");
        System.out.println(testA1);
         testA1 = beans.containsKey("testA1");
        System.out.println(testA1);
        beans.clear();
    }

}
