package shareniu.ch19;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.Job;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AsyncTest {

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
    FormService formService;
    @Before
    public void init() {
        processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("com/shareniu/ch19/camunda.cfg.xml");
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
        formService=processEngine.getFormService();
    }
    /**
     *
     * insert into ACT_RU_JOBDEF ( ID_, PROC_DEF_ID_, PROC_DEF_KEY_, ACT_ID_, JOB_TYPE_, JOB_CONFIGURATION_, JOB_PRIORITY_, SUSPENSION_STATE_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1, 1, 1, 1]
     * Result 1:	insert into ACT_RE_DEPLOYMENT(ID_, NAME_, DEPLOY_TIME_, SOURCE_, TENANT_ID_) values(?, ?, ?, ?, ?)	Update counts: [1]
     * Result 2:	insert into ACT_GE_BYTEARRAY( ID_, NAME_, BYTES_, DEPLOYMENT_ID_, GENERATED_, TENANT_ID_, TYPE_, CREATE_TIME_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, ?, 1)	Update counts: [1]
     * Result 3:	insert into ACT_RE_PROCDEF(ID_, CATEGORY_,
     */
    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("外置表单流程")
            .source("本地测试")
            .tenantId("a")
         //   .addClasspathResource("com/shareniu/ch19/async1.bpmn")
            .addClasspathResource("com/shareniu/ch19/async-start2.bpmn")
//            .addClasspathResource("com/shareniu/ch18/start.html")
//            .addClasspathResource("com/shareniu/ch18/1.html")
//            .addClasspathResource("com/shareniu/ch18/2.html")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }







    @Test
    public void startProcessInstanceByKey() {

      runtimeService.startProcessInstanceByKey("asyc");

    }
    /**
     * insert into ACT_HI_JOB_LOG ( ID_, TIMESTAMP_, JOB_ID_, JOB_DUEDATE_, JOB_RETRIES_, JOB_PRIORITY_, JOB_EXCEPTION_MSG_, JOB_EXCEPTION_STACK_ID_, JOB_STATE_, JOB_DEF_ID_, JOB_DEF_TYPE_, JOB_DEF_CONFIGURATION_, ACT_ID_, EXECUTION_ID_, ROOT_PROC_INST_ID_, PROCESS_INSTANCE_ID_, PROCESS_DEF_ID_, PROCESS_DEF_KEY_, DEPLOYMENT_ID_, TENANT_ID_, SEQUENCE_COUNTER_, REMOVAL_TIME_ ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 1:	insert into ACT_HI_TASKINST ( ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, CASE_DEF_KEY_, CASE_DEF_ID_, CASE_INST_ID_, CASE_EXECUTION_ID_, ACT_INST_ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, OWNER_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, DELETE_REASON_, TASK_DEF_KEY_, PRIORITY_, DUE_DATE_, FOLLOW_UP_DATE_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 2:	insert into ACT_HI_ACTINST ( ID_, PARENT_ACT_INST_ID_, PROC_DEF_KEY_, PROC_DEF_ID_, ROOT_PROC_INST_ID_, PROC_INST_ID_, EXECUTION_ID_, ACT_ID_, TASK_ID_, CALL_PROC_INST_ID_, CALL_CASE_INST_ID_, ACT_NAME_, ACT_TYPE_, ASSIGNEE_, START_TIME_, END_TIME_, DURATION_, ACT_INST_STATE_, SEQUENCE_COUNTER_, TENANT_ID_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 3:	insert into ACT_RU_TASK ( ID_, NAME_, PARENT_TASK_ID_, DESCRIPTION_, PRIORITY_, CREATE_TIME_, OWNER_, ASSIGNEE_, DELEGATION_, EXECUTION_ID_, PROC_INST_ID_, PROC_DEF_ID_, CASE_EXECUTION_ID_, CASE_INST_ID_, CASE_DEF_ID_, TASK_DEF_KEY_, DUE_DATE_, FOLLOW_UP_DATE_, SUSPENSION_STATE_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     * Result 4:	delete from ACT_RU_JOB where ID_ = ? and REV_ = ?	Update counts: [1]
     * Result 5:	update ACT_RU_EXECUTION set REV_ = ?, P
     */
    @Test
    public void executeJob() {
        String jobId="701";
        managementService.executeJob(jobId);

    }
    /**
     * insert into ACT_HI_JOB_LOG ( ID_, TIMESTAMP_, JOB_ID_, JOB_DUEDATE_, JOB_RETRIES_, JOB_PRIORITY_, JOB_EXCEPTION_MSG_, JOB_EXCEPTION_STACK_ID_, JOB_STATE_, JOB_DEF_ID_, JOB_DEF_TYPE_, JOB_DEF_CONFIGURATION_, ACT_ID_, EXECUTION_ID_, ROOT_PROC_INST_ID_, PROCESS_INSTANCE_ID_, PROCESS_DEF_ID_, PROCESS_DEF_KEY_, DEPLOYMENT_ID_, TENANT_ID_, SEQUENCE_COUNTER_, REMOVAL_TIME_ ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 1:	insert into ACT_RU_JOB ( ID_, TYPE_, LOCK_OWNER_, LOCK_EXP_TIME_, EXCLUSIVE_, EXECUTION_ID_, PROCESS_INSTANCE_ID_, PROCESS_DEF_ID_, PROCESS_DEF_KEY_, RETRIES_, EXCEPTION_STACK_ID_, EXCEPTION_MSG_, DUEDATE_, HANDLER_TYPE_, HANDLER_CFG_, DEPLOYMENT_ID_, SUSPENSION_STATE_, JOB_DEF_ID_, PRIORITY_, SEQUENCE_COUNTER_, TENANT_ID_, CREATE_TIME_, REV_ ) values (?, 'message', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     * Result 2:	delete from ACT_RU_TASK where ID_ = ? and REV_ = ?	Update counts: [1]
     * Result 3:	update ACT_RU_EXECUTION set REV_ = ?, PROC_DEF_ID_ = ?, BUSINESS_KEY_ = ?, ACT_ID_ = ?, ACT_INST_ID_ = ?, IS_ACTIVE_ = ?, IS_CONCURRENT_ = ?, IS_SCOPE_ = ?, IS_EVENT_SCOPE_ = ?, PARENT_ID_ = ?, SUPER_EXEC_ = ?, SUSPENSION_STATE_ = ?, CACHED_ENT_STATE_ = ?, SEQUENCE_COUNTER_ = ?, TENANT_ID_ = ? where ID_ = ? and REV_ = ?	Update counts: [1]
     * Result 4:	UPDATE ACT_HI_ACTINST SET EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, ACT_ID_ = ?, ACT_NAME_ = ?, ACT_TYPE_ = ?, PARENT_ACT_INST_ID_ = ? , END_TIME_ = ? , DURATION_ = ? , ACT_INST_STATE_ = ? WHERE ID_ = ?	Update counts: [1]
     * Result 5:	update ACT_HI_TASKINST set EXECUTION_ID_ = ?
     */
    @Test
    public void complete() {
        String taskId="502";
        taskService.complete(taskId);

    }
    /**
     * select distinct RES.* from ACT_RU_JOB RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createJobQuery() {

        List<Job> list = managementService.createJobQuery()
          //  .active()
           // .processInstanceId()
            .list();
        System.out.println(list);

    }


}
