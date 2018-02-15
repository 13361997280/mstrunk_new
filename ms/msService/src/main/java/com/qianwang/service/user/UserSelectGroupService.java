package com.qianwang.service.user;

import com.qianwang.dao.domain.UserSelectGroup;
import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.user.UserRegist;
import com.qianwang.dao.domain.user.Users;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserSelectGroupService {
    /**
     * 查询所有
     * @return 用户信息集合
     */
    List<UserSelectGroup> findAll(Integer userId);
    /**
     * 插入用户
     * @param user
     */
    void insert(UserSelectGroup user);

    /**
     * 更新用户
     * @param user
     */
    void update(UserSelectGroup user);

    UserSelectGroup findOne(Integer id);

    Map getResultByPage(int page, int size, int userId);

}
