package shareniu.ch11;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ReceiveTaskTest {

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
            .name("接受任务流程")
            .source("本地测试")
            .tenantId("a")
           // .addClasspathResource("com/shareniu/ch11/receicetask.bpmn")
            .addClasspathResource("com/shareniu/ch11/receicetask_msg.bpmn")
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
       runtimeService.startProcessInstanceByKey("receiveTask");
    }
    /**
     * reparing: insert into ACT_HI_ACTINST ( ID_, PARENT_ACT_INST_ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, CALL_CASE_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, ACT_INST_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * 01:18:07.649 [main] DEBUG o.c.b.e.i.p.e.H.insertHistoricActivityInstanceEvent - ==> Parameters: Task_13u9ct3:201(String), 101(String), receiveTask(String), receiveTask:1:3(String), 101(String), 101(String), 101(String), Task_13u9ct3(String), null, null, null, 给总经理发短信(String), receiveTask(String), null, 2019-02-17 01:18:07.638(Timestamp), null, null, 0(Integer), 5(Long), a(String), null
     * 01:18:07.650 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'UPDATE'; Entity: 'ExecutionEntity[id=101]'
     * 01:18:07.650 [main] DEBUG o.c.b.e.i.p.e.E.updateExecution - ==>  Preparing: update ACT_RU_EXECUTION set REV_ = ?, PROC_DEF_ID_ = ?, BUSINESS_KEY_ = ?, ACT_ID_ = ?, ACT_INST_ID_ = ?, IS_ACTIVE_ = ?, IS_CONCURRENT_ = ?, IS_SCOPE_ = ?, IS_EVENT_SCOPE_ = ?, PARENT_ID_ = ?, SUPER_EXEC_ = ?, SUSPENSION_STATE_ = ?, CACHED_ENT_STATE_ = ?, SEQUENCE_COUNTER_ = ?, TENANT_ID_ = ? where ID_ = ? and REV_ = ?
     * 01:18:07.651 [main] DEBUG o.c.b.e.i.p.e.E.updateExecution - ==> Parameters: 2(Integer), receiveTask:1:3(String), null, Task_13u9ct3(String), Task_13u9ct3:201(String), true(Boolean), false(Boolean), true(Boolean), false(Boolean), null, null, 1(Integer), 0(Integer), 5(Long), a(String), 101(String), 1(Integer)
     * 01:18:07.651 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'UPDATE'; Entity: 'HistoricActivityInstanceEventEntity[id=Task_08uny2s:103]'
     * 01:18:07.685 [main] DEBUG o.c.b.e.i.p.e.H.updateHistoricActivityInstanceEvent - ==>  Preparing: UPDATE ACT_HI_ACTINST SET EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, ACT_ID_ = ?, ACT_NAME_ = ?, ACT_TYPE_ = ?, PARENT_ACT_INST_ID_ = ? , END_TIME_ = ? , DURATION_ = ? , ACT_INST_STATE_ = ? WHERE ID_ = ?
     * 01:18:07.686 [main] DEBUG o.c.b.e.i.p.e.H.updateHistoricActivityInstanceEvent - ==> Parameters: 101(String), receiveTask(String), receiv
     */
    @Test
    public void signal() {
        String executionId="101";
        //获取当日的销售额度 完成自己的一些业务，完成之后，触发实例继续往下流转
       runtimeService.signal(executionId);
    }

    /**
     *
     * select distinct RES.*
     * from ACT_RU_EXECUTION RES
     * inner join ACT_RE_PROCDEF P
     * on RES.PROC_DEF_ID_ = P.ID_
     * WHERE RES.ACT_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     * 01:22:12.341 [main] DEBUG o.c.b.e.i.p.e.E.selectExecutionsByQueryCriteria - ==> Parameters: Task_08uny2s(String), 2147483647(Integer), 0(Integer)
     */

    @Test
    public void createExecutionQuery() {
        Execution execution = runtimeService
            .createExecutionQuery()
            .activityId("Task_08uny2s")
            .singleResult();
        System.out.println(execution);
    }

    /**
     * org.camunda.bpm.engine.impl.pvm.PvmException: cannot signal execution 601: it has no current activity

     insert into ACT_HI_ACTINST ( ID_, PARENT_ACT_INST_ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, CALL_CASE_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, ACT_INST_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     Result 1:	delete from ACT_RU_EVENT_SUBSCR where ID_ = ? and REV_ = ?	Update counts: [1]
     Result 2:	update ACT_RU_EXECUTION set REV_ = ?, PROC_DEF_ID_ = ?, BUSINESS_KEY_ = ?, ACT_ID_ = ?, ACT_INST_ID_ = ?, IS_ACTIVE_ = ?, IS_CONCURRENT_ = ?, IS_SCOPE_ = ?, IS_EVENT_SCOPE_ = ?, PARENT_ID_ = ?, SUPER_EXEC_ = ?, SUSPENSION_STATE_ = ?, CACHED_ENT_STATE_ = ?, SEQUENCE_COUNTER_ = ?, TENANT_ID_ = ? where ID_ = ? and REV_ = ?	Update counts: [1]
     Result 3:	delete from ACT_RU_EXECUTION where ID_ = ? and REV_ = ?	Update counts: [1]
     Result 4:	UPDATE ACT_HI_ACTINST SET EXECUTION_ID_ = ?, PROC_
     */
    @Test
    public void signalMsg() {
        String executionId="603";
        //获取当日的销售额度 完成自己的一些业务，完成之后，触发实例继续往下流转
        runtimeService.signal(executionId);
    }


    @Test
    public void messageEventReceived() {
        String executionId="803";
        String messageName="newInnvoice";
        runtimeService.messageEventReceived(messageName,executionId);
    }


    @Test
    public void correlateMessage() {
        String messageName="newInnvoice";
        runtimeService.correlateMessage(messageName);
    }

    /**
     * 01:42:57.693 [main] DEBUG o.c.b.e.i.p.e.E.selectEventSubscriptionByQueryCriteria - ==>  Preparing: select distinct RES.* from
     * ACT_RU_EVENT_SUBSCR RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void correlateMessageAndcreateEventSubscriptionQuery() {
        runtimeService.startProcessInstanceByKey("receiveTask","23");
        runtimeService.startProcessInstanceByKey("receiveTask","42");

        List<EventSubscription> list = runtimeService.createEventSubscriptionQuery()
            .list();
        System.out.println(list.size());

        runtimeService.correlateMessage("newInnvoice","23");
        list = runtimeService.createEventSubscriptionQuery()
            .list();
        System.out.println(list.size());

        runtimeService.correlateMessage("newInnvoice","42");
        list = runtimeService.createEventSubscriptionQuery()
            .list();
        System.out.println(list.size());

    }
    /**
     *
     *
     * org.camunda.bpm.engine.MismatchingMessageCorrelationException: ENGINE-13031 Cannot correlate a message with name 'newInnvoice' to a single execution. 2 executions match the correlation keys: CorrelationSet [businessKey=null, processInstanceId=null, processDefinitionId=null, correlationKeys=null, localCorrelationKeys=null, tenantId=null, isTenantIdSet=false]
     */

    @Test
    public void correlateMessageAndcreateEventSubscriptionQuery2() {
        runtimeService.startProcessInstanceByKey("receiveTask","23");
        runtimeService.startProcessInstanceByKey("receiveTask","42");

        List<EventSubscription> list = runtimeService.createEventSubscriptionQuery()
            .list();
        System.out.println(list.size());
        runtimeService.correlateMessage("newInnvoice");
        list = runtimeService.createEventSubscriptionQuery()
            .list();
        System.out.println(list.size());


    }


}


