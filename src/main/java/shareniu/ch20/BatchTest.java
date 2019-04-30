package shareniu.ch20;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.batch.Batch;
import org.camunda.bpm.engine.history.UserOperationLogEntry;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.migration.MigrationPlan;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.UserTaskImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class BatchTest {

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
            .createProcessEngineConfigurationFromResource("com/shareniu/ch20/camunda.cfg.xml");
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
           // .addClasspathResource("com/shareniu/ch20/async.bpmn")
            .addClasspathResource("com/shareniu/ch20/async1.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }







    @Test
    public void startProcessInstanceByKey() {
        for (int i = 0; i < 10; i++) {
            runtimeService.startProcessInstanceByKey("asyc");
        }

    }

    /**
     * 14:26:52.043 [main] DEBUG o.c.b.e.i.p.e.E.selectProcessInstanceByQueryCriteria - ==>  Preparing: select distinct RES.* from ACT_RU_EXECUTION RES inner join ACT_RE_PROCDEF P on RES.PROC_DEF_ID_ = P.ID_ WHERE RES.PARENT_ID_ is null and ( RES.PROC_INST_ID_ IN ( ? , ? ) ) order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createProcessInstanceQuery() {
        Set<String> processInstanceIds=new HashSet<String>();
        processInstanceIds.add("133");
        processInstanceIds.add("137");
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
            .processInstanceIds(processInstanceIds)
            .list();

    }

    @Test
    public void createProcessInstanceQuerytenantIdIn() {
        Set<String> processInstanceIds=new HashSet<String>();
        processInstanceIds.add("133");
        processInstanceIds.add("137");
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
            .tenantIdIn("a","b")
            .list();

    }
    @Test
    public void complete() {
        taskService.complete("1303");

    }


    @Test
    public void newMigration() {
        String sourceProcessDefinitionId="asyc:1:3";
        String targetProcessDefinitionId="asyc:2:303";
        String sourceActivityId="Task_1usod4v";
        String targetActivityId="Task_1w31kt8";

        MigrationPlan migrationPlan = runtimeService.createMigrationPlan(sourceProcessDefinitionId, targetProcessDefinitionId)
            .mapActivities(sourceActivityId, targetActivityId)
            .build();
        runtimeService.newMigration(migrationPlan)
            .processInstanceIds("129")
            .execute();
        identityService.setAuthenticatedUserId("分享牛1");

    }
    /**
     * 	insert into ACT_RU_JOBDEF ( ID_, PROC_DEF_ID_, PROC_DEF_KEY_, ACT_ID_, JOB_TYPE_, JOB_CONFIGURATION_, JOB_PRIORITY_, SUSPENSION_STATE_, TENANT_ID_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1, 1, 1]
     * Result 1:	insert into ACT_HI_JOB_LOG ( ID_, TIMESTAMP_, JOB_ID_, JOB_DUEDATE_, JOB_RETRIES_, JOB_PRIORITY_, JOB_EXCEPTION_MSG_, JOB_EXCEPTION_STACK_ID_, JOB_STATE_, JOB_DEF_ID_, JOB_DEF_TYPE_, JOB_DEF_CONFIGURATION_, ACT_ID_, EXECUTION_ID_, ROOT_PROC_INST_ID_, PROCESS_INSTANCE_ID_, PROCESS_DEF_ID_, PROCESS_DEF_KEY_, DEPLOYMENT_ID_, TENANT_ID_, SEQUENCE_COUNTER_, REMOVAL_TIME_ ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 2:	insert into ACT_HI_BATCH ( ID_, TYPE_, TOTAL_JOBS_, JOBS_PER_SEED_, INVOCATIONS_PER_JOB_, SEED_JOB_DEF_ID_, MONITOR_JOB_DEF_ID_, BATCH_JOB_DEF_ID_, TENANT_ID_, CREATE_USER_ID_, START_TIME_, END_TIME_, REMOVAL_TIME_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 3:	insert into ACT_RU_BATCH ( ID_, TYPE_, TOTAL_JOBS_, JOBS_CREATED_, JOBS_PER_SEED_, INVOCATIONS_PER_JOB_, SEED_JOB_DEF_ID_, MONITOR_JOB_DEF_ID_, BATCH_JOB_DEF_ID_, CONFIGURATION_, TENANT_ID_, CREATE_USER_ID_, SUSPENSION_STATE_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     * Result 4:	insert into ACT_GE_BYTEARRAY(ID_, NAME_, BYTES_, DEPLOYMENT_ID_, TENANT_ID_, TYPE_, CREATE_TIME_, ROOT_PROC_INST_ID_, REMOVAL_TIME_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     * Result 5:	insert into ACT_RU_JOB ( ID_, TYPE_,
     */
    @Test
    public void newMigrationexecuteAsync() {
        String sourceProcessDefinitionId="asyc:1:3";
        String targetProcessDefinitionId="asyc:2:303";
        String sourceActivityId="Task_1usod4v";
        String targetActivityId="Task_1w31kt8";
        MigrationPlan migrationPlan = runtimeService.createMigrationPlan(sourceProcessDefinitionId, targetProcessDefinitionId)
            .mapActivities(sourceActivityId, targetActivityId)
            .build();
        runtimeService.newMigration(migrationPlan)
            .processInstanceIds("133","137","121")
            .executeAsync();

    }

    @Test
    public void createBatchQuery() {
        List<Batch> list = managementService.
            createBatchQuery()
            .list();
        for (Batch batch:list){
            System.out.println(batch.getId());
            System.out.println(batch.getType());
            System.out.println(batch.getTotalJobs());

        }

    }

    @Test
    public void activateBatchById() {
        String batchId="1602";
      managementService.activateBatchById(batchId);

    }
    @Test
    public void executeJob() {
        managementService.executeJob("1806");

    }

    @Test
    public void deleteBatch() {
        String batchId="1302";

        managementService.deleteBatch(batchId,true);

    }

    @Test
    public void bpmnModelInstance() {
        String processDefinitionId="asyc:1:3";
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionId);
        BpmnModelInstance clone = bpmnModelInstance.clone();
        UserTaskImpl task_1w31kt8 = clone.getModelElementById("Task_1w31kt8");
        task_1w31kt8.setName("分享牛22");
        task_1w31kt8.setId("shareniu-333");
        repositoryService.createDeployment()
            .addModelInstance("复制.bpmn",clone)
            .deploy();
    }

    @Test
    public void createUserOperationLogQuery() {
        List<UserOperationLogEntry> list = historyService.createUserOperationLogQuery().list();
        System.out.println(list);
    }

    @Test
    public void createModification() {
        String processDefinitionId="asyc:1:3";
        runtimeService
            .createModification(processDefinitionId)
           // .startAfterActivity("Task_1urcmi1")
           // .startBeforeActivity("Task_1urcmi1")
            .startTransition("SequenceFlow_06pr015")
            .processInstanceIds("121")
            .cancelAllForActivity("Task_1usod4v",true)
            .execute();
    }



    @Test
    public void suspendAsync() {
        runtimeService
            .updateProcessInstanceSuspensionState()
            .byProcessInstanceIds(Arrays.asList("117"))
            .suspendAsync();
    }
    @Test
    public void activateAsync() {
        identityService.setAuthenticatedUserId("分享牛2");
        runtimeService
            .updateProcessInstanceSuspensionState()
            .byProcessInstanceIds(Arrays.asList("117"))
            .activateAsync();
    }

    @Test
    public void restartProcessInstances() {
        runtimeService.restartProcessInstances("asyc:1:3")
            .startTransition("SequenceFlow_1cnjzt0")
            .processInstanceIds("129")
            .execute();
    }

    @Test
    public void restartProcessInstancesexecuteAsync() {
        runtimeService.restartProcessInstances("asyc:1:3")
            .startTransition("SequenceFlow_1cnjzt0")
            .processInstanceIds("129")
            .executeAsync();
    }

    @Test
    public void deleteProcessInstances() {
        List<String> processInstanceIds=new ArrayList<String>();
        processInstanceIds.add("301");
        processInstanceIds.add("302");
       runtimeService.deleteProcessInstances(processInstanceIds,"分享牛删除",true,true);
    }

    @Test
    public void deleteProcessInstancesAsync() {
        List<String> processInstanceIds=new ArrayList<String>();
        processInstanceIds.add("301");
        processInstanceIds.add("302");
        runtimeService.deleteProcessInstancesAsync(processInstanceIds,runtimeService.createProcessInstanceQuery(),"分享牛异步删除",true,true);
    }
}
