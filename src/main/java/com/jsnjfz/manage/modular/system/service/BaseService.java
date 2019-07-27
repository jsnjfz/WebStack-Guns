package com.jsnjfz.manage.modular.system.service;

import com.jsnjfz.manage.modular.system.dao.BaseDao;
import com.jsnjfz.manage.modular.system.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <Description> <br>
 * 基础逻辑处理
 *
 * @CreateDate 2016年5月26日 <br>
 */
@Service
public class BaseService<T> {

    @Autowired
    private BaseDao<T> baseDao;

    /**
     * 根据id查询
     *
     * @param id
     */
    public T get(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        } else {
            return baseDao.selectByPrimaryKey(id);
        }
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    public T get(Integer id) {
        if (null == id) {
            return null;
        } else {
            return baseDao.selectByPrimaryKey(id);
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return <br>
     */
    public int delete(Integer id) {
        if (null == id) {
            return -1;
        } else {
            return baseDao.deleteByPrimaryKey(id);
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return <br>
     */
    public int delete(String id) {
        if (StringUtils.isEmpty(id)) {
            return -1;
        } else {
            return baseDao.deleteByPrimaryKey(id);
        }
    }

    /**
     * 修改
     *
     * @param id
     * @return <br>
     */
    public int update(T obj) {
        return baseDao.updateByPrimaryKeySelective(obj);
    }

    /**
     * Description: <br>
     * 统计条数
     *
     * @return <br>
     */
    public int getTotalCount(Map<String, Object> map) {
        return baseDao.getTotalCount(map);
    }

    /**
     * Description: <br>
     * 统计已经学习过的条数
     *
     * @return <br>
     */
    public int getTotalCountYiZhi(Map<String, Object> map) {
        return baseDao.getTotalCountYiZhi(map);
    }


    /**
     * Description: <br>
     * 分页查询
     *
     * @param map{startIndex,limit}
     * @return <br>
     */
    public List<T> getList(Integer size) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("offset", 0);
        map.put("size", size);
        return baseDao.getList(map);
    }

    /**
     * Description: <br>
     * 分页查询
     *
     * @param map{startIndex,limit}
     * @return <br>
     */
    public List<T> getList(Map<String, Object> map) {
        return baseDao.getList(map);
    }

    /**
     * Description: <br>
     * 去除字段查询
     *
     * @param map{startIndex,limit}
     * @return <br>
     */
    public List<T> getCommList(Map<String, Object> map) {
        return baseDao.getCommList(map);
    }

//    /**
//     * Description: <br>
//     * 分页查询
//     *
//     * @param map{startIndex,limit}
//     * @return <br>
//     */
//    public Pager<T> getPage(Integer pageNo) {
//        return getPage(new HashMap<String, Object>(), pageNo, null);
//    }
//
//    /**
//     * Description: <br>
//     * 分页查询
//     *
//     * @param map{startIndex,limit}
//     * @return <br>
//     */
//    public Pager<T> getPage(T obj, Integer pageNo) {
//        Map<String, Object> map = FiledUtil.getObjectValue(obj);
//        return getPage(map, pageNo, null);
//    }
//
//    /**
//     * Description: <br>
//     * 分页查询
//     *
//     * @param map{startIndex,limit}
//     * @return <br>
//     */
//    public Pager<T> getPage(Map<String, Object> map, Integer pageNo) {
//        return getPage(map, pageNo, null);
//    }
//
//    /**
//     * Description: <br>
//     * 分页查询
//     *
//     * @param map{startIndex,limit}
//     * @return <br>
//     */
//    public Pager<T> getPage(Map<String, Object> map, Integer pageNo, Integer size) {
//        if (map == null) {
//            map = new HashMap<String, Object>();
//        }
//        int total = getTotalCount(map);
//        Pager<T> page = new Pager<T>(total, pageNo, size);
//        map.put("offset", page.getOffset());
//        map.put("size", page.getLimit());
//        page.setList(getList(map));
//        return page;
//    }
//    /**
//     * Description: <br>
//     * 分页查询
//     *
//     * @param map{startIndex,limit}
//     * @return <br>
//     */
//    public Pager<T> getCommList(Map<String, Object> map, Integer pageNo, Integer size) {
//        if (map == null) {
//            map = new HashMap<String, Object>();
//        }
//        int total = getTotalCount(map);
//        Pager<T> page = new Pager<T>(total, pageNo, size);
//        map.put("offset", page.getOffset());
//        map.put("size", page.getLimit());
//        page.setList(getCommList(map));
//        return page;
//    }

    /**
     * 修改或保存
     */
    public int saveOrUpdate(T obj, Integer id) {
        int count = 0;
        if (obj == null) {
            count = -1;
        } else if (null == id) {
            count = insert(obj);
        } else {
            count = update(obj);
        }
        return count;
    }

    /**
     * 修改或保存
     */
    public int saveOrUpdate(T obj, String id) {
        int count = 0;
        if (obj == null) {
            count = -1;
        } else if (StringUtils.isEmpty(id)) {
            count = insert(obj);
        } else {
            count = update(obj);
        }
        return count;
    }

    public int insert(T obj) {
        return baseDao.insertSelective(obj);
    }

    /**
     * 判断多少用户学习此课程
     */
    public List<T> getUserImage(String startlesson, String type) {
        return baseDao.getUserImage(startlesson);
    }

    /**
     * 判断多少用户学习此课程
     */
    public List<T> getUserYiZhi(Map<String, Object> map) {
        return baseDao.getUserYiZhi(map);
    }

    /**
     * 获取好友排行
     */
    public List<T> getHaoYou(Map<String, Object> map) {
        return baseDao.getHaoYou(map);
    }

    /**
     * 获取指定列的最大值
     */
    public String getMaxLie(String lie) {
        return baseDao.getMaxLie(lie);
    }

    /**
     * 批量修改
     */
    public int updateAll(String onekey) {
        return baseDao.updateAll(onekey);
    }

    /**
     * 修改头像和昵称
     */
    public int setUsernick(Map<String, Object> map) {
        return baseDao.setUsernick(map);
    }

}
