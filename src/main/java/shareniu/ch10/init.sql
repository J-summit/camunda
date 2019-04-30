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