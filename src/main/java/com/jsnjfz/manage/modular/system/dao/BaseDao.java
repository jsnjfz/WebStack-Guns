package com.jsnjfz.manage.modular.system.dao;

import java.util.List;
import java.util.Map;

/**
 * <Description> <br>
 * 基础dao
 * 
 * @CreateDate 2016年5月26日 <br>
 */
public interface BaseDao<T> {
    /**
     * 根据id查询
     * 
     * @param id
     */
    T selectByPrimaryKey(String id);

    /**
     * 根据openid查询
     * 
     * @param id
     */
    T selectByOpenidKey(String open_id);
    /**
     * 根据id查询
     * 
     * @param id
     */
    T selectByPrimaryKey(Integer id);

    /**
     * 删除
     * 
     * @param id
     * @return <br>
     */
    int deleteByPrimaryKey(Integer id);
    
    /**
     * 删除
     * 
     * @param id
     * @return <br>
     */
    int deleteByPrimaryKey(String id);

    /**
     * 修改
     * 
     * @param id
     * @return <br>
     */
    int updateByPrimaryKeySelective(T obj);
    /**
     * 修改头像和昵称
     * 
     * @param id
     * @return <br>
     */
    int setUsernick(Map<String, Object> map);

    /**
     * 新增
     * 
     * @param id
     * @return <br>
     */
    int insertSelective(T obj);

    /**
     * Description: <br>
     * 统计条数
     * 
     * @return <br>
     */
    int getTotalCount(Map<String, Object> map);
    /**
     * Description: <br>
     * 统计已经学习过的条数
     * 
     * @return <br>
     */
    int getTotalCountYiZhi(Map<String, Object> map);

    /**
     * Description: <br>
     * 分页查询
     * 
     * @param map{startIndex,limit}
     * @return <br>
     */
    List<T> getList(Map<String, Object> map);
    
    /**
     * Description: <br>
     * 分页查询
     * 
     * @param map{startIndex,limit}
     * @return <br>
     */
    List<T> getCommList(Map<String, Object> map);
    
    /**
     * 判断多少用户学习此课程
     */
    List<T> getUserImage(String startlesson);
    /**
     * 判断可以推送的用户
     */
    List<T> getUserSubscribe(String subscribe);
    /**
     * 获取当前课程组中学习此课程的人数
     */
    List<T> getUserYiZhi(Map<String, Object> map);
    /**
     * 获取当前课程组中好友排行
     */
    List<T> getHaoYou(Map<String, Object> map);
    /**
     * 获取指定列的最大值
     * @param lie
     * @return
     */
    
    String getMaxLie(String lie);
    /**
     * 根据type类型获取list
     * @param type
     * @return
     */
    List<T> getTypeList(String type);
    
    /**
     * 批量修改
     * 
     * @param id
     * @return <br>
     */
    int updateAll(String onekey);

    int insert(T obj);
 
}
