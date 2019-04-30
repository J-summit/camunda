package shareniu.ch12;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.impl.ServiceImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FilterTest {

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
            .createProcessEngineConfigurationFromResource("com/shareniu/ch12/camunda.cfg.xml");
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
     * Result 0:	insert into ACT_RU_FILTER (ID_, RESOURCE_TYPE_, NAME_, OWNER_, QUERY_, PROPERTIES_, REV_) values ( ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     */
    @Test
    public void saveFilter() {
        TaskQuery taskQuery = taskService.createTaskQuery()
            .taskAssignee("分享牛");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("shareniu-1","分享牛1");
        map.put("shareniu-2","分享牛2");
        map.put("shareniu-3","分享牛3");
        Filter filter = filterService
            .newTaskFilter()
            .setName("分享牛个人偏好")
            .setOwner("分享牛")
            .setQuery(taskQuery)
            .setProperties(map);
        Filter filter1 = filterService.saveFilter(filter);
        System.out.println(filter1.getId()+","+filter1.getQuery()+","+filter1.getProperties()+","
        +filter1.getResourceType()+","+filter1.getOwner()+","+filter1.getName());
    }


    @Test
    public void saveFilter2() {
        TaskQuery taskQuery = taskService.createTaskQuery()
            .taskAssignee("分享牛");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("shareniu-1","分享牛1");
        map.put("shareniu-2","分享牛2");
        map.put("shareniu-3","分享牛3");
        Filter filter = filterService
            .newTaskFilter()
            .setName("分享牛个人偏好")
            .setOwner("分享牛")
            .setQuery(null)
            .setProperties(map);
        Filter filter1 = filterService.saveFilter(filter);
        System.out.println(filter1.getId()+","+filter1.getQuery()+","+filter1.getProperties()+","
            +filter1.getResourceType()+","+filter1.getOwner()+","+filter1.getName());
    }

    /**
     * select * from ACT_RU_FILTER where ID_ = ?
     */
    @Test
    public void getFilter() {
        String filterId="1";
        Filter filter1 = filterService.getFilter(filterId);
        System.out.println("###########");
        System.out.println(filter1.getId()+","+filter1.getQuery()+","+filter1.getProperties()+","
            +filter1.getResourceType()+","+filter1.getOwner()+","+filter1.getName());
    }
    /**
     * Result 0:	insert into ACT_RU_FILTER (ID_, RESOURCE_TYPE_, NAME_, OWNER_, QUERY_, PROPERTIES_, REV_) values ( ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     */
    @Test
    public void extendFilter() {
        String filterId="1";
        Filter filter1 = filterService.getFilter(filterId);
        TaskQuery taskQuery = taskService.createTaskQuery()
            .taskName("分享牛")
            .taskCandidateUser("分享牛");
        Filter extend = filter1.extend(taskQuery);
        filterService.saveFilter(extend);
        //{"assignee":"分享牛"}
        //{"candidateUser":"分享牛","name":"分享牛","assignee":"分享牛"}
    }
    /**
     * Result 0:	delete from ACT_RU_FILTER where ID_ = ? and REV_ = ?	Update counts: [1]
     */
    @Test
    public void deleteFilter() {
        String filterId="1";
        filterService.deleteFilter(filterId);
    }
    /**
     * 11:15:35.274 [main] DEBUG o.c.b.e.i.p.e.T.selectTaskByQueryCriteria - ==>  Preparing: select distinct RES.REV_, RES.ID_, RES.NAME_, RES.PARENT_TASK_ID_, RES.DESCRIPTION_, RES.PRIORITY_, RES.CREATE_TIME_, RES.OWNER_, RES.ASSIGNEE_, RES.DELEGATION_, RES.EXECUTION_ID_, RES.PROC_INST_ID_, RES.PROC_DEF_ID_, RES.CASE_EXECUTION_ID_, RES.CASE_INST_ID_, RES.CASE_DEF_ID_, RES.TASK_DEF_KEY_, RES.DUE_DATE_, RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_, RES.TENANT_ID_
     * from ACT_RU_TASK RES WHERE ( 1 = 1 and UPPER(RES.NAME_) = UPPER(?) and RES.ASSIGNEE_ = ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void getAndUseQuery() {
        String filterId="101";
        Filter filter = filterService.getFilter(filterId);
        System.out.println("###########");
        ServiceImpl service= (ServiceImpl) filterService;
        service.getCommandExecutor().execute(new CustomerCmd(filter));
    }
    @Test
    public void saveFilter3() {
        ProcessInstanceQuery taskQuery =runtimeService.createProcessInstanceQuery();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("shareniu-1","分享牛1");
        map.put("shareniu-2","分享牛2");
        map.put("shareniu-3","分享牛3");
        Filter filter = filterService
            .newTaskFilter()
            .setName("分享牛个人偏好")
            .setOwner("分享牛")
            .setQuery(taskQuery)
            .setProperties(map);
        Filter filter1 = filterService.saveFilter(filter);
        System.out.println(filter1.getId()+","+filter1.getQuery()+","+filter1.getProperties()+","
            +filter1.getResourceType()+","+filter1.getOwner()+","+filter1.getName());
    }


}
