package shareniu.ch1;

import org.camunda.bpm.engine.*;

/**
 * deployment.lock	0	1
 * history.cleanup.job.lock	0	1
 * historyLevel	2	1
 * next.dbid	1	1
 * schema.history	create(fox)	1
 * startup.lock	0	1
 */
public class ProcessEngineTest {

    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
        String name = processEngine.getName();
        System.out.println(name);
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        System.out.println("流程引擎配置类："+processEngineConfiguration);
        AuthorizationService authorizationService = processEngine.getAuthorizationService();
        System.out.println("authorizationService:"+authorizationService);
        //CMMN引擎相关的
        CaseService caseService = processEngine.getCaseService();
        System.out.println(caseService);
        //DMN引擎相关的
        DecisionService decisionService = processEngine.getDecisionService();
        System.out.println(decisionService);
        ExternalTaskService externalTaskService = processEngine.getExternalTaskService();
        System.out.println(externalTaskService);
        FilterService filterService = processEngine.getFilterService();
        System.out.println(filterService);
        FormService formService = processEngine.getFormService();
        System.out.println(formService);
        HistoryService historyService = processEngine.getHistoryService();
        System.out.println(historyService);
        IdentityService identityService = processEngine.getIdentityService();
        System.out.println(identityService);
        ManagementService managementService = processEngine.getManagementService();
        System.out.println(managementService);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        System.out.println(repositoryService);
        RuntimeService runtimeService = processEngine.getRuntimeService();
        System.out.println(runtimeService);
        TaskService taskService = processEngine.getTaskService();
        System.out.println(taskService);
    }

}
