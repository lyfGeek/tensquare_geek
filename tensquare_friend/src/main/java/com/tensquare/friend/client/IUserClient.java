package com.tensquare.friend.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户模块远程接口。
 */
@FeignClient("tensquare-user")
public interface IUserClient {

    /**
     * 更新关注数。
     *
     * @param userid
     * @param x
     * @return
     */
    @RequestMapping(value = "/user/updateFollowcount/{userid}/{x}", method = RequestMethod.PUT)
    Result updateFollowCount(@PathVariable("userid") String userid, @PathVariable("x") int x);

    /**
     * 更新粉丝数。
     *
     * @param userid
     * @param x
     * @return
     */
    @RequestMapping(value = "/user/updateFanscount/{userid}/{x}", method = RequestMethod.PUT)
    Result updateFansCount(@PathVariable("userid") String userid, @PathVariable("x") int x);

}
