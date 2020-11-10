package com.tensquare.user.dao;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口。
 */
public interface IUserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 根据手机查询用户。
     *
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 更新关注数。
     *
     * @param userid
     * @param x
     */
    @Modifying
    @Query("update User u set u.followcount = u.followcount + ?2 where u.id = ?1")
    void updateFollowCount(String userid, int x);

    /**
     * 更新粉丝数。
     *
     * @param userid
     * @param x
     */
    @Modifying
    @Query("update User u set u.fanscount = u.fanscount + ?2 where u.id = ?1")
    void updateFansCount(String userid, int x);

}
