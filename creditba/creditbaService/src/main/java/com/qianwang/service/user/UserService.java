package com.qianwang.service.user;

import java.util.List;
import java.util.Map;

public interface UserService {
    public static final String USER_CAS_ID_KEY = "id";
    public static final String USER_NAME_KEY = "username";
    public static final String USER_HYID_ID_KEY = "hyipUserId";
    public static final String USER_ENABLE_KEY = "enabled";
    public static final String USER_MOBILEPHONE_KEY = "mobile";
    public static final String USER_EMAIL_KEY = "email";
    public static final String USER_NICKNAME_KEY = "nickName";
    public static final String USER_LASTLOGIN_KEY = "lastLoginTime";

    /**
     *
     * @param userName
     * @return
     */
    Map<String, Object> getUserFromHyip(String userName);

    /**
     *
     * @param userId
     * @return
     */
    List<String> getUserRoles(Long userId);

}
