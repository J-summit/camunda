package shareniu.ch17;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricDetail;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.type.PrimitiveValueTypeImpl;
import org.camunda.bpm.engine.variable.value.BooleanValue;
import org.camunda.bpm.engine.variable.value.DoubleValue;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VarTest {

    public IdentityService identityService;
    AuthorizationService authorizationService;
    ProcessEngineConfigurationImpl processEngineConfiguration;
    ManagementService managementService;
    RepositoryService repositoryService;
    RuntimeService runtimeService;
    TaskService taskService;
    HistoryService historyService;
    FilterService filterService;
    ExternalTaskService externalTaskService;
    @Before
    public void init() {
        processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("com/shareniu/ch17/camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        identityService = processEngine.getIdentityService();
        authorizationService = processEngine.getAuthorizationService();
        managementService = processEngine.getManagementService();
        repositoryService = processEngine.getRepositoryService();
        runtimeService=processEngine.getRuntimeService();
        taskService=processEngine.getTaskService();
        historyService=processEngine.getHistoryService();
        filterService=processEngine.getFilterService();
        externalTaskService=processEngine.getExternalTaskService();
    }

    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("测试消息启动事件流程")
            .source("本地测试")
            .tenantId("a")
           // .addClasspathResource("com/shareniu/ch17/var.bpmn")
            .addClasspathResource("com/shareniu/ch17/gateway3.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    /**
     * 0:	insert into ACT_HI_PROCINST ( ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_KEY_, PROC_DEF_ID_, START_TIME_, END_TIME_, REMOVAL_TIME_, DURATION_, START_USER_ID_, START_ACT_ID_, END_ACT_ID_, SUPER_PROCESS_INSTANCE_ID_, ROOT_PROC_INST_ID_, SUPER_CASE_INSTANCE_ID_, CASE_INST_ID_, DELETE_REASON_, TENANT_ID_, STATE_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 1:	insert into ACT_HI_EXT_TASK_LOG ( ID_, TIMESTAMP_, EXT_TASK_ID_, RETRIES_, TOPIC_NAME_, WORKER_ID_, PRIORITY_, ERROR_MSG_, ERROR_DETAILS_ID_, ACT_ID_, ACT_INST_ID_, EXECUTION_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, PROC_DEF_ID_, PROC_DEF_KEY_, TENANT_ID_, STATE_, REMOVAL_TIME_ ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 2:	insert into ACT_HI_ACTINST ( ID_, PARENT_ACT_INST_ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, CALL_CASE_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, ACT_INST_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1, 1]
     * Result 3:	insert into ACT_RU_EXECUTION ( ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_ID_, ACT_ID_, ACT_INST_ID_, IS_ACTIVE_, IS_CONCURRENT_, IS_SCOPE_, IS_EVENT_SCOPE_, PARENT_ID_, SUPER_EXEC_, SUPER_CASE_EXEC_, CASE_INST_ID_, SUSPENSION_STATE_, CACHED_ENT_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1, 1]
     * Result 4:	insert into ACT_RU_EXT_TASK ( ID_, WORKER_ID_, TOPIC_NAME_, LOCK_EXP_TIME_, RETRI
     */
    @Test
    public void startProcessInstanceByKey() {
        Map<String,Object> vars=new HashMap<String, Object>();
        vars.put("priority",200);
        VariableMap variableMap = Variables.createVariables()
            .putValue("priority", 200)
            .putValue("shareniu-1", "分享牛1")
            .putValue("shareniu-2", "分享牛2")
            .putValue("person",new Person(18,"分享牛"));

        runtimeService.startProcessInstanceByKey("gateway1",variableMap);
    }


    @Test
    public void complete3() {
        VariableMap variableMap = Variables.createVariables()
            .putValue("priority", 200)
            .putValue("shareniu-1", "分享牛1"+"1")
            .putValue("shareniu-2", "分享牛2_1")
            .putValue("person",new Person(19,"分享牛1"));

       taskService.complete("1715",variableMap);
    }


    @Test
    public void runtimeServicegetVariables() {
        String executionId="1701";
        Map<String, Object> variables = runtimeService.getVariables("1701");
        System.out.println(variables);
        Object priority = runtimeService.getVariable("1701", "priority");
        System.out.println("########"+priority);
        List<String> list=new ArrayList<String>();
        list.add("priority");
        list.add("shareniu-2");
        Map<String, Object> variables1 = runtimeService.getVariables(executionId, list);
        System.out.println("########"+variables1);

        VariableMap variablesTyped = runtimeService.getVariablesTyped(executionId);
        Object priority1 = variablesTyped.get("priority");
        System.out.println("########"+priority1);

        Object person = variablesTyped.get("person");
        System.out.println("#######"+person);

       // VariableMap variablesTyped1 = runtimeService.getVariablesTyped(executionId, false);
        // person = variablesTyped1.get("person");
       // System.out.println("#######"+person);
    }

    @Test
    public void taskServiceServicegetVariables() {
        String taskId="1809";
        Map<String, Object> variables = taskService.getVariables(taskId);
        System.out.println(variables);
        Object priority = taskService.getVariable(taskId, "priority");
        System.out.println("########"+priority);
        List<String> list=new ArrayList<String>();
        list.add("priority");
        list.add("shareniu-2");
        Map<String, Object> variables1 = taskService.getVariables(taskId, list);
        System.out.println("########"+variables1);

        VariableMap variablesTyped = taskService.getVariablesTyped(taskId);
        Object priority1 = variablesTyped.get("priority");
        System.out.println("########"+priority1);

        Object person = variablesTyped.get("person");
        System.out.println("#######"+person);
        // VariableMap variablesTyped1 = runtimeService.getVariablesTyped(executionId, false);
        // person = variablesTyped1.get("person");
        // System.out.println("#######"+person);
    }

    /**
     * insert into ACT_HI_VARINST ( ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_INST_ID_, TENANT_ID_, CASE_DEF_KEY_, CASE_DEF_ID_, CASE_INST_ID_, CASE_EXECUTION_ID_, TASK_ID_, NAME_, REV_, VAR_TYPE_, CREATE_TIME_, REMOVAL_TIME_, BYTEARRAY_ID_, DOUBLE_, LONG_, TEXT_, TEXT2_, STATE_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 1:	insert into ACT_HI_DETAIL ( ID_, TYPE_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_INST_ID_, CASE_DEF_KEY_, CASE_DEF_ID_, CASE_INST_ID_, CASE_EXECUTION_ID_, TASK_ID_, NAME_, REV_, VAR_INST_ID_, VAR_TYPE_, TIME_, BYTEARRAY_ID_, DOUBLE_, LONG_, TEXT_, TEXT2_, SEQUENCE_COUNTER_, TENANT_ID_, OPERATION_ID_, REMOVAL_TIME_ ) values ( ?, 'VariableUpdate', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 2:	insert into ACT_RU_VARIABLE ( ID_, TYPE_, NA
     */
    @Test
    public void runtimeServicesetVariable() {
        String executionId="1701";
        VariableMap variableMap = Variables.createVariables()
            .putValue("priority", 200)
            .putValue("shareniu-1", "分享牛1"+"1")
            .putValue("shareniu-2", "分享牛2_1")
            .putValue("person",new Person(19,"分享牛1"));

        runtimeService.setVariable(executionId,"分享牛1","分享牛1");
        runtimeService.setVariables(executionId,variableMap);
    }
    @Test
    public void taskServicesetVariable() {
        String taskId="1809";
      taskService.setVariable(taskId,"shareniu-1", "分享牛1"+"1");
        VariableMap variableMap = Variables.createVariables()
            .putValue("priority", 200)
            .putValue("shareniu-1", "分享牛1"+"1")
            .putValue("shareniu-2", "分享牛2_1")
            .putValue("person",new Person(19,"分享牛1"));
        taskService.setVariables(taskId,variableMap);

    }
    /**
     * select distinct RES.* from ACT_HI_DETAIL RES WHERE RES.ACT_INST_ID_ = ? orde
     */
    @Test
    public void createHistoricDetailQuery() {
        List<HistoricDetail> list = historyService
            .createHistoricDetailQuery()
   .activityInstanceId("Task_1usod4v:1714")
           // .processInstanceId("1701")
            .list();
        for (HistoricDetail historicDetail :list){
            System.out.println("##################");
            System.out.println(historicDetail.getId());
            System.out.println(historicDetail.getExecutionId());
            System.out.println(historicDetail.getActivityInstanceId());
            System.out.println("##################");

        }

    }


    @Test
    public void createHistoricVariableInstanceQuery() {
        List<HistoricVariableInstance> list = historyService
            .createHistoricVariableInstanceQuery()
            .list();
        for (HistoricVariableInstance historicDetail :list){
            System.out.println("##################");
            System.out.println(historicDetail.getId());
            System.out.println(historicDetail.getExecutionId());
            System.out.println(historicDetail.getActivityInstanceId());
            System.out.println("##################");

        }

    }


    @Test
    public void setVariable() {
        String executionId="1701";

        StringValue shareniu11 = Variables.stringValue("shareniu11", true);
        BooleanValue booleanValue = Variables.booleanValue(false, false);
        runtimeService.setVariable(executionId,"shareniu",booleanValue);
    }

    @Test
    public void setVariableLocal() {
        String executionId="1701";
        StringValue shareniu11 = Variables.stringValue("shareniu11", true);
        BooleanValue booleanValue = Variables.booleanValue(false, false);
        runtimeService.setVariableLocal(executionId,"shareniu",booleanValue);
    }
    @Test
    public void setVariable2() {
        String executionId="1701";
        runtimeService.setVariable(executionId,"priority","100");
    }


    @Test
    public void setVariable3() {
        String executionId="604";
        runtimeService.setVariable(executionId,"priority4",100D);
    }
    @Test
    public void setVariable4() {
        String executionId="604";
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("transient",false);
        PrimitiveValueTypeImpl.DoubleTypeImpl doubleType = new PrimitiveValueTypeImpl.DoubleTypeImpl();
        DoubleValue doubleValue = doubleType.createValue(12D, map);
        Variables.doubleValue(13D,true);

        runtimeService.setVariable(executionId,"priority4",doubleValue);
    }

    @Test
    public void setVariable5() {
        String executionId="604";
        VariableMap variableMap = Variables.createVariables()
            .putValue("shareniu-1", Variables.doubleValue(11D, true))
            .putValue("shareniu-2", Variables.doubleValue(11D, false))
            .putValue("shareniu-3", Variables.stringValue("a", true));

        runtimeService.setVariables(executionId,variableMap);
    }




    @Test
    public void complete4() {
        VariableMap variableMap = Variables.createVariables()
            .putValue("priority5", 200);

        taskService.complete("2618",variableMap);
    }

}
