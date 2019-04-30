package cn.summit.authorizationtest;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.authorization.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author onlyo
 * @see Resources
 * @see org.camunda.bpm.engine.authorization.Permissions
 * @since 2019/4/20 16:45
 */
public class AuthorizationTest {

    public AuthorizationService authorizationService;

    @Before
    public void init() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        authorizationService = processEngine.getAuthorizationService();
    }

    /**
     * 查找用户的权限
     * select distinct RES.* from ACT_RU_AUTHORIZATION RES WHERE RES.USER_ID_ in ( ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void findAuthorization() {
        List<Authorization> list = authorizationService
                .createAuthorizationQuery()
                .userIdIn("test1").list();
        for (Authorization authorization : list) {
            System.out.println(authorization.getId());
            System.out.println(authorization.getAuthorizationType());
            System.out.println(authorization.getGroupId());
            System.out.println(authorization.getResourceId());
            System.out.println(authorization.getResourceType());
            System.out.println(authorization.getUserId());
            //  System.out.println(authorization.getPermissions());
            System.out.println("————————————————————");

        }
    }

    /**
     * 授权
     * insert into ACT_RU_AUTHORIZATION ( ID_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     */
    @Test
    public void addAuthorization() {
        Resource resource1 = new TestResource("resource1", 100);
        Resource resource2 = new TestResource("resource2", 200);

        createAuthorization("test1", null, resource1, new Permission[]{Permissions.ALL});
        createAuthorization("test2", null, resource2, new Permission[]{Permissions.ACCESS});
    }


    public void createAuthorization(String userId, String groupId, Resource resource, Permission[] permissions) {

        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId(userId);
        authorization.setGroupId(groupId);
        authorization.setResource(resource);
        authorization.setResourceId(resource.resourceType() + "");
        authorization.setPermissions(permissions);

        authorizationService.saveAuthorization(authorization);
    }

    public static class TestResource implements Resource {
        String resourceName;
        int resourceType;

        public TestResource(String resourceName, int resourceType) {
            this.resourceName = resourceName;
            this.resourceType = resourceType;
        }

        @Override
        public String resourceName() {
            return resourceName;
        }

        @Override
        public int resourceType() {
            return resourceType;
        }
    }

    /**
     * 授权用户 Resources.APPLICATION all权限
     * insert into ACT_RU_AUTHORIZATION ( ID_, TYPE_, GROUP_ID_, USER_ID_, RESOURCE_TYPE_, RESOURCE_ID_, PERMS_, REV_ ) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     */
    @Test
    public void grantUser() {
        //授权
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("auth1");
        authorization.setResource(Resources.APPLICATION);//登入
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.ALL});
        authorizationService.saveAuthorization(authorization);
    }

    /**
     * 授权用户 对用户的curd
     */
    @Test
    public  void  addAuthorization3(){
        Authorization authorization=authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId("auth1");
        authorization.setResource(Resources.USER);
        authorization.setResourceId("*");
        authorization.setPermissions(new Permission[] {Permissions.READ,Permissions.CREATE,Permissions.UPDATE,Permissions.DELETE});
        authorizationService.saveAuthorization(authorization);
    }

}
