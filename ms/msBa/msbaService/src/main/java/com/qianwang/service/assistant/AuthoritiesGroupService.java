package com.qianwang.service.assistant;

import com.qianwang.dao.domain.assistant.AuthoritiesGroup;

import java.util.List;

/**
 * Created by chenghaijiang on 2017/8/22.
 */
public interface AuthoritiesGroupService {
    Boolean add(AuthoritiesGroup entity);

    List<AuthoritiesGroup> getList();

    int getCount();

    Boolean edit(AuthoritiesGroup entity);

    Boolean delete(Integer id);

    AuthoritiesGroup getEntity(Integer id);
}
