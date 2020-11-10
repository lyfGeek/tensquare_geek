package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin// 解决前端跨域请求问题。
@RequestMapping("/label")
@RefreshScope// 实时更新配置信息。
public class LabelController {

    @Autowired
    private LabelService labelService;
    @Value("${sms.ip}")
    private String ip;

    /**
     * 查询所有。
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功。", labelService.findAll());
    }

    /**
     * 查询一个。
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功。", labelService.findById(id));
    }

    /**
     * 添加。
     *
     * @param label
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Label label) {
        labelService.add(label);
        return new Result(true, StatusCode.OK, "添加成功。");
    }

    /**
     * 修改。
     *
     * @param label
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Label label, @PathVariable String id) {
        // 设置 id。
        label.setId(id);
        labelService.update(label);
        return new Result(true, StatusCode.OK, "修改成功。");
    }

    /**
     * 删除。
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        labelService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功。");
    }

    /**
     * 条件查询。
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        List<Label> list = labelService.findSearch(searchMap);
        return new Result(true, StatusCode.OK, "查询城功。", list);
    }

    /**
     * 条件 + 分页。
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Label> list = labelService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功。", new PageResult<>(list.getTotalElements(), list.getContent()));
    }

    /**
     * 读取自定义的配置。
     *
     * @return
     */
    @RequestMapping(value = "/testConfig", method = RequestMethod.GET)
    public Result testConfig() {
        return new Result(true, StatusCode.OK, ip);
    }

}
