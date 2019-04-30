package shareniu.ch1;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;

public class ShareniuProcessEnginePlugin implements ProcessEnginePlugin {

    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        System.out.println("preInit##############"+processEngineConfiguration);
//        HistoryLevel historyLevel = processEngineConfiguration.getHistoryLevel();
//        if (historyLevel.getId()!=1){
//            throw  new RuntimeException("historyLevel");
//        }
    }
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        System.out.println("postInit##############"+processEngineConfiguration);
    }
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        System.out.println("postProcessEngineBuild##############"+processEngine);

    }
}
