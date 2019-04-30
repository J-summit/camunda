package shareniu.ch6;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TenantEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.commons.utils.IoUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdmTest {

    public  IdentityService identityService;
    @Before
    public  void  init(){
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("com/shareniu/ch6/camunda.cfg.xml");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
         identityService = processEngine.getIdentityService();
    }
    @Test
    public  void  getIdentityService(){

    }
    /**
     * : insert into ACT_ID_USER (ID_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, REV_) values ( ?, ?, ?, ?, ?, ?, 1 )
     * shareniu1(String), 分享牛(String), 分享牛(String), shareniu1@qq.com(String), 1(String), 1(String)
     */
    @Test
    public  void  saveUser(){
        UserEntity userEntity=new UserEntity();
        userEntity.setId("shareniu2");
        userEntity.setEmail("shareniu1@qq.com");
        userEntity.setFirstName("分享牛");
        userEntity.setLastName("分享牛");
        userEntity.setDbPassword("1");
        userEntity.setSalt("1");
        identityService.saveUser(userEntity);
    }
    /**
     * insert into ACT_ID_USER (ID_, FIRST_, LAST_, EMAIL_, PWD_, SALT_, REV_) values ( ?, ?, ?, ?, ?, ?, 1 )
     *  shareniu2(String), 分享牛2(String), 分享牛2(String), shareniu2@qq.com(String), {SHA-512}We0eviMnQxJsizhEieObjj0GRE/4DorJFsABCHtL8I42f80gpk1ApLwMNzMLEmJogSVCoaQFbuyJJ/Fn2Ep08w==(String), blvJEXcMUAotppV8W4HlQA==(String)
     */
    @Test
    public  void  saveUser2(){
        UserEntity userEntity=new UserEntity();
        userEntity.setId("shareniu4");
        userEntity.setEmail("shareniu2@qq.com");
        userEntity.setFirstName("分享牛2");
        userEntity.setLastName("分享牛2");
        userEntity.setPassword("1");
        identityService.saveUser(userEntity);
    }

    /**
     * select distinct RES.* from ACT_ID_USER RES order by RES.ID_ asc LIMIT ? OFFSET ?
     * 2147483647(Integer), 0(Integer)
     *
     *  select distinct RES.* from ACT_ID_USER RES WHERE RES.ID_ in ( ? , ? ) order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public  void  createUserQuery(){
        UserQuery userQuery = identityService.createUserQuery();
        List<User> list = userQuery
           // .userIdIn("shareniu1","shareniu2")//WHERE RES.ID_ in ( ? , ? )
            .userEmailLike("%shareniu%")// WHERE RES.EMAIL_ like ? ESCAPE '\\'
            .list();
        for (User user:list){
            System.out.println("################");
            System.out.println(user.getId());
            System.out.println(user.getEmail());
            System.out.println(user.getFirstName());
            System.out.println(user.getLastName());
            System.out.println(user.getPassword());
            System.out.println("################");

        }
    }

    /**
     * select distinct RES.* from ACT_ID_USER RES order by RES.ID_ asc LIMIT ? OFFSET ?
     *  3(Integer), 0(Integer)
     *
     *  select distinct RES.* from ACT_ID_USER RES order by RES.ID_ asc LIMIT ? OFFSET ?
     *  3(Integer), 3(Integer)
     *
     *  select distinct RES.* from ACT_ID_USER RES order by RES.ID_ asc LIMIT ? OFFSET ?
     *  3(Integer), 6(Integer)
     */
    @Test
    public  void  listPage(){
        int maxResults=3;
        int firstResult=maxResults*(3-1);
        UserQuery userQuery = identityService.createUserQuery();
        List<User> list = userQuery
            .listPage(firstResult,maxResults);
        for (User user:list){
            System.out.println("################");
            System.out.println(user.getId());
            System.out.println(user.getEmail());
            System.out.println(user.getFirstName());
            System.out.println(user.getLastName());
            System.out.println(user.getPassword());
            System.out.println("################");

        }
    }
    /**
     *delete from ACT_ID_USER where ID_ = ? and REV_ = ?
     * shareniu2(String), 1(Integer)
     * 21:47:33.027 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03082 Batch summary:
     * Result 0:	delete from ACT_ID_MEMBERSHIP where USER_ID_ = ?	Update counts: [0]
     * Result 1:	delete from ACT_ID_TENANT_MEMBER where USER_ID_ = ?	Update counts: [0]
     * Result 2:	delete from ACT_ID_USER where ID_ = ? and REV_ = ?	Update counts: [1]
     */
    @Test
    public  void  deleteUser(){
        String userId="shareniu2";
       identityService.deleteUser(userId);
    }
    /**
     * Preparing: insert into ACT_ID_GROUP (ID_, NAME_, TYPE_, REV_) values ( ?, ?, ?, 1 )
     *  dep(String), 项目经理(String), workflow(String)
     */
    @Test
    public  void  saveGroup(){
        GroupEntity groupEntity=new GroupEntity();
        groupEntity.setId("dep");
        groupEntity.setName("项目经理");
        groupEntity.setType("workflow");
        identityService.saveGroup(groupEntity);
    }
    /**
     * select distinct RES.* from ACT_ID_GROUP RES order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public  void  createGroupQuery(){
        GroupQuery groupQuery = identityService
            .createGroupQuery();
        List<Group> list = groupQuery.list();
        for (Group group:list){
            System.out.println("################");
            System.out.println(group.getId());
            System.out.println(group.getName());
            System.out.println(group.getType());
            System.out.println("################");

        }
    }
    /**
     * insert into ACT_ID_MEMBERSHIP (USER_ID_, GROUP_ID_) values ( ?, ? )
     * shareniu1(String), dep(String)
     */
    @Test
    public  void  createMembership(){
        String userId="shareniu1";
        String groupId="dep";
       identityService.createMembership(userId,groupId);
    }
    /**
     * Result 0:	delete from ACT_ID_MEMBERSHIP where GROUP_ID_ = ?	Update counts: [1]
     * Result 1:	delete from ACT_ID_TENANT_MEMBER where GROUP_ID_ = ?	Update counts: [0]
     * Result 2:	delete from ACT_ID_GROUP where ID_ = ? and REV_ = ?	Update counts: [1]
     */
    @Test
    public  void  deleteGroup(){
        String groupId="dep";
        identityService.deleteGroup(groupId);
    }

    /**
     * ing: insert into ACT_ID_TENANT (ID_, NAME_, REV_) values ( ?, ?, 1 )
     * 22:05:53.772 [main] DEBUG o.c.b.e.i.p.e.T.insertTenant - ==> Parameters: a(String), a系统(String)
     */
    @Test
    public  void  saveTenant(){
        TenantEntity tenantEntity=new TenantEntity();
        tenantEntity.setId("a");
        tenantEntity.setName("a系统");
        identityService.saveTenant(tenantEntity);
    }

    /**
     * insert into ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) values ( ?, ?, ?, ? )
     * 22:08:22.600 [main] DEBUG o.c.b.e.i.p.e.T.insertTenantMembership - ==> Parameters: 301(String), a(String), shareniu1(String), null
     */
    @Test
    public  void  createTenantUserMembership(){
        String tenantId="a";
        String userId="shareniu1";
        identityService.createTenantUserMembership(tenantId,userId);
    }

    /**
     *  insert into ACT_ID_TENANT_MEMBER (ID_, TENANT_ID_, USER_ID_, GROUP_ID_) values ( ?, ?, ?, ? )
     *  401(String), a(String), null, dep(String)
     */
    @Test
    public  void  createTenantGroupMembership(){
        String tenantId="a";
        String groupId="dep";
        identityService.createTenantGroupMembership(tenantId,groupId);
    }

    /**
     *  select distinct RES.* from ACT_ID_USER RES
     *  inner join ACT_ID_TENANT_MEMBER TM
     *  on RES.ID_ = TM.USER_ID_ WHERE TM.TENANT_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     */
    @Test
    public  void  createUserQueryMemberOfTenant(){
        String tenantId="a";
        UserQuery userQuery = identityService.createUserQuery();
        List<User> list = userQuery.memberOfTenant(tenantId).list();
        for (User user:list){
            System.out.println("################");
            System.out.println(user.getId());
            System.out.println(user.getEmail());
            System.out.println(user.getFirstName());
            System.out.println(user.getLastName());
            System.out.println(user.getPassword());
            System.out.println("################");

        }
    }

    /**
     * select distinct RES.* from ACT_ID_GROUP RES inner join ACT_ID_TENANT_MEMBER TM on RES.ID_ = TM.GROUP_ID_ WHERE TM.TENANT_ID_ = ? order by RES.ID_ asc LIMIT ? OFFSET ?
     *  a(String), 2147483647(Integer), 0(Integer)
     */
    @Test
    public  void  createGroupQueryMemberOfTenant(){
        String tenantId="a";
        List<Group> list = identityService
            .createGroupQuery()
            .memberOfTenant(tenantId).list();
        for (Group group:list){
            System.out.println("################");
            System.out.println(group.getId());
            System.out.println(group.getName());
            System.out.println(group.getType());
            System.out.println("################");

        }
    }

    /**
     * ring: insert into ACT_ID_INFO (ID_, USER_ID_, TYPE_, KEY_, VALUE_, PASSWORD_, PARENT_ID_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * 22:30:58.515 [main] DEBUG o.c.b.e.i.p.e.I.insertIdentityInfo - ==> Parameters: 501(String), shareniu2(String), account(String), shareniuaccountName(String), shareniuaccountUsername(String), java.io.ByteArrayInputStream@18f20260(ByteArrayInputStream), null
     * 22:30:58.515 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'INSERT'; Entity: 'IdentityInfoEntity[id=502]'
     * 22:30:58.516 [main] DEBUG o.c.b.e.i.p.e.I.insertIdentityInfo - ==> Parameters: 502(String), null, null, a(String), a(String), null, 501(String)
     * 22:30:58.516 [main] DEBUG org.camunda.bpm.engine.persistence - ENGINE-03009 SQL operation: 'INSERT'; Entity: 'IdentityInfoEntity[id=503]'
     * 22:30:58.517 [main] DEBUG o.c.b.e.i.p.e.I.insertIdentityInfo - ==> Parameters: 503(String), null, null, b(String), b(String), null, 501(String)
     */
    @Test
    public  void  setUserAccount(){
        String userId="shareniu2";
        String userPassword="1";
        String accountName="shareniuaccountName";
        String accountUsername="shareniuaccountUsername";
        String accountPassword="shareniuaccountPassword";
        Map<String, String> accountDetails=new HashMap<String, String>();
        accountDetails.put("a","a");
        accountDetails.put("b","b");
       identityService.setUserAccount(userId,userPassword,accountName,accountUsername,accountUsername,accountDetails);
    }


    @Test
    public  void  getUserAccountNames(){
        String userId="shareniu2";
        List<String> userAccountNames = identityService.getUserAccountNames(userId);
        System.out.println(userAccountNames);
    }
    /**
     * insert into ACT_ID_INFO (ID_, USER_ID_, TYPE_, KEY_, VALUE_, PASSWORD_, PARENT_ID_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     * Parameters: 601(String), shareniu2(String), userinfo(String), c(String), c(String), null, null
     */
    @Test
    public  void  setUserInfo(){
        String userId="shareniu2";
       identityService.setUserInfo(userId,"c","c");
    }
    /**
     * Info - ==>  Preparing: insert into ACT_ID_INFO (ID_, USER_ID_, TYPE_, KEY_, VALUE_, PASSWORD_, PARENT_ID_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, 1 )
     *  701(String), shareniu2(String), null, picture(String), 702(String), null, null
     *  insert into ACT_GE_BYTEARRAY(ID_, NAME_, BYTES_, DEPLOYMENT_ID_, TENANT_ID_, TYPE_, CREATE_TIME_, ROOT_PROC_INST_ID_, REMOVAL_TIME_, REV_) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 )
     * Parameters: 702(String), png(String), java.io.ByteArrayInputStream@297ea53a(ByteArrayInputStream), null, null, 1(Integer), 2019-02-13 22:38:49.424(Timestamp), null, null
     */
    @Test
    public  void  setUserPicture(){
        String userId="shareniu2";
        byte[] bytes = IoUtil
            .fileAsByteArray(new File("/Users/shareniu/workspace/shareniu-camunda-study/src/main/resources/com/shareniu/ch6/WX20190212-223238@2x.png"));
        Picture picture=new Picture(bytes,"png");
       identityService.setUserPicture(userId,picture);
    }

    /**
     * select * from ACT_ID_INFO where USER_ID_ = ? and KEY_ = ? and PARENT_ID_ is null
     *  shareniu2(String), c(String)
     * delete from ACT_ID_INFO where ID_ = ? and REV_ = ?
     *  601(String), 1(Integer)
     */
    @Test
    public  void  deleteUserInfo(){
        String userId="shareniu2";
        identityService.deleteUserInfo(userId,"c");
    }

}
