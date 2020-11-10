package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IFriendDao extends JpaRepository<Friend, String> {

    /**
     * 查询用户添加好友记录。
     *
     * @param userid
     * @param friendid
     * @return
     */
    @Query("select count(f) from Friend f where f.userid=?1 and f.friendid=?2")
    int selectCount(String userid, String friendid);

    /**
     * 更新 islike 状态。
     *
     * @param userid
     * @param friendid
     * @param islike
     */
    @Modifying
    @Query("update Friend f set f.islike = ?3 where f.userid=?1 and f.friendid=?2")
    void updateLike(String userid, String friendid, String islike);

    /**
     * 删除好友。
     *
     * @param userid
     * @param friendid
     */
    @Modifying
    @Query("delete from Friend f where f.userid=?1 and f.friendid=?2")
    void deleteFriend(String userid, String friendid);

}
