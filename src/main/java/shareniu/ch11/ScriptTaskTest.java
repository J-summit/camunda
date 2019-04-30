package shareniu.ch11;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.util.CollectionUtil;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ScriptTaskTest {

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
            .name("服务任务流程")
            .source("本地测试")
            .tenantId("a")
           // .addClasspathResource("com/shareniu/ch11/servicetask.bpmn")
            .addClasspathResource("com/shareniu/ch11/scripttask_js.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
        startProcessInstanceByKey();
    }

    @Test
    public void startProcessInstanceByKey() {
       runtimeService.startProcessInstanceByKey("scripttask_js");

    }


    @Test
    public void createDeploymentPython() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("服务任务流程")
            .source("本地测试")
            .tenantId("a")
            // .addClasspathResource("com/shareniu/ch11/servicetask.bpmn")
            .addClasspathResource("com/shareniu/ch11/scripttask_python1.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
        startProcessInstanceByKeyPython();
    }
    @Test
    public void startProcessInstanceByKeyPython() {
        Map<String,Object> var=new HashMap<String, Object>();
        var.put("var","execution.setVariable('python-foo', 'python-fooVal');\n");
        ProcessInstance scripttaskJs = runtimeService.startProcessInstanceByKey("scripttask_js",var);
    }


    @Test
    public void createDeploymentJuel() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("服务任务流程")
            .source("本地测试")
            .tenantId("a")
            // .addClasspathResource("com/shareniu/ch11/servicetask.bpmn")
            .addClasspathResource("com/shareniu/ch11/scripttask_juel2.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
        startProcessInstanceByKeyJuel();
    }
    @Test
    public void startProcessInstanceByKeyJuel() {
        Map<String,Object> var=new HashMap<String, Object>();
        var.put("echo","eecho");
        ProcessInstance scripttaskJs = runtimeService.startProcessInstanceByKey("scripttask_js",var);
    }
    /**
     *
     * sum = 0
     * for (i in intput){
     * sum += i
     * }
     */
    @Test
    public void createDeploymentGroovy() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("服务任务流程")
            .source("本地测试")
            .tenantId("a")
            // .addClasspathResource("com/shareniu/ch11/servicetask.bpmn")
            .addClasspathResource("com/shareniu/ch11/scripttask_groovy.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
        startProcessInstanceByKeyGroovy();
    }
    @Test
    public void startProcessInstanceByKeyGroovy() {
        int[] intput=new int[]{1,3,5};
        Map<String, Object> map = CollectionUtil.singletonMap("intput", intput);
        ProcessInstance scripttaskJs = runtimeService.startProcessInstanceByKey("scripttask_js",map);
    }



    @Test
    public void createDeploymentGroovyResource() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("服务任务流程")
            .source("本地测试")
            .tenantId("a")
            // .addClasspathResource("com/shareniu/ch11/servicetask.bpmn")
            .addClasspathResource("com/shareniu/ch11/scripttask_resource.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
        startProcessInstanceByKeyGroovyReouurce();
    }
    @Test
    public void startProcessInstanceByKeyGroovyReouurce() {
        int[] intput=new int[]{1,3,5};
        Map<String, Object> map = CollectionUtil.singletonMap("scriptPath", "com/shareniu/ch11/1.py");
        ProcessInstance scripttaskJs = runtimeService.startProcessInstanceByKey("scripttask_js",map);
    }


    @Test
    public void createDeploymentGroovyResource2() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("服务任务流程")
            .source("本地测试")
            .tenantId("a")
            // .addClasspathResource("com/shareniu/ch11/servicetask.bpmn")
            .addClasspathResource("com/shareniu/ch11/scripttask_resource1.bpmn")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
        startProcessInstanceByKeyGroovyReouurce2();
    }
    @Test
    public void startProcessInstanceByKeyGroovyReouurce2() {
        Map<String, Object> map = CollectionUtil.singletonMap("scriptReouurceBean", new ScriptReouurceBean());
        ProcessInstance scripttaskJs = runtimeService.startProcessInstanceByKey("scripttask_js",map);
    }

@Test
   public  void testJs(){
       // GIVEN
       // a function named sum
   String js=    "function sum(a,b){"
           + "  return a+b;"
           + "};"

           // THEN
           // i can call the function
           + "var result = sum(1,2);"

           + "execution.setVariable('foo', result);";
       System.out.println(js);


   }
@Test
   public  void testpython(){
       // GIVEN
       // a function named sum
   String python=  "execution.setVariable('foo', 'a')\n"

       // THEN
       // there should be a script variable defined
       + "if ( !foo ) {\n"
       + "  throw new Exception('Variable foo should be defined as script variable.')\n"
       + "}\n"

       // GIVEN
       // a script variable with the same name
       + "foo = 'b'\n"

       // THEN
       // it should not change the value of the execution variable
       + "if (execution.getVariable('foo') != 'a') {\n"
       + "  throw new Exception('Execution should contain variable foo')\n"
       + "}\n"

       // AND
       // it should override the visibility of the execution variable
       + "if (foo != 'b') {\n"
       + "  throw new Exception('Script variable must override the visibiltity of the execution variable.')\n"
       + "}";
       System.out.println(python);


   }



}


