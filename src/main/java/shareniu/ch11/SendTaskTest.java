package shareniu.ch11;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SendTaskTest {

    public IdentityService identityService;
    AuthorizationService authorizationService;
    ProcessEngineConfigurationImpl processEngineConfiguration;
    ManagementService managementService;
    RepositoryService repositoryService;
    RuntimeService runtimeService;
    TaskService taskService;
    HistoryService historyService;
    @Before
    public void init() {
        processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("com/shareniu/ch11/camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        identityService = processEngine.getIdentityService();
        authorizationService = processEngine.getAuthorizationService();
        managementService = processEngine.getManagementService();
        repositoryService = processEngine.getRepositoryService();
        runtimeService=processEngine.getRuntimeService();
        taskService=processEngine.getTaskService();
        historyService=processEngine.getHistoryService();
    }

    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("服务任务流程")
            .source("本地测试")
            .tenantId("a")
           // .addClasspathResource("com/shareniu/ch11/servicetask.bpmn")
            .addClasspathResource("com/shareniu/ch11/sendtask.bpmn")
            // .nameFromDeployment("101")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void startProcessInstanceByKey() {
        Map<String,Object> var=new HashMap<String, Object>();
        var.put("bean",new ValueBean("100"));
       runtimeService.startProcessInstanceByKey("sendtask",var);
        System.out.println(  ShareniuSendTaskJavaDelegate.wasExcuted);

    }



}


