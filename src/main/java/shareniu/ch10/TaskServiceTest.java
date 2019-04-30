package shareniu.ch10;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricIdentityLinkLog;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskServiceTest {

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
            .createProcessEngineConfigurationFromResource("com/shareniu/ch10/camunda.cfg.xml");
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
          //  .addClasspathResource("com/shareniu/ch10/leave.bpmn")
          //  .addClasspathResource("com/shareniu/ch10/leave_var.bpmn")
           // .addClasspathResource("com/shareniu/ch10/leave_class.bpmn")
       //     .addClasspathResource("com/shareniu/ch10/leave_users.bpmn")
          //  .addClasspathResource("com/shareniu/ch10/leave_candidateUsers.bpmn")
          //  .addClasspathResource("com/shareniu/ch10/leave_class_users.bpmn")
            .addClasspathResource("com/shareniu/ch10/leave_groups.bpmn")
            // .nameFromDeployment("101")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }
    @Test
    public void startProcessInstanceByKey() {
        Map<String,Object> var=new HashMap<String, Object>();
      //  var.put("userId","张翠山");
        var.put("userIds","分享牛1,分享牛2,分享牛3");
       runtimeService.startProcessInstanceByKey("leave",var);
    }

    /**
     * select distinct RES.REV_,
     * RES.ID_, RES.NAME_,
     * RES.PARENT_TASK_ID_, RES.DESCRIPTION_, RES.PRIORITY_,
     * RES.CREATE_TIME_, RES.OWNER_, RES.ASSIGNEE_,
     * RES.DELEGATION_, RES.EXECUTION_ID_,
     * RES.PROC_INST_ID_, RES.PROC_DEF_ID_, RES.CASE_EXECUTION_ID_,
     * RES.CASE_INST_ID_, RES.CASE_DEF_ID_, RES.TASK_DEF_KEY_,
     * RES.DUE_DATE_, RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_,
     * RES.TENANT_ID_
     *
     * from ACT_RU_TASK RES WHERE ( 1 = 1 and RES.ASSIGNEE_ = ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     * ameters: 张三丰(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void findMyTask() {
        String assignee="分享牛1";
        List<Task> list = taskService
            .createTaskQuery()
            .taskAssignee(assignee)
            .list();
        for (Task task:list){
            System.out.println("#################");
            System.out.println(task.getId());
            System.out.println(task.getCreateTime());
            System.out.println(task.getName());
            System.out.println(task.getPriority());
            System.out.println(task.getExecutionId());
            System.out.println(task.getDescription());
            System.out.println("#################");

        }
    }


    @Test
    public void completeMyTask() {
        Map<String,Object> var=new HashMap<String, Object>();
        var.put("userId","分享牛");
        String taskId="1106";
        taskService.complete(taskId,var);
    }
    @Test
    public void setAssignee() {
        String taskId="1203";
        String assignee="张翠山";

        taskService.setAssignee(taskId,assignee);
    }

    /**
     * ng: select distinct RES.REV_, RES.ID_, RES.NAME_, RES.PARENT_TASK_ID_,
     * RES.DESCRIPTION_, RES.PRIORITY_, RES.CREATE_TIME_,
     * RES.OWNER_, RES.ASSIGNEE_, RES.DELEGATION_,
     * \RES.EXECUTION_ID_, RES.PROC_INST_ID_,
     * RES.PROC_DEF_ID_, RES.CASE_EXECUTION_ID_,
     * RES.CASE_INST_ID_, RES.CASE_DEF_ID_,
     * RES.TASK_DEF_KEY_, RES.DUE_DATE_,
     * RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_,
     * RES.TENANT_ID_
     * from
     * ACT_RU_TASK
     * RES inner join ACT_RU_IDENTITYLINK I
     *
     * on I.TASK_ID_ = RES.ID_
     * WHERE ( 1 = 1 and ( RES.ASSIGNEE_ is null
     * and I.TYPE_ = 'candidate' and ( I.USER_ID_ = ? ) ) ) order by RES.ID_ asc LIMIT ? OFFSET ?
     * 21:51:40.467 [main] DEBUG o.c.b.e.i.p.e.T.selectTaskByQueryCriteria - ==> Parameters: 张三(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void findGroupTask() {
        String assignee="分享牛1";
        List<Task> list = taskService
            .createTaskQuery()
            .taskCandidateUser(assignee)
            .list();
        for (Task task:list){
            System.out.println("#################");
            System.out.println(task.getId());
            System.out.println(task.getCreateTime());
            System.out.println(task.getName());
            System.out.println(task.getPriority());
            System.out.println(task.getExecutionId());
            System.out.println(task.getDescription());
            System.out.println("#################");

        }
    }
    /**
     * select * from ACT_RU_IDENTITYLINK where TASK_ID_ = ?
     */
    @Test
    public void getIdentityLinksForTask() {
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask("1506");
        for (IdentityLink identityLink:identityLinksForTask){
            System.out.println("###########");
            System.out.println(identityLink.getTaskId());
            System.out.println(identityLink.getTenantId());
            System.out.println(identityLink.getUserId());
            System.out.println("###########");
        }
    }

    /**
     * select distinct RES.*
     * from ACT_HI_IDENTITYLINK RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createHistoricIdentityLinkLogQuery() {
        List<HistoricIdentityLinkLog> list = historyService
            .createHistoricIdentityLinkLogQuery()
            .list();
        for (HistoricIdentityLinkLog hil:list){
            System.out.println("################");
            System.out.println(hil.getOperationType());
            System.out.println(hil.getUserId());
            System.out.println(hil.getGroupId());
            System.out.println(hil.getId());
            System.out.println("################");
        }
    }
    /**
     *  Preparing: insert into ACT_HI_IDENTITYLINK ( ID_, TIMESTAMP_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, ROOT_PROC_INST_ID_, PROC_DEF_ID_, OPERATION_TYPE_, ASSIGNER_ID_, PROC_DEF_KEY_, TENANT_ID_, REMOVAL_TIME_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * 22:03:35.870 [main] DEBUG o.c.b.e.i.p.e.H.insertHistoricIdentityLinkLogEvent - ==> Parameters: 1601(String), 2019-02-16 22:03:35.842(Timestamp), assignee(String), 张三(String), null, 1506(String), 1501(String), leave:4:1403(String), add(String), null, leave(String), a(String), null
     * 22:03:35.870 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'UPDATE'; Entity: 'TaskEntity[id=1506]'
     * 22:03:35.871 [main] DEBUG o.c.b.e.i.p.e.TaskEntity.updateTask - ==>  Preparing: update ACT_RU_TASK SET REV_ = ?, NAME_ = ?, PARENT_TASK_ID_ = ?, PRIORITY_ = ?, CREATE_TIME_ = ?, OWNER_ = ?, ASSIGNEE_ = ?, DELEGATION_ = ?, EXECUTION_ID_ = ?, PROC_DEF_ID_ = ?, CASE_EXECUTION_ID_ = ?, CASE_INST_ID_ = ?, CASE_DEF_ID_ = ?, TASK_DEF_KEY_ = ?, DESCRIPTION_ = ?, DUE_DATE_ = ?, FOLLOW_UP_DATE_ = ?, SUSPENSION_STATE_ = ?, TENANT_ID_ = ? where ID_= ? and REV_ = ?
     * 22:03:35.872 [main] DEBUG o.c.b.e.i.p.e.TaskEntity.updateTask - ==> Parameters: 2(Integer), 请假申请(String), null, 50(Integer), 2019-02-16 21:49:22.0(Timestamp), null, 张三(String), null, 1501(String), leave:4:1403(String), null, null, null, Task_08uny2s(String), null, null, null, 1(Integer), a(String), 1506(String), 1(Integer)
     * 22:03:35.875 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'UPDATE'; Entity: 'HistoricActivityInstanceEventEntity[id=Task_08uny2s:1505]'
     * 22:03:35.901 [main] DEBUG o.c.b.e.i.p.e.H.updateHistoricActivityInstanceEvent - ==>  Preparing: UPDATE ACT_HI_ACTINST SET EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, ACT_ID_ = ?, ACT_NAME_ = ?, ACT_TYPE_ = ?, PARENT_ACT_INST_ID_ = ? , ASSIGNEE_ = ? , TASK_ID_ = ? WHERE ID_ = ?
     * 22:03:35.901 [main] DEBUG o.c.b.e.i.p.e.H.updateHistoricActivityInstanceEvent - ==> Parameters: 1501(String), leave(String), leave:4:1403(String), Task_08uny2s(String), 请假申请(String), userTask(String), 1501(String), 张三(String), 1506(String), Task_08uny2s:1505(String)
     * 22:03:35.901 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'UPDATE'; Entity: 'HistoricTaskInstanceEventEntity[id=1506]'
     * 22:03:35.902 [main] DEBUG o.c.b.e.i.p.e.H.updateHistoricTaskInstanceEvent - ==>  Preparing: update ACT_HI_TASKINST set EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, NAME_ = ?, PARENT_TASK_ID_ = ?, DESCRIPTION_ = ?, OWNER_ = ?, ASSIGNEE_ = ?, DELETE_REASON_ = ?, TASK_DEF_KEY_ = ?, PRIORITY_ = ?, DUE_DATE_ = ?, FOLLOW_UP_DATE_ = ?, CASE_INST_ID_ = ? where ID_ = ?
     * 22:03:35.903 [main] DEBUG o.c.b.e.i.p.e.H.updateHistoricTaskInstanceEvent - ==> Parameters: 1501(String), leave(String), leave:4:1403(String), 请假申请(String), null, null, null, 张三(String
     */
    /**
     *
     *
     * insert into ACT_HI_IDENTITYLINK ( ID_, TIMESTAMP_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, ROOT_PROC_INST_ID_, PROC_DEF_ID_, OPERATION_TYPE_, ASSIGNER_ID_, PROC_DEF_KEY_, TENANT_ID_, REMOVAL_TIME_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )	Update counts: [1]
     * Result 1:	update ACT_RU_TASK SET REV_ = ?, NAME_ = ?, PARENT_TASK_ID_ = ?, PRIORITY_ = ?, CREATE_TIME_ = ?, OWNER_ = ?, ASSIGNEE_ = ?, DELEGATION_ = ?, EXECUTION_ID_ = ?, PROC_DEF_ID_ = ?, CASE_EXECUTION_ID_ = ?, CASE_INST_ID_ = ?, CASE_DEF_ID_ = ?, TASK_DEF_KEY_ = ?, DESCRIPTION_ = ?, DUE_DATE_ = ?, FOLLOW_UP_DATE_ = ?, SUSPENSION_STATE_ = ?, TENANT_ID_ = ? where ID_= ? and REV_ = ?	Update counts: [1]
     * Result 2:	UPDATE ACT_HI_ACTINST SET EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, ACT_ID_ = ?, ACT_NAME_ = ?, ACT_TYPE_ = ?, PARENT_ACT_INST_ID_ = ? , ASSIGNEE_ = ? , TASK_ID_ = ? WHERE ID_ = ?	Update counts: [1]
     * Result 3:	update ACT_HI_TASKINST set EXECUTION_ID_ = ?, PROC_DEF_KEY_ = ?, PROC_DEF_ID_ = ?, NAME_ =
     */
    @Test
    public void claim() {
        String taskId="2006";
        String userId="张三1";
        taskService.claim(taskId,userId);
    }

    /**
     *  ==>  Preparing: insert into ACT_HI_IDENTITYLINK ( ID_, TIMESTAMP_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, ROOT_PROC_INST_ID_, PROC_DEF_ID_, OPERATION_TYPE_, ASSIGNER_ID_, PROC_DEF_KEY_, TENANT_ID_, REMOVAL_TIME_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     * 22:24:04.534 [main] DEBUG o.c.b.e.i.p.e.H.insertHistoricIdentityLinkLogEvent - ==> Parameters: 2402(String), 2019-02-16 22:24:04.523(Timestamp), candidate(String), 分享牛6(String), null, 2306(String), 2301(String), leave:6:2203(String), add(String), null, leave(String), a(String), null
     * 22:24:04.534 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'INSERT'; Entity: 'IdentityLinkEntity[id=2401]'
     * 22:24:04.534 [main] DEBUG o.c.b.e.i.p.e.I.insertIdentityLink - ==>  Preparing: insert into ACT_RU_IDENTITYLINK (ID_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, PROC_DEF_ID_, TENANT_ID_, REV_ ) values (?, ?, ?, ?, ?, ?, ?, 1 )
     * 22:24:04.535 [main] DEBUG o.c.b.e.i.p.e.I.insertIdentityLink - ==> Parameters: 2401(String), candidate(String), 分享牛6(String), null, 2306(String), null, a(String)
     */
    @Test
    public void addCandidateUser() {
        String taskId="2306";
        String userId="分享牛6";
        taskService.addCandidateUser(taskId,userId);
    }
    /**
     * =>   insert into ACT_HI_IDENTITYLINK ( ID_, TIMESTAMP_, TYPE_, USER_ID_, GROUP_ID_, TASK_ID_, ROOT_PROC_INST_ID_, PROC_DEF_ID_, OPERATION_TYPE_, ASSIGNER_ID_, PROC_DEF_KEY_, TENANT_ID_, REMOVAL_TIME_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
     *  2501(String), 2019-02-16 22:25:28.266(Timestamp), candidate(String), 分享牛6(String), null, 2306(String), 2301(String), leave:6:2203(String), delete(String), null, leave(String), a(String), null
     *  delete from ACT_RU_IDENTITYLINK where ID_ = ?
     *   2401(String)
     */
    @Test
    public void deleteCandidateUser() {
        String taskId="2306";
        String userId="分享牛6";
        taskService.deleteCandidateUser(taskId,userId);
    }
    @Test
    public void initUsers() {
        GroupEntity groupEntity1 = new GroupEntity();
        groupEntity1.setRevision(0);
        groupEntity1.setName("普通员工");
        groupEntity1.setId("pt");

        identityService.saveGroup(groupEntity1);//建立组
        GroupEntity groupEntity2 = new GroupEntity();
        groupEntity2.setRevision(0);
        groupEntity2.setName("GeneralManager");
        groupEntity2.setId("GeneralManager");
        identityService.saveGroup(groupEntity2);//建立组

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setRevision(0);
        userEntity1.setId("zs");
        identityService.saveUser(userEntity1);
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setRevision(0);
        userEntity2.setId("ls");
        identityService.saveUser(userEntity2);
        UserEntity userEntity3 = new UserEntity();
        userEntity3.setRevision(0);
        userEntity3.setId("ww");
        identityService.saveUser(userEntity3);
        identityService.createMembership("zs", "GeneralManager");//建立组和用户关系
        identityService.createMembership("ls", "GeneralManager");//建立组和用户关系
        identityService.createMembership("ww", "pt");//建立组和用户关系
    }

    /**
     * : select distinct RES.REV_, RES.ID_, RES.NAME_, RES.PARENT_TASK_ID_, RES.DESCRIPTION_, RES.PRIORITY_, RES.CREATE_TIME_, RES.OWNER_, RES.ASSIGNEE_, RES.DELEGATION_, RES.EXECUTION_ID_, RES.PROC_INST_ID_, RES.PROC_DEF_ID_, RES.CASE_EXECUTION_ID_, RES.CASE_INST_ID_, RES.CASE_DEF_ID_, RES.TASK_DEF_KEY_, RES.DUE_DATE_, RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_, RES.TENANT_ID_ from ACT_RU_TASK RES inner join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES.ID_ WHERE ( 1 = 1 and ( RES.ASSIGNEE_ is null and I.TYPE_ = 'candidate' and ( I.GROUP_ID_ IN ( ? ) ) ) ) order by RES.ID_ asc LIMIT ? OFFSET ?
     * 22:35:46.058 [main] DEBUG o.c.b.e.i.p.e.T.selectTaskByQueryCriteria - ==> Parameters: pt(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void findGroupTask2() {
        String candidateGroup="pt";
        List<Task> list = taskService
            .createTaskQuery()
            .taskCandidateGroup(candidateGroup)
            .list();
        for (Task task:list){
            System.out.println("#################");
            System.out.println(task.getId());
            System.out.println(task.getCreateTime());
            System.out.println(task.getName());
            System.out.println(task.getPriority());
            System.out.println(task.getExecutionId());
            System.out.println(task.getDescription());
            System.out.println("#################");

        }
    }

    /**
     * select distinct RES.REV_, RES.ID_, RES.NAME_, RES.PARENT_TASK_ID_, RES.DESCRIPTION_,
     * RES.PRIORITY_, RES.CREATE_TIME_, RES.OWNER_, RES.ASSIGNEE_,
     * RES.DELEGATION_, RES.EXECUTION_ID_,
     * RES.PROC_INST_ID_, RES.PROC_DEF_ID_,
     * RES.CASE_EXECUTION_ID_, RES.CASE_INST_ID_,
     * RES.CASE_DEF_ID_, RES.TASK_DEF_KEY_, RES.DUE_DATE_,
     * RES.FOLLOW_UP_DATE_, RES.SUSPENSION_STATE_,
     * RES.TENANT_ID_
     *
     * from ACT_RU_TASK RES inner join ACT_RU_IDENTITYLINK I
     * on I.TASK_ID_ = RES.ID_
     * WHERE ( 1 = 1 and ( RES.ASSIGNEE_ is null
     * and I.TYPE_ = 'candidate'
     * and ( I.USER_ID_ = ? or I.GROUP_ID_ IN ( ? ) ) ) )
     * order by RES.ID_ asc LIMIT ? OFFSET ?
     * ww(String), pt(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public void findGroupTask3() {
        String candidateUser="zs";
        List<Task> list = taskService
            .createTaskQuery()
            .taskCandidateUser(candidateUser)
            .list();
        for (Task task:list){
            System.out.println("#################");
            System.out.println(task.getId());
            System.out.println(task.getCreateTime());
            System.out.println(task.getName());
            System.out.println(task.getPriority());
            System.out.println(task.getExecutionId());
            System.out.println(task.getDescription());
            System.out.println("#################");

        }
    }
}


