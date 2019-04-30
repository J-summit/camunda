package shareniu.ch16;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTaskPriorityTest {

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
            .createProcessEngineConfigurationFromResource("com/shareniu/ch16/camunda.cfg.xml");
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
            .addClasspathResource("com/shareniu/ch16/topic_priority.bpmn")
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
            .putValue("shareniu-2", "分享牛2");

        runtimeService.startProcessInstanceByKey("topic",variableMap);
    }
    /**
     *  select distinct RES.REV_, RES.ID_, RES.TOPIC_NAME_, RES.WORKER_ID_, RES.LOCK_EXP_TIME_, RES.RETRIES_, RES.ERROR_MSG_, RES.ERROR_DETAILS_ID_, RES.EXECUTION_ID_, RES.PROC_INST_ID_, RES.PROC_DEF_ID_, RES.PROC_DEF_KEY_, RES.ACT_ID_, RES.ACT_INST_ID_, RES.SUSPENSION_STATE_, RES.TENANT_ID_, RES.PRIORITY_, RES.BUSINESS_KEY_ from ( select RES.*, PI.BUSINESS_KEY_ from ACT_RU_EXT_TASK RES left join ACT_RU_EXECUTION PI on RES.PROC_INST_ID_ = PI.ID_ ) RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createExternalTaskQuery() {
        List<ExternalTask> list = externalTaskService
            .createExternalTaskQuery()
            .list();
        System.out.println(list);
    }

    /**
     *  select distinct RES.* from (
     *  select RES.*, PI.BUSINESS_KEY_ from ACT_RU_EXT_TASK RES left join ACT_RU_EXECUTION PI on RES.PROC_INST_ID_ = PI.ID_ WHERE (RES.LOCK_EXP_TIME_ is null or RES.LOCK_EXP_TIME_ <= ?) and (RES.SUSPENSION_STATE_ is null or RES.SUSPENSION_STATE_ = 1) and (RES.RETRIES_ is null or RES.RETRIES_ > 0) and ( RES.TOPIC_NAME_ like ? ) )
     * RES order by RES.PRIORITY_ desc LIMIT ? OFFSET ?
     */
    @Test
    public void fetchAndLock() {
        List<LockedExternalTask> list = externalTaskService
            .fetchAndLock(5, "分享牛2", true)
            .topic("topic1", 1000 * 3)
            .variables("shareniu-1")
            .execute();
        for (LockedExternalTask lockedExternalTask:list){
            System.out.println("##################");
            System.out.println(lockedExternalTask.getPriority());
            System.out.println(lockedExternalTask.getId());
            System.out.println(lockedExternalTask.getTopicName());
            VariableMap variables = lockedExternalTask.getVariables();
            System.out.println(variables);
            System.out.println("##################");
        }

    }


    @Test
    public void fetchAndLock2() {
        List<LockedExternalTask> list = externalTaskService
            .fetchAndLock(5, "分享牛2", true)
            .topic("topic1", 1000 * 3)
            .topic("topic2", 1000 * 3)
            .topic("topic3", 1000 * 3)

            .variables("shareniu-1")
            .execute();
        for (LockedExternalTask lockedExternalTask:list){
            System.out.println("##################");
            System.out.println(lockedExternalTask.getPriority());
            System.out.println(lockedExternalTask.getId());
            System.out.println(lockedExternalTask.getTopicName());
            VariableMap variables = lockedExternalTask.getVariables();
            System.out.println(variables);
            System.out.println("##################");
        }

    }
    /**
     * 21:41:10.083 [main] DEBUG o.c.b.e.i.p.e.E.selectExternalTasksForTopics - ==>  Preparing: select distinct RES.* from ( select RES.*, PI.BUSINESS_KEY_ from ACT_RU_EXT_TASK RES left join ACT_RU_EXECUTION PI on RES.PROC_INST_ID_ = PI.ID_ WHERE (RES.LOCK_EXP_TIME_ is null or RES.LOCK_EXP_TIME_ <= ?) and (RES.SUSPENSION_STATE_ is null or RES.SUSPENSION_STATE_ = 1) and (RES.RETRIES_ is null or RES.RETRIES_ > 0) and ( RES.TOPIC_NAME_ like ? and RES.PROC_DEF_ID_ = ? ) ) RES order by RES.PRIORITY_ desc LIMIT ? OFFSET ?
     */
    @Test
    public void fetchAndLock3() {
        List<LockedExternalTask> list = externalTaskService
            .fetchAndLock(5, "分享牛2", true)
            .topic("topic1", 1000 * 3)
          //  .processDefinitionId("topic:2:303")
            .processDefinitionIdIn("topic:2:303")

            .variables("shareniu-1")
            .execute();
        for (LockedExternalTask lockedExternalTask:list){
            System.out.println("##################");
            System.out.println(lockedExternalTask.getPriority());
            System.out.println(lockedExternalTask.getId());
            System.out.println(lockedExternalTask.getTopicName());
            VariableMap variables = lockedExternalTask.getVariables();
            System.out.println(variables);
            System.out.println("##################");
        }

    }




}
