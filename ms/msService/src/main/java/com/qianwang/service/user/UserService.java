package com.qianwang.service.user;

import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.user.UserRegist;
import com.qianwang.dao.domain.user.Users;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 查询所有
     * @return 用户信息集合
     */
    List<Users> findAllUser();

    /**
     * 插入用户
     * @param user
     */
    Integer insertUser(Users user);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(Users user);

    /**
     * 根据用户名加载用户
     * @param username
     * @return
     */
    Users loadUser(String username);

    /**
     * 根据用户名删除用户
     * @param username
     * @return
     */
    void deleteUser(String username);

    /**
     * 配置用户权限信息
     * @param username 用户名
     * @param authorities 逗号分隔权限集合
     */
    void configUser(String username, String authorities);

    /**
     * 根据用户名查询权限菜单
     * @param username
     * @return
     */
    List<Menu> findUserMenu(String username, int parentId);

    List<Map<String, Object>>getUserSource(String startDate,
                                           String endDate, String registerChannel, String spreadType, Integer registerArea, Integer tuijian);

    List<Map<String, Object>> getUserSourceGroup(String startDate,
                                                 String endDate, String registerChannel, String spreadType,
                                                 Integer registerArea, Integer tuijian);

    List<UserRegist> selectUserRegistRecent(Date statDate);

    List<UserRegist> selectUserRegist(Date startDate, Date endDate);

    List<Map<String, Object>> getUserSourceHour(String startDate,
                                                String endDate, String registerChannel, String spreadType,
                                                Integer registerArea, Integer tuijian);

    List<Map<String, Object>> getUserSourceHourGroup(String startDate,
                                                     String endDate, String registerChannel, String spreadType,
                                                     Integer registerArea, Integer tuijian);

    List<Map<String, Object>> getPUser(int presenterId, String sort, int start, int length);

    int getPUserCnt(int presenterId);

    List<Map<String, Object>> getCUser(int presenterId, String sort, int start,
                                       int length);

    int getCUserCnt(int presenterId);

    List<Map<String, Object>> getTicketBee(String startDate, String endDate, String xiaomifeng, String changdi, String sort, int start, int length);

    int getTicketBeeCnt(String startDate, String endDate, String xiaomifeng, String changdi);

    List<Map<String, Object>> getChangdi();

    List<Map<String, Object>> getSignInfo(String startDate, String endDate);

    List<Map<String, Object>> getClassifySignInfo(String startDate, String endDate);

    List<Map<String, Object>> getSignHourInfo(String startDate, String endDate);

    List<Map<String, Object>> getClassifySignHourInfo(String startDate, String endDate);

    Map<String, Object> getXiaomifengByName(String xiaomifeng);

    void saveXiaomifeng(String xiaomifeng, String changdi, String username);

    Map<String, Object> getXiaomifengUserByName(String xiaomifeng);

    List<Map<String, Object>> getMarketIndexInfo(String startDate, String endDate);
    List<Map<String, Object>> getMarketIndexRealInfo(String startDate, String endDate);
    List<Map<String, Object>> getAssetsInfo(String startDate, String endDate);
    List<Map<String, Object>> getAssetsRealInfo(String startDate, String endDate);
    Map<String, Object> getUserIdByName(String username);
    List<Map<String,Object>> getCUsersByPresentId(int presenterId);

    List<Map<String,Object>> getUserRechargeDist(String startDate, String endDate);

    List<Map<String,Object>> getUserSourceRec(String startDate, String endDate);

    List<Map<String,Object>> getUserSourceRecHour(String startDate, String endDate);

    List<Map<String,Object>> getAllUserTaskActivity(String startDate, String endDate,
                                                    String startAssets, String endAssets, String type);

    List<Map<String,Object>> getUserTaskActivityTblInfo(String startDate, String endDate);

    List<Map<String, Object>> findUserInfoByMobile(String mobile);

    List<Map<String, Object>> findLastRegistUserRecent();



}
