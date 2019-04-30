package shareniu.ch18;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.form.type.DateFormType;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormValidators;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FormTest {

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
            .createProcessEngineConfigurationFromResource("com/shareniu/ch18/camunda.cfg.xml");
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

    @Test
    public void createDeployment() {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        Deployment deployment = deploymentBuilder
            .name("外置表单流程")
            .source("本地测试")
            .tenantId("a")
            .addClasspathResource("com/shareniu/ch18/form3.bpmn")
//            .addClasspathResource("com/shareniu/ch18/start.html")
//            .addClasspathResource("com/shareniu/ch18/1.html")
//            .addClasspathResource("com/shareniu/ch18/2.html")
            .deploy();
        System.out.println(deploymentBuilder);
        System.out.println(deployment);
    }


    @Test
    public void getStartFormKey() {
        String processDefinitionId="form:1:6";
        String startFormKey = formService.getStartFormKey(processDefinitionId);
        System.out.println(startFormKey);
    }

    @Test
    public void getTaskFormKey() {
        String processDefinitionId="form:1:6";
     //   String taskDefinitionKey="Task_1usod4v";
        String taskDefinitionKey="Task_1urcmi1";
        String startFormKey = formService.getTaskFormKey(processDefinitionId,taskDefinitionKey);
        System.out.println(startFormKey);
    }

    /**
     *  Preparing: select * from ACT_GE_BYTEARRAY where DEPLOYMENT_ID_ = ? and NAME_ = ?
     * 1(String), start.html(String)
     */
    @Test
    public void getRenderedStartForm1() {
        String processDefinitionId="form:4:303";
        Object startFormKey = formService.getRenderedStartForm(processDefinitionId,"juel");
        System.out.println(startFormKey);
    }

    @Test
    public void getStartFormData() {
        String processDefinitionId="form:5:506";
        StartFormData startFormData = formService.getStartFormData(processDefinitionId);
        System.out.println(startFormData.getDeploymentId());
        System.out.println(startFormData.getProcessDefinition());
        System.out.println(startFormData.getFormKey());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        System.out.println(formProperties);
        List<FormField> formFields = startFormData.getFormFields();
        System.out.println(formFields);
    }


    @Test
    public void submitStartForm() {
        String processDefinitionId="form:5:506";
        VariableMap variableMap = Variables.createVariables()
            .putValue("days", 10)
            .putValue("reason", "请假三天")
            .putValue("startUserId", "张三")
            .putValue("category", "年假");

        ProcessInstance processInstance = formService.submitStartForm(processDefinitionId, variableMap);

    }

    @Test
    public void getTaskFormData() {
        Task task = taskService.createTaskQuery().singleResult();
        System.out.println(task.getId());
        TaskFormData taskFormData =
            formService.getTaskFormData(task.getId());
        System.out.println(taskFormData.getDeploymentId());
        System.out.println(taskFormData.getTask());
        System.out.println(taskFormData.getFormKey());
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        System.out.println(formProperties);
        List<FormField> formFields = taskFormData.getFormFields();
        System.out.println(formFields);
    }

    @Test
    public void getRenderedTaskForm() {
        //Task task = taskService.createTaskQuery().singleResult();
        Object juel = formService.getRenderedTaskForm("1315", "shareniu");
        System.out.println(juel);
    }

    @Test
    public void submitTaskForm2() {
        String processDefinitionId="form:5:506";

        Task task = taskService.createTaskQuery().singleResult();
        VariableMap variableMap = Variables.createVariables()
            .putValue("startUserId", "王五")
            .putValue("category", "年假");
        formService.submitTaskForm(task.getId(),variableMap);
    }

    @Test
    public void submitTaskForm() {
        String processDefinitionId=  "form:9:1403";
        StartFormData startFormData = formService.getStartFormData(processDefinitionId);

        List<FormField> formFields = startFormData.getFormFields();
        for (FormField formField:formFields){
            System.out.println("#######################");
            System.out.println(formField.getId());
            System.out.println(formField.getValue());
            System.out.println(formField.getTypeName());
            System.out.println(formField.getType());
            FormType formType = formField.getType();
            if (formType instanceof DateFormType){
                DateFormType dateFormType= (DateFormType) formType;
                Object information = dateFormType.getInformation("datePattern");
                System.out.println(information);
            }
            if (formType instanceof EnumFormType){
                EnumFormType enumFormType= (EnumFormType) formType;
                Object information = enumFormType.getInformation("values");
                System.out.println(information);
            }
            System.out.println(formField.getDefaultValue());
            System.out.println(formField.getLabel());
            Map<String, String> properties = formField.getProperties();
            Set<Map.Entry<String, String>> entries = properties.entrySet();
            System.out.println("???????");
            for(Map.Entry<String, String> entry:entries){
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
            System.out.println("???????");

            System.out.println("#######################");

        }
    }

    @Test
    public void getFormValidators() {

        FormValidators formValidators = processEngineConfiguration.getFormValidators();
        Map<String, Class<? extends FormFieldValidator>> validators = formValidators.getValidators();

        System.out.println(validators.get("min"));
        System.out.println(validators.get("max"));
        System.out.println(validators.get("minlength"));
        System.out.println(validators.get("maxlength"));
        System.out.println(validators.get("readonly"));
        System.out.println(validators.get("required"));
        System.out.println(validators.get("aa"));
        formValidators = new FormValidators();
//        formValidators.addValidator("min", MinValidator.class);
//        formValidators.addValidator("max", MaxValidator.class);
//        formValidators.addValidator("minlength", MinLengthValidator.class);
//        formValidators.addValidator("maxlength", MaxLengthValidator.class);
//        formValidators.addValidator("required", RequiredValidator.class);
//        formValidators.addValidator("readonly", ReadOnlyValidator.class);

    }



    @Test
    public void submitTaskForm3() {
        String processDefinitionId=  "form:8:1003";
        StartFormData startFormData = formService.getStartFormData(processDefinitionId);

        List<FormField> formFields = startFormData.getFormFields();
        for (FormField formField:formFields){

            System.out.println("#######################");
            List<FormFieldValidationConstraint> validationConstraints = formField.getValidationConstraints();
            for(FormFieldValidationConstraint formFieldValidationConstraint:validationConstraints){
                System.out.println(formFieldValidationConstraint.getName());
                System.out.println(formFieldValidationConstraint.getConfiguration());
            }
            System.out.println("#######################");

        }
    }


    @Test
    public void submitStartForm4() {
        String processDefinitionId=  "form:9:1403";
        VariableMap variableMap = Variables.createVariables()
            .putValue("days", 2)
            .putValue("reason", "11")
            .putValue("startUserId", "张三")
            .putValue("shareniuA", "c")
            .putValue("category", "年假");
      formService.submitStartForm(processDefinitionId,variableMap);
    }




}
