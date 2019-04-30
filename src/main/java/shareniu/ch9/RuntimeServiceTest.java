package shareniu.ch9;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.runtime.ProcessInstantiationBuilder;
import org.camunda.bpm.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RuntimeServiceTest {

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
            .createProcessEngineConfigurationFromResource("com/shareniu/ch9/camunda.cfg.xml");
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
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("a")
            .addClasspathResource("com/shareniu/ch9/leaveinitiator.bpmn")
            // .nameFromDeployment("101")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    /**
     * insert into ACT_HI_TASKINST ( ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, CASE_DEF_KEY_, CASE_DEF_ID_, CASE_INST_ID_, CASE_EXECUTION_ID_, ACT_INST_ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, OWNER_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, DELETE_REASON_, TASK_DEF_KEY_, PRIORITY_, DUE_DATE_, FOLLOW_UP_DATE_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 1:	insert into ACT_HI_PROCINST ( ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_KEY_, PROC_DEF_ID_, START_TIME_, END_TIME_, REMOVAL_TIME_, DURATION_, START_USER_ID_, START_ACT_ID_, END_ACT_ID_, SUPER_PROCESS_INSTANCE_ID_, ROOT_PROC_INST_ID_, SUPER_CASE_INSTANCE_ID_, CASE_INST_ID_, DELETE_REASON_, TENANT_ID_, STATE_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 2:	insert into ACT_HI_IDENTITYLINK ( ID_, TIMESTAMP_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, ROOT_PROC_INST_ID_, PROC_DEF_ID_, OPERATION_TYPE_, ASSIGNER_ID_, PROC_DEF_KEY_, TENANT_ID_, REMOVAL_TIME_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 3:	insert into ACT_HI_ACTINST ( ID_, PARENT_ACT_INST_ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, CALL_CASE_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, ACT_INST_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1, 1]
     * Result 4:	insert into ACT_RU_EXECUTION ( ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, BUSINESS_KEY_, PROC_DEF_ID_, ACT_ID_, ACT_INST_ID_, IS_ACTIVE_, IS_CONCURRENT_, IS_SCOPE_, IS_EVENT_SCOPE_, PARENT_ID_, SUPER_EXEC_, SUPER_CASE_EXEC_, CASE_INST_ID_, SUSPENSION_STATE_, CACHED_ENT_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     * Result 5:	insert into ACT_RU_TASK ( ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_
     */
    @Test
    public void startProcessInstanceByKey() {
        String processDefinitionKey="leave";
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId()+","+processInstance.getBusinessKey()+","+processInstance.getProcessDefinitionId());


    }
    /**
     * select distinct RES.REV_, RES.ID_, RES.NAME_,
     * RES.PARENT_TASK_ID_, RES.DESCRIPTION_, RES.PRIORITY_,
     * RES.CREATE_TIME_, RES.OWNER_, RES.ASSIGNEE_,
     * RES.DELEGATION_, RES.EXECUTION_ID_, RES.PROC_INST_ID_,
     * RES.PROC_DEF_ID_, RES.CASE_EXECUTION_ID_,
     * RES.CASE_INST_ID_, RES.CASE_DEF_ID_, RES.TASK_DEF_KEY_,
     * RES.DUE_DATE_, RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_, RES.TENANT_ID_
     *
     * from ACT_RU_TASK RES WHERE ( 1 = 1 and RES.ASSIGNEE_ = ? )
     * order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createTaskQuery() {
        String assignee="张三";
        List<Task> list = taskService.createTaskQuery()
            .taskAssignee(assignee)
            .list();
        for (Task task:list){
            System.out.println(task.getId());
        }

    }
    /**
     * 	insert into ACT_HI_TASKINST ( ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, CASE_DEF_KEY_, CASE_DEF_ID_, CASE_INST_ID_, CASE_EXECUTION_ID_, ACT_INST_ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, OWNER_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, DELETE_REASON_, TASK_DEF_KEY_, PRIORITY_, DUE_DATE_, FOLLOW_UP_DATE_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 1:	insert into ACT_HI_IDENTITYLINK ( ID_, TIMESTAMP_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, ROOT_PROC_INST_ID_, PROC_DEF_ID_, OPERATION_TYPE_, ASSIGNER_ID_, PROC_DEF_KEY_, TENANT_ID_, REMOVAL_TIME_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 2:	insert into ACT_HI_ACTINST ( ID_, PARENT_ACT_INST_ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, CALL_CASE_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, ACT_INST_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 3:	insert into ACT_RU_TASK ( ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, PRIORITY_, CREATE_TIME_, OWNER_, ASSIGNEE_, DELEGATION_, EXECUTION_ID_, PROC_INST_ID_, PROC_DEF_ID_, CASE_EXECUTION_ID_, CASE_INST_ID_, CASE_DEF_ID_, TASK_DEF_KEY_, DUE_DATE_, FOLLOW_UP_DATE_, SUSPENSION_STATE_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     * Result 4:	delete from ACT_RU_TASK where ID_ = ? and REV_ = ?	Update counts: [1]
     * Result 5:	update ACT_RU_EXECUTION set REV_ = ?, PROC_DEF_ID_ = ?, BUSINESS_KEY_ = ?, ACT_ID_ = ?, ACT_INST_ID_ = ?, IS_ACTIVE_ = ?, IS_CONCURRENT_ = ?, IS_SCOPE_ = ?, IS_EVENT_SCOPE_ = ?, PARENT_ID_ = ?, SUPER_EXEC_ = ?, SUSPENSION_STATE_ = ?, CACHED_ENT_STATE_ = ?, SEQUENCE_COUNTER_ = ?, TENANT_ID_ = ? where ID_ = ? and REV_ = ?	Update counts: [1]
     * Result 6:	UPDATE ACT_HI_ACTINST SET EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, ACT_ID_ = ?, ACT_NAME_ = ?, ACT_TYPE_ = ?, PARENT_ACT_INST_ID_ = ? , END_TIME_ = ? , DURATION_ = ? , ACT_INST_STATE_ = ? WHERE ID_ = ?	Update counts: [1]
     * Result 7:	update ACT_HI_TASKINST set EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, NAME_ = ?,
     */
    @Test
    public void complete() {
        String taskId="2107";
       taskService.complete(taskId);

    }
    /**
     * "Task_08uny2s" -> "Activity(Task_08uny2s)"
     */
    @Test
    public void createProcessInstanceByKey() {
        String processDefinitionKey="leave";
        String activityId="Task_1qmwv3d";
        ProcessInstantiationBuilder processInstantiationBuilder = runtimeService.createProcessInstanceByKey(processDefinitionKey);
        ProcessInstance processInstance = processInstantiationBuilder.businessKey("001")
            .startBeforeActivity(activityId)
            .execute();

        System.out.println(processInstance.getId()+","+processInstance.getBusinessKey()+","+processInstance.getProcessDefinitionId());


    }
    @Test
    public void createProcessInstanceByKey2() {
        String processDefinitionKey="leave";
        ProcessInstantiationBuilder processInstantiationBuilder = runtimeService.createProcessInstanceByKey(processDefinitionKey);
        ProcessInstance processInstance = processInstantiationBuilder.businessKey("001")
            .startTransition("SequenceFlow_0arnx9g")
            .execute();

        System.out.println(processInstance.getId()+","+processInstance.getBusinessKey()+","+processInstance.getProcessDefinitionId());


    }

    @Test
    public void createProcessInstanceByKey3() {
        String processDefinitionKey="leave";
        ProcessInstantiationBuilder processInstantiationBuilder = runtimeService.createProcessInstanceByKey(processDefinitionKey);
        ProcessInstance processInstance = processInstantiationBuilder.businessKey("001")
            .startAfterActivity("Task_1qmwv3d")
            .execute();
        System.out.println(processInstance.getId()+","+processInstance.getBusinessKey()+","+processInstance.getProcessDefinitionId());

    }
    @Test
    public void createProcessInstanceByKey4() {
        String processDefinitionKey="leave";
        ProcessInstantiationBuilder processInstantiationBuilder = runtimeService.createProcessInstanceByKey(processDefinitionKey);
        ProcessInstance processInstance = processInstantiationBuilder.businessKey("001")
            .startAfterActivity("Task_1qmwv3d")
            .execute(true,true);
        System.out.println(processInstance.getId()+","+processInstance.getBusinessKey()+","+processInstance.getProcessDefinitionId());

    }
    /**
     * select distinct RES.* from ACT_RU_EXECUTION RES inner join
     * ACT_RE_PROCDEF P on RES.PROC_DEF_ID_ = P.ID_ WHERE
     *
     * RES.PARENT_ID_ is null and RES.PROC_INST_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createProcessInstanceQuery() {
        String processInstanceId="801";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
        if (processInstance==null){
            System.out.println("当前的实例已经结束了");
        }else{
            System.out.println("当前的实例正在运转");
        }
    }
    /**
     * select distinct RES.* from
     * ( SELECT SELF.*, DEF.NAME_,
     * DEF.VERSION_ FROM ACT_HI_PROCINST
     * SELF LEFT JOIN ACT_RE_PROCDEF DEF ON SELF.PROC_DEF_ID_ = DEF.ID_
     * WHERE SELF.PROC_INST_ID_ = ? and STATE_ = ? )
     * RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createHistoricProcessInstanceQuery() {
        String processInstanceId="801";
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
        if (historicProcessInstance.getEndTime()!=null){
            System.out.println("当前的实例已经结束了");
        }else{
            System.out.println("当前的实例正在运转");
        }
    }
    /**
     * select RES.* FROM ACT_HI_ACTINST
     * RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createHistoricActivityInstanceQuery() {
        String processInstanceId="801";
        List<HistoricActivityInstance> historicProcessInstance = historyService
            .createHistoricActivityInstanceQuery()
            .processInstanceId(processInstanceId)
            .list();
        System.out.println(historicProcessInstance);
    }
    /**
     * select distinct RES.* from ACT_HI_TASKINST
     * RES WHERE RES.PROC_INST_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createHistoricTaskInstanceQuery() {
        String processInstanceId="801";
        List<HistoricTaskInstance> historicProcessInstance = historyService
            .createHistoricTaskInstanceQuery()
            .processInstanceId(processInstanceId)
            .list();
        System.out.println(historicProcessInstance);
    }



    @Test
    public void setAuthenticatedUserId() {
        identityService.setAuthenticatedUserId("分享牛4");
        runtimeService.startProcessInstanceById("leave:5:1903");

    }

    /**
     * delete from ACT_RU_IDENTITYLINK where ID_ = ?	Update counts: [1]
     * Result 1:	delete from ACT_RU_TASK where ID_ = ? and REV_ = ?	Update counts: [1]
     * Result 2:	delete from ACT_RU_EXECUTION where ID_ = ? and REV_ = ?	Update counts: [1]
     * Result 3:	UPDATE ACT_HI_ACTINST SET EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, ACT_ID_ = ?, ACT_NAME_ = ?, ACT_TYPE_ = ?, PARENT_ACT_INST_ID_ = ? , END_TIME_ = ? , DURATION_ = ? , ACT_INST_STATE_ = ? WHERE ID_ = ?	Update counts: [1]
     * Result 4:	update ACT_HI_PROCINST set PROC_DEF_ID_ = ?, PROC_DEF_KEY_ = ?, BUSINESS_KEY_ = ?, END_ACT_ID_ = ?, DELETE_REASON_ = ?, SUPER_PROCESS_INSTANCE_ID_ = ?, STATE_ = ? , END_TIME_ = ? , DURATION_ = ? where ID_ = ?	Update counts: [1]
     * Result 5:	update ACT_HI_TASKINST set EXECUTION_ID_ = ?, PROC_DEF_KEY_
     */
    @Test
    public void deleteProcessInstance() {
       runtimeService.deleteProcessInstance("601","分享牛测试删除");

    }

    @Test
    public void deleteProcessInstance2() {
        List<String> list=new ArrayList<String>();
        list.add("401");
        list.add("501");
        runtimeService.deleteProcessInstances(list,"分享牛批量删除",true,true);

    }

    /**
     *  select * from ACT_RU_EXECUTION where PROC_INST_ID_ = ?
     */
    @Test
    public void getActiveActivityIds() {
        String executionId="2001";
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        System.out.println("######"+activeActivityIds);
    }


    @Test
    public void getActivityInstance() {
        String processInstanceId="2001";
        ActivityInstance activeActivityIds = runtimeService.getActivityInstance(processInstanceId);
        System.out.println(activeActivityIds);
    }
    @Test
    public void suspendProcessDefinitionById() {
        repositoryService.suspendProcessDefinitionById("leave:5:1903");
    }


    @Test
    public void setAuthenticatedUserId2() {
        runtimeService.startProcessInstanceById("leave:5:1903");
    }

    @Test
    public void updateProcessDefinitionSuspensionState() {
        repositoryService
            .updateProcessDefinitionSuspensionState()
            .byProcessDefinitionId("leave:5:1903")
            .suspend();
    }
    /**
     * update ACT_RU_EXT_TASK SET SUSPENSION_STATE_ = ? WHERE PROC_DEF_ID_ = ?	Update counts: [0]
     * Result 1:	update ACT_RU_JOB set REV_ = REV_ + 1, SUSPENSION_STATE_ = ? WHERE PROCESS_DEF_ID_ = ?	Update counts: [0]
     * Result 2:	update ACT_RU_JOB set REV_ = REV_ + 1, SUSPENSION_STATE_ = ? WHERE PROCESS_DEF_ID_ = ? and HANDLER_TYPE_ = ?	Update counts: [0]
     * Result 3:	update ACT_RU_TASK set REV_ = REV_ + 1, SUSPENSION_STATE_ = ?, CREATE_TIME_ = CREATE_TIME_ WHERE PROC_DEF_ID_ = ?	Update counts: [2]
     * Result 4:	update ACT_RU_EXECUTION set REV_ = REV_ + 1, SUSPENSION_STATE_ = ? WHERE PROC_DEF_ID_ = ?	Update counts: [2]
     * Result 5:	update ACT_RE_PROCDEF set REV_ = REV_ + 1, SUSPENSION_STATE_ = ? WHERE ID_ = ?	Update counts: [1]
     * Result 6:	update ACT_HI_PROCINST set PROC_DEF_ID_ = ?, PROC_DEF_KEY_ = ?, BUSINESS_KEY_ = ?, END_ACT_ID_ = ?, DELETE_REASON_ = ?, SUPER_PROCESS_INSTANCE_ID_ = ?, STATE_ = ? where ID_ = ?	Update counts: [1, 1]
     * Result 7:	update ACT_RU_JOBDEF set REV_ = REV_ + 1, SUSPENSION_STATE_ =
     */
    @Test
    public void updateProcessDefinitionSuspensionState2() {
        repositoryService
            .updateProcessDefinitionSuspensionState()
            .byProcessDefinitionId("leave:5:1903")
            .includeProcessInstances(true)
            .activate();
    }
    /**
     * update ACT_RU_EXT_TASK SET SUSPENSION_STATE_ = ? WHERE PROC_INST_ID_ = ?	Update counts: [0]
     * Result 1:	update ACT_RU_JOB set REV_ = REV_ + 1, SUSPENSION_STATE_ = ? WHERE PROCESS_INSTANCE_ID_ = ?	Update counts: [0]
     * Result 2:	update ACT_RU_TASK set REV_ = REV_ + 1, SUSPENSION_STATE_ = ?, CREATE_TIME_ = CREATE_TIME_ WHERE PROC_INST_ID_ = ?	Update counts: [1]
     * Result 3:	update ACT_RU_EXECUTION set REV_ = REV_ + 1, SUSPENSION_STATE_ = ? WHERE PROC_INST_ID_ = ?	Update counts: [1]
     * Result 4:	update ACT_HI_PROCINST set PROC_DEF_ID_ = ?, PROC_DEF_KEY_ = ?,
     */
    @Test
    public void suspendProcessInstanceById() {
      runtimeService.suspendProcessInstanceById("2101");
    }



    @Test
    public void updateProcessInstanceSuspensionStatebyProcessInstanceIds() {
        ProcessInstanceQuery processInstanceQuery = runtimeService.
            createProcessInstanceQuery();
        runtimeService.updateProcessInstanceSuspensionState()
           // .byProcessInstanceIds("1801","1601")
            .byProcessInstanceQuery(processInstanceQuery)
            .activate();
    }
}


