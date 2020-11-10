package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    /**
     * 添加好友或非好友。
     *
     * @param friendid
     * @param type
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}", method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid, @PathVariable String type) {
        // 判断当前用户是否登录。
        Claims claims = (Claims) request.getAttribute("user_claims");

        if (claims == null) {
            new Result(false, StatusCode.ACCESS_ERROR, "请先登录。");
        }

        if ("1".equals(type)) {
            // 添加好友。
            int f = friendService.addFriend(claims.getId(), friendid);
            if (f == 0) {
                // 重复添加好友。
                new Result(false, StatusCode.REMOTE_ERROR, "你已添加过该好友。");
            }
            return new Result(true, StatusCode.OK, "添加好友成功。");
        } else {
            // 添加非好友（黑名单）。
            friendService.addNoFriend(claims.getId(), friendid);
            return new Result(true, StatusCode.OK, "添加非好友成功。");
        }
    }

    /**
     * 删除好友。
     */
    @RequestMapping(value = "/{friendid}", method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid) {
        // 判断当前用户是否登录。
        Claims claims = (Claims) request.getAttribute("user_claims");

        if (claims == null) {
            // 没有登录。
            return new Result(false, StatusCode.ACCESS_ERROR, "请先登录。");
        }

        // 删除该用户的某一个好友。
        friendService.deleteFriend(claims.getId(), friendid);

        return new Result(true, StatusCode.OK, "删除好友成功。");
    }

}
