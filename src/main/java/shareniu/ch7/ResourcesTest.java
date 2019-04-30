package shareniu.ch7;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.*;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ResourcesTest {

    public  IdentityService identityService;
    AuthorizationService authorizationService;
    ProcessEngineConfigurationImpl processEngineConfiguration;
    ManagementService managementService;
    @Before
    public  void  init(){
         processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("com/shareniu/ch7/camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
         identityService = processEngine.getIdentityService();
         authorizationService = processEngine.getAuthorizationService();
         managementService = processEngine.getManagementService();
    }
    /**
     * select distinct RES.* from ACT_RU_AUTHORIZATION RES WHERE RES.USER_ID_ in ( ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     *  demo(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public  void  queryUserAuthorization(){
        List<Authorization> list = authorizationService
            .createAuthorizationQuery()
          .userIdIn("shareniu1")
           // .groupIdIn("a") //WHERE RES.GROUP_ID_ in ( ? )
            .list();
        for (Authorization authorization: list) {
            System.out.println("################");
            System.out.println(authorization.getId());
            System.out.println(authorization.getAuthorizationType());
            System.out.println(authorization.getGroupId());
            System.out.println(authorization.getResourceId());
            System.out.println(authorization.getResourceType());
            System.out.println(authorization.getUserId());
          //  System.out.println(authorization.getPermissions());
            System.out.println("################");

        }
    }
    /**
     *
     *
     *  insert into ACT_RU_AUTHORIZATION ( ID_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * 801(String), 1(Integer), null, shareniu1(String), 100(Integer), 100(String), 2147483647(Integer)
     * insert into ACT_RU_AUTHORIZATION ( ID_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     *  802(String), 1(Integer), null, shareniu2(String), 200(Integer), 200(String), 32(Integer)
     */
    @Test
    public  void  addAuthorization(){
        Resource resource1=new TestResource("resource1",100);
        Resource resource2=new TestResource("resource2",200);

        createAuthorization("shareniu1",null,resource1, new Permission[] {Permissions.ALL});
        createAuthorization("shareniu2",null,resource2, new Permission[] {Permissions.ACCESS});
    }



    public  void   createAuthorization(String userId,String groupId, Resource resource,Permission[] permissions){

        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId(userId);
        authorization.setGroupId(groupId);
        authorization.setResource(resource);
        authorization.setResourceId(resource.resourceType()+"");
        authorization.setPermissions(permissions);

        authorizationService.saveAuthorization(authorization);
    }

    public  static  class  TestResource implements  Resource{
        String resourceName;
        int resourceType;
        public TestResource(String resourceName,int resourceType) {
            this.resourceName=resourceName;
            this.resourceType=resourceType;
        }
        public String resourceName() {
            return resourceName;
        }
        public int resourceType() {
            return resourceType;
        }
    }


    @Test
    public  void  addAuthorization2(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.APPLICATION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});

        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization3(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.USER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ,Permissions.CREATE,Permissions.UPDATE,Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

    /**
     * 授权访问资源名称是组
     */
    @Test
    public  void  addAuthorization4(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.GROUP);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ,Permissions.CREATE,Permissions.UPDATE,Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }
    /**
     * 租户资源
     */
    @Test
    public  void  addAuthorization5(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.TENANT);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ,Permissions.CREATE,Permissions.UPDATE,Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization6(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.GROUP_MEMBERSHIP);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ,Permissions.CREATE,Permissions.UPDATE,Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization7(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.TENANT_MEMBERSHIP);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ,Permissions.CREATE,Permissions.UPDATE,Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization8(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.AUTHORIZATION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ,Permissions.CREATE,Permissions.UPDATE,Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

    ////////////////////////CockPit

    @Test
    public  void  addAuthorization9(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.PROCESS_DEFINITION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization10(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.DECISION_DEFINITION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization11(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.TASK);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization12(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.DEPLOYMENT);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization13(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.BATCH);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    @Test
    public  void  addAuthorization14(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.PROCESS_INSTANCE);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

////////////////TaskList
    @Test
    public  void  addAuthorization15(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniu4");
        authorization.setResource(Resources.FILTER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }
    @Test
    public  void  addAuthorization16(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniud");
        authorization.setResource(Resources.AUTHORIZATION);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }


    @Test
    public  void  setAuthorizationEnabledFalse(){


        processEngineConfiguration.setAuthorizationEnabled(false);

        identityService.setAuthenticatedUserId("shareniuc");

        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniuc");
        authorization.setResource(Resources.USER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ});
        authorizationService.saveAuthorization(authorization);
        managementService.getProperties();

    }
    @Test
    public  void  setAuthorizationEnabledTrue(){

        processEngineConfiguration.getAdminUsers().add("shareniud");
        processEngineConfiguration.getAdminGroups().add("shareniu-c");
        processEngineConfiguration.setAuthorizationEnabled(true);
        identityService.setAuthenticatedUserId("shareniud");

        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("shareniud");
        authorization.setResource(Resources.USER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ});
        authorizationService.saveAuthorization(authorization);
        managementService.getProperties();
    }
}
