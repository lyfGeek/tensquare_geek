package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INoFriendDao extends JpaRepository<NoFriend, String> {
}
