package com.dqings.user.api;

import com.dqings.user.domain.User;

import java.util.Collection;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     *
     * @param user {@link User}
     * @return 保存成功返回<code>true</code>
     * 否则返回<code>false</code>
     */
    boolean save(User user);

    /**
     * 查询所有用户
     * @return 不反回null
     */
    Collection<User> findAll();

}
