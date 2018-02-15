package com.qianwang.service.assistant;

import com.qianwang.dao.domain.assistant.AuthoritiesResource;

import java.util.List;

/**
 * Created by chenghaijiang on 2017/8/23.
 */
public interface AuthoritiesResourceService {
    Boolean add(AuthoritiesResource entity);

    List<AuthoritiesResource> getList();

    int getCount();

    Boolean edit(AuthoritiesResource entity);

    Boolean delete(Integer id);

    AuthoritiesResource getEntity(Integer id);
}
