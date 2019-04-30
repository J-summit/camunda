package shareniu.ch8;

import org.apache.commons.io.FileUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.repository.*;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.commons.utils.IoUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

public class DeploymentTest {

    public IdentityService identityService;
    AuthorizationService authorizationService;
    ProcessEngineConfigurationImpl processEngineConfiguration;
    ManagementService managementService;
    RepositoryService repositoryService;
    @Before
    public void init() {
        processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("com/shareniu/ch8/camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        identityService = processEngine.getIdentityService();
        authorizationService = processEngine.getAuthorizationService();
        managementService = processEngine.getManagementService();
        repositoryService = processEngine.getRepositoryService();
    }

    /**
     * Preparing: insert into ACT_RE_DEPLOYMENT(ID_, NAME_, DEPLOY_TIME_, SOURCE_, TENANT_ID_) values(?, ?, ?, ?, ?)
     * 09:29:31.139 [main] DEBUG o.c.b.e.i.p.e.D.insertDeployment - ==> Parameters: 4001(String), 分享牛请假流程(String), 2019-02-16 09:29:30.975(Timestamp), 本地测试(String), a(String)
     * 09:29:31.139 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'INSERT'; Entity: 'ResourceEntity[id=4002]'
     * 09:29:31.139 [main] DEBUG o.c.b.e.i.p.e.R.insertResource - ==>  Preparing: insert into ACT_GE_BYTEARRAY( ID_, NAME_, BYTES_, DEPLOYMENT_ID_, GENERATED_, TENANT_ID_, TYPE_, CREATE_TIME_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, ?, 1)
     * 09:29:31.141 [main] DEBUG o.c.b.e.i.p.e.R.insertResource - ==> Parameters: 4002(String), com/shareniu/ch8/leave.bpmn(String), java.io.ByteArrayInputStream@1922e6d(ByteArrayInputStream), 4001(String), false(Boolean), null, 1(Integer), 2019-02-16 09:29:30.993(Timestamp)
     * 09:29:31.141 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'INSERT'; Entity: 'ProcessDefinitionEntity[id=leave:1:4003]'
     * 09:29:31.141 [main] DEBUG o.c.b.e.i.p.e.P.insertProcessDefinition - ==>  Preparing: insert into ACT_RE_PROCDEF(ID_, CATEGORY_, NAME_, KEY_, VERSION_, DEPLOYMENT_ID_, RESOURCE_NAME_, DGRM_RESOURCE_NAME_, HAS_START_FORM_KEY_, SUSPENSION_STATE_, TENANT_ID_, VERSION_TAG_, HISTORY_TTL_, STARTABLE_, REV_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )
     * 09:29:31.142 [main] DEBUG o.c.b.e.i.p.e.P.insertProcessDefinition - ==> Parameters: leave:1:4003(String), http://bpmn.io/schema/bpmn(String), 请假流程(String), leave(String), 1(Integer), 4001(String), com/shareniu/ch8/leave.bpmn(String), null, false(Boolean), 1(Integer), a(String), null, null, true(Boolean)
     */
    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("a")
            .addClasspathResource("com/shareniu/ch8/leave.bpmn")
            .addClasspathResource("com/shareniu/ch8/leave1.bpmn")
            // .nameFromDeployment("101")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void addString() {
        String resourceName = "3.bpmn";
        String text = IoUtil.fileAsString("com/shareniu/ch8/leave.bpmn");
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("a")
            .addString(resourceName, text)
            // .nameFromDeployment("101")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void addInputStream() {
        String resourceName = "3.bpmn";
        InputStream inputStream = DeploymentTest.class
            .getClassLoader().getResourceAsStream("com/shareniu/ch8/leave.bpmn");
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("a")
            .addInputStream(resourceName, inputStream)
            // .nameFromDeployment("101")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void addZipInputStream() {
        InputStream inputStream = DeploymentTest.class
            .getClassLoader().getResourceAsStream("com/shareniu/ch8/leave.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("b")
            .addZipInputStream(zipInputStream)
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void addDeploymentResourceById() {
        String deploymentId = "101";
        String resourceId = "104";
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("b")
            .addDeploymentResourceById(deploymentId, resourceId)
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void addDeploymentResourceByName() {
        String deploymentId = "201";
        String resourceName = "leave.bpmn";
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("b")
            .addDeploymentResourceByName(deploymentId, resourceName)
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void addDeploymentResourcesByIds() {
        String deploymentId = "101";
        List<String> ids = new ArrayList<String>();
        ids.add("103");
        ids.add("104");
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("b")
            .addDeploymentResourcesById(deploymentId, ids)
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }

    @Test
    public void addDeploymentResourcesByName() {
        String deploymentId = "101";
        List<String> ids = new ArrayList<String>();
        ids.add("leave1.bpmn");
        ids.add("leave.bpmn");
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("分享牛请假流程")
            .source("本地测试")
            .tenantId("b")
            .addDeploymentResourcesByName(deploymentId, ids)
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }
    /**
     * select distinct RES.* from ACT_RE_PROCDEF RES order by RES.ID_ asc LIMIT ? OFFSET ?
     * <p>
     * select distinct RES.* from ACT_RE_PROCDEF RES WHERE (RES.SUSPENSION_STATE_ = ?) order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void createProcessDefinitionQuery() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
            .active()
            .list();
        for (ProcessDefinition pd : list
        ) {
            System.out.println(pd.getId());
        }
    }
    /**
     * delete from ACT_RU_IDENTITYLINK where PROC_DEF_ID_ = ?	Update counts: [0]
     * Result 1:	delete from ACT_RE_PROCDEF where ID_ = ?	Update counts: [1]
     * Result 2:	DELETE FROM ACT_RU_JOBDEF where PROC_DEF_ID_ = ?	Update counts: [0]
     */
    @Test
    public void deleteProcessDefinition() {
        String processDefinitionId = "leave1:1:105";
        repositoryService.deleteProcessDefinition(processDefinitionId);
    }

    /**
     * delete from ACT_RU_IDENTITYLINK where PROC_DEF_ID_ = ?	Update counts: [0]
     * Result 1:	delete B from ACT_GE_BYTEARRAY B inner join ACT_HI_JOB_LOG J on B.ID_ = J.JOB_EXCEPTION_STACK_ID_ and J.JOB_EXCEPTION_STACK_ID_ is not null and J.PROCESS_DEF_ID_ = ?	Update counts: [0]
     * Result 2:	delete from ACT_RE_PROCDEF where ID_ = ?	Update counts: [1]
     * Result 3:	delete from ACT_HI_IDENTITYLINK where PROC_DEF_ID_ = ?	Update counts: [0]
     * Result 4:	delete from ACT_HI_INCIDENT where PROC_DEF_ID_ = ? and PROC_INST_ID_ is null	Update counts: [0]
     * Result 5:	delete from ACT_HI_JOB_LOG where PROCESS_DEF_ID_ = ?	Update counts: [0]
     * Result 6:	DELETE FROM ACT_RU_JOBDEF where PROC_DEF_ID_ = ?	Update counts: [0]
     */
    @Test
    public void deleteProcessDefinitionCacade() {
        String processDefinitionId = "leave:1:106";
        repositoryService.deleteProcessDefinition(processDefinitionId, true);
    }
    /**
     * delete from ACT_RU_IDENTITYLINK where PROC_DEF_ID_ = ?	Update counts: [0, 0]
     * Result 1:	delete B from ACT_GE_BYTEARRAY B inner join ACT_HI_JOB_LOG J on B.ID_ = J.JOB_EXCEPTION_STACK_ID_ and J.JOB_EXCEPTION_STACK_ID_ is not null and J.PROCESS_DEF_ID_ = ?	Update counts: [0, 0]
     * Result 2:	delete from ACT_RE_PROCDEF where ID_ = ?	Update counts: [1, 1]
     * Result 3:	delete from ACT_HI_IDENTITYLINK where PROC_DEF_ID_ = ?	Update counts: [0, 0]
     * Result 4:	delete from ACT_HI_INCIDENT where PROC_DEF_ID_ = ? and PROC_INST_ID_ is null	Update counts: [0, 0]
     * Result 5:	delete from ACT_HI_JOB_LOG where PROCESS_DEF_ID_ = ?	Update counts: [0, 0]
     * Result 6:	DELETE FROM ACT_RU_JOBDEF where PROC_DEF_ID_ = ?	Update counts: [0, 0]
     */
    @Test
    public void deleteProcessDefinitionsSelectBuilder() {
        DeleteProcessDefinitionsSelectBuilder deleteProcessDefinitionsSelectBuilder = repositoryService.deleteProcessDefinitions();
        deleteProcessDefinitionsSelectBuilder.byIds("leave:5:505", "leave:4:405").cascade()
            .delete();
    }

    @Test
    public void getProcessModel() throws IOException {
        String processDefinitionId = "leave:1:6";
        InputStream inputStream = repositoryService.getProcessModel(processDefinitionId);
        FileUtils.copyInputStreamToFile(inputStream, new File("/Users/shareniu/workspace/shareniu-camunda-study/src/main/resources/com/shareniu/ch8/tmp.txt"));
    }
    @Test
    public void getProcessDiagram() throws IOException {
        String processDefinitionId = "leave1:1:5";
        InputStream inputStream = repositoryService.getProcessDiagram(processDefinitionId);
        FileUtils.copyInputStreamToFile(inputStream, new File("/Users/shareniu/workspace/shareniu-camunda-study/src/main/resources/com/shareniu/ch8/tmp2.png"));
    }

    /**
     * select distinct RES.* from ACT_RE_DEPLOYMENT RES order by RES.ID_ asc LIMIT ? OFFSET ?
     *
     * @throws IOException
     */
    @Test
    public void createDeploymentQuery() throws IOException {

        List<Deployment> list = repositoryService.createDeploymentQuery()
            .list();
        for (Deployment de : list
        ) {
            System.out.println(de.getId());
        }
    }

    @Test
    public void getProcessDiagramLayout() throws IOException {
        String processDefinitionId = "leave:1:6";
        DiagramLayout processDiagramLayout = repositoryService.getProcessDiagramLayout(processDefinitionId);

        Map<String, DiagramElement> elements = processDiagramLayout.getElements();
        Set<Map.Entry<String, DiagramElement>> entries = elements.entrySet();
        for (Map.Entry<String, DiagramElement> entry : entries) {
            String key = entry.getKey();
            DiagramElement value = entry.getValue();
            System.out.println(key + "," + value);
        }
    }

    @Test
    public void bpmnModelInstance() throws IOException {
        BpmnModelInstance bpmnModelInstance = Bpmn.createExecutableProcess("leave")
            .startEvent()
            .name("开始节点")
            .userTask()
            .name("申请人")
            .camundaCandidateUsers("张三")
            .exclusiveGateway()
            .name("排他网关")
            .condition("小于三天", "${day<3}")
            .userTask()
            .name("组长审批")
            .camundaCandidateUsers("李四")
            .endEvent()

            .findLastGateway()
            .builder()
            .condition("大于等于三天", "${day>=3}")
            .userTask()
            .name("组长审批")
            .camundaCandidateUsers("李四")
            .userTask()
            .name("项目经理审批")
            .camundaCandidateUsers("王五")
            .endEvent()
            .done();

        Deployment deploy = repositoryService.createDeployment()
            .addModelInstance("customer.bpmn", bpmnModelInstance)
            .deploy();
    }


    @Test
    public void getBpmnModelInstance() throws IOException {
        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance("leave:2:903");

        Collection<UserTask> modelElementsByType = bpmnModelInstance.getModelElementsByType(UserTask.class);
        System.out.println("######"+modelElementsByType);

        Deployment deploy = repositoryService.createDeployment()
            .addModelInstance("customer.bpmn", bpmnModelInstance)
            .deploy();
    }

}


