package shareniu.ch15;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.junit.Before;
import org.junit.Test;

public class SingnalThrowAndBoundayNonCancleTest {

    public IdentityService identityService;
    AuthorizationService authorizationService;
    ProcessEngineConfigurationImpl processEngineConfiguration;
    ManagementService managementService;
    RepositoryService repositoryService;
    RuntimeService runtimeService;
    TaskService taskService;
    HistoryService historyService;
    FilterService filterService;
    @Before
    public void init() {
        processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("com/shareniu/ch15/camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        identityService = processEngine.getIdentityService();
        authorizationService = processEngine.getAuthorizationService();
        managementService = processEngine.getManagementService();
        repositoryService = processEngine.getRepositoryService();
        runtimeService=processEngine.getRuntimeService();
        taskService=processEngine.getTaskService();
        historyService=processEngine.getHistoryService();
        filterService=processEngine.getFilterService();
    }

    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("测试消息启动事件流程")
            .source("本地测试")
            .tenantId("a")
            .addClasspathResource("com/shareniu/ch15/singnal_throw.bpmn")
            .addClasspathResource("com/shareniu/ch15/singnal_boundyNoCancle.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void signalEventReceived() {
     runtimeService.signalEventReceived("alert");
    }

    @Test
    public void startProcessInstanceByKey() {
        runtimeService.startProcessInstanceByKey("boundyNoCancle");
    }
    @Test
    public void startProcessInstanceByKeysingnalThrow() {
        runtimeService.startProcessInstanceByKey("singnalThrow");
    }





    @Test
    public void complete() {
      taskService.complete("210");
    }






}
