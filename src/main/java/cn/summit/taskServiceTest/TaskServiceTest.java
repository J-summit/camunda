package cn.summit.taskServiceTest;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlyo
 * @since 2019/4/22 20:54
 */
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
                .createProcessEngineConfigurationFromResource("camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        identityService = processEngine.getIdentityService();
        authorizationService = processEngine.getAuthorizationService();
        managementService = processEngine.getManagementService();
        repositoryService = processEngine.getRepositoryService();
        runtimeService=processEngine.getRuntimeService();
        taskService=processEngine.getTaskService();
        historyService=processEngine.getHistoryService();
    }

    /**
     * ACT_RE_PROCDEF
     * ACT_RE_DEPLOYMENT 流程定时 bpm 中name为名字 key唯一标识  如果key相同version自动加一
     */
    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
                .name("请假流程-变量")
                .source("本地测试")
                .tenantId("a")
                .addClasspathResource("taskService/task_var.bpmn")
                .addClasspathResource("taskService/task_var.png")
                // .nameFromDeployment("101")
                .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void startProcessInstanceByKey() {
        Map<String,Object> var=new HashMap<String, Object>();
        var.put("userId","summit");
        runtimeService.startProcessInstanceByKey("process1",var);
    }

    @Test
    public void findMyTask() {
        String assignee="sword";
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
        var.put("userId","sword");
        String taskId="2503";
        taskService.complete(taskId,var);
    }
}
