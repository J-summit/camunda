package cn.summit.identitytest;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TenantEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author onlyo
 * @since 2019/4/20 11:27
 */
public class IdentityServiceTest {

    public IdentityService identityService;


    @Before
    public void init() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        identityService = processEngine.getIdentityService();
    }

    /**
     * 创建用户
     * Result 0:	insert into ACT_ID_USER (ID_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, REV_) values ( ?, ?, ?, ?, ?, ?, 1 )	Update counts: [1]
     * ACT_ID_USER
     */
    @Test
    public void createUser() {
        UserEntity user = new UserEntity();
        user.setId("auth1");
        user.setEmail("test1@qq.com");
        user.setFirstName("test1");
        user.setLastName("test2");
        // user.setDbPassword(); 密码未加密
        user.setPassword("1");

        identityService.saveUser(user);
    }

    /**
     * 查询/列表/分页
     * int maxResults=3;
     * int firstResult=maxResults*(3-1);
     */
    @Test
    public void getUserList() {
        List<User> userList = identityService.createUserQuery()
                .listPage(0, 3)
                // .list()
                ;
        userList.forEach(user -> {

            System.out.println("name:" + user.getId());
            System.out.println(user.getFirstName());
            System.out.println("_________________________-");
        });

    }

    /**
     * 删除用户
     * delete from ACT_ID_MEMBERSHIP where USER_ID_ = ?
     * delete from ACT_ID_TENANT_MEMBER where USER_ID_ = ?
     * delete from ACT_ID_USER where ID_ = ? and REV_ = ?
     */
    @Test
    public void deleteUser() {
        identityService.deleteUser("test1");
    }

    /**
     * 创建组
     * insert into ACT_ID_GROUP (ID_, NAME_, TYPE_, REV_) values ( ?, ?, ?, 1 )
     */
    @Test
    public void createGroup() {
        GroupEntity group = new GroupEntity();
        group.setId("group1");
        group.setName("经理");
        group.setType("管理");
        identityService.saveGroup(group);
    }

    /**
     * 查询群
     * select distinct RES.* from ACT_ID_GROUP RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void findGroup() {
        List<Group> list = identityService.createGroupQuery().list();
        list.forEach(x -> {
            System.out.println(x.getId());
            System.out.println(x.getName());
            System.out.println(x.getType());
            System.out.println("_________________-");
        });
    }

    /**
     * 用户和群建立连接
     * insert into ACT_ID_MEMBERSHIP (USER_ID_, GROUP_ID_) values ( ?, ? )
     */
    @Test
    public void createMemberShip() {
        String userId = "test1";
        String groupId = "group1";
        identityService.createMembership(userId, groupId);
    }

    /**
     * 删除组
     * <p>
     * delete from ACT_ID_MEMBERSHIP where GROUP_ID_ = ?
     * delete from ACT_ID_TENANT_MEMBER where GROUP_ID_ = ?
     * delete from ACT_ID_GROUP where ID_ = ? and REV_ = ?
     */
    @Test
    public void deleteGroup() {
        String groupId = "group1";
        identityService.deleteGroup(groupId);

    }

    /**
     * 创建租户
     * <p>
     * insert into ACT_ID_TENANT (ID_, NAME_, REV_) values ( ?, ?, 1 )
     */
    @Test
    public void createTenant() {
        TenantEntity tenant = new TenantEntity();
        tenant.setId("a");
        tenant.setName("a系统");
        identityService.saveTenant(tenant);
    }

    /**
     * 租户与群组关系
     * insert into ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) values ( ?, ?, ?, ? )
     */
    @Test
    public void createGroupTenantShip() {
        String tenantId = "a";
        String groupId = "group1";
        identityService.createTenantGroupMembership(tenantId, groupId);
    }

    /**
     * 租户与用户关系
     * insert into ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) values ( ?, ?, ?, ? )
     */
    @Test
    public  void  createTenantUserMembership(){
        String tenantId="a";
        String userId="test1";
        identityService.createTenantUserMembership(tenantId,userId);
    }

    /**
     * 租户下用户
     * select distinct RES.* from ACT_ID_USER RES inner join ACT_ID_TENANT_MEMBER TM on RES.ID_ = TM.USER_ID_ WHERE TM.TENANT_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public void findMemberBytenantId() {
        String tenantId = "a";
        List<User> list = identityService.createUserQuery().memberOfTenant(tenantId).list();
        list.forEach(user -> {

            System.out.println("name:" + user.getId());
            System.out.println(user.getFirstName());
            System.out.println("_________________________-");
        });

    }


}
