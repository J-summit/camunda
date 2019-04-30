package shareniu.ch13;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GateWayTest {

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
            .createProcessEngineConfigurationFromResource("com/shareniu/ch13/camunda.cfg.xml");
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
            .name("测试排他网关流程")
            .source("本地测试")
            .tenantId("a")
           // .addClasspathResource("com/shareniu/ch11/receicetask.bpmn")
         //   .addClasspathResource("com/shareniu/ch13/gateway1.bpmn")
            .addClasspathResource("com/shareniu/ch13/gateway2.bpmn")
            // .nameFromDeployment("101")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }
    /**
     * insert into ACT_HI_PROCINST ( ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_KEY_, PROC_DEF_ID_, START_TIME_, END_TIME_, REMOVAL_TIME_, DURATION_, START_USER_ID_, START_ACT_ID_, END_ACT_ID_, SUPER_PROCESS_INSTANCE_ID_, ROOT_PROC_INST_ID_, SUPER_CASE_INSTANCE_ID_, CASE_INST_ID_, DELETE_REASON_, TENANT_ID_, STATE_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 1:	insert into ACT_HI_ACTINST ( ID_, PARENT_ACT_INST_ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, CALL_CASE_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, ACT_INST_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1, 1]
     * Result 2:	insert into ACT_RU_EXECUTION ( ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_ID_, ACT_ID_, ACT_INST_ID_, IS_ACTIVE_, IS_CONCURRENT_, IS_SCOPE_, IS_EVENT_SCOPE_, PARENT_ID_, SUPER_EXEC_, SUPER_CASE_EXEC_, CASE_INST_ID_, SUSPENSION_STATE_, CACHED_ENT_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1, 1]
     * Result 3:	insert into ACT_RU_EVENT_SUBSCR ( ID_, EVENT_TYPE_, EVENT_NAM
     */
    @Test
    public void startProcessInstanceByKey() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("day",2);//传递2 走到分享牛1节点
       runtimeService.startProcessInstanceByKey("gateway1",vars);
    }
    @Test
    public void startProcessInstanceByKey2() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("day",4);//传递4 走到分享牛1节点
       runtimeService.startProcessInstanceByKey("gateway1",vars);
    }

    @Test
    public void startProcessInstanceByKey3() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("day",6);//传递4 走到分享牛1节点
        runtimeService.startProcessInstanceByKey("gateway1",vars);
    }




///////////////////并行网管测试

    @Test
    public void createDeployment3() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("测试排他网关流程")
            .source("本地测试")
            .tenantId("a")
            // .addClasspathResource("com/shareniu/ch11/receicetask.bpmn")
            //   .addClasspathResource("com/shareniu/ch13/gateway1.bpmn")
            .addClasspathResource("com/shareniu/ch13/gateway4.bpmn")
            // .nameFromDeployment("101")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void startProcessInstanceByKey4() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("day",2);//传递2 走到分享牛1节点
        runtimeService.startProcessInstanceByKey("gateway1",vars);
    }
    @Test
    public void complete() {
        taskService.complete("111");
    }





    ///////////////////并行网管测试




    ///////////////////兼容网管测试

    @Test
    public void createDeployment4() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("测试排他网关流程")
            .source("本地测试")
            .tenantId("a")
            .addClasspathResource("com/shareniu/ch13/gateway4.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void startProcessInstanceByKey5() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("money",200);//传递200 走到采购员
        runtimeService.startProcessInstanceByKey("gateway1",vars);
    }

    @Test
    public void startProcessInstanceByKey6() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("money",400);//传递400两个都满足条件
        runtimeService.startProcessInstanceByKey("gateway1",vars);
    }
    @Test
    public void complete2() {
        taskService.complete("709");
    }





    ///////////////////兼容网管测试

}
