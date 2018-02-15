package com.qianwang.service.assistant;

import com.qianwang.dao.domain.assistant.Authorities;

import java.util.List;

/**
 * Created by chenghaijiang on 2017/8/21.
 */
public interface AuthoritiesService {
    Boolean addAuthorities(Authorities entity);

    List<Authorities> getAuthorities();

    int getAuthoritiesCount();

    Boolean editAuthorities(Authorities entity);

    Boolean removeAuthorities(Integer entityId);

    Authorities getAuthorities(Integer id);
}
