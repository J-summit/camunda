package shareniu.ch1;

import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class ShareniuStandaloneProcessEngineConfiguration extends StandaloneProcessEngineConfiguration {

    @Override
    protected void init() {
        System.out.println("#######开始init");
        super.init();
        System.out.println("#######结束init");

    }

    @Override
    public void initHistoryLevel() {
        super.initHistoryLevel();
    }
}
