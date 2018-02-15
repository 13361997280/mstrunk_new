package com.qianwang.service.assistant;

import com.qianwang.dao.domain.assistant.Users;

import java.util.List;

/**
 * Created by chenghaijiang on 2017/8/23.
 */
public interface UsersService {
    Boolean add(Users entity);

    List<Users> getList(Integer start, Integer size);

    int existUserName(String username);

    Boolean edit(Users entity);

    Boolean delete(Integer id);

    Users getEntity(Integer id);

    List<Users> getListForAuth(Integer id);

    int getCount();
}
