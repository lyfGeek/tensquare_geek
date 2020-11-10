package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;

/**
 * 吐槽 service。
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    // 注入 MongoDBTemplate。
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询所有。
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 查询一个。
     *
     * @param id
     * @return
     */
    public Spit findById(String id) {
        return spitDao.findById(id).get();
    }

    /**
     * 添加。
     */
    public void add(Spit spit) {

        // 使用 idWord 获取一个 id 值。
        spit.setId(idWorker.nextId() + "");
        spitDao.save(spit);

        // 判断哪些是吐槽的评论。
        if (spit.getParentid() != null && !spit.getParentid().equals("")) {
            // 更新该评论对应的吐槽的回复数 +1。

            // 创建查询对象。
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));

            // 创建修改对象。
            Update update = new Update();
            update.inc("comment", 1);

            mongoTemplate.updateFirst(query, update, "spit");
        }
    }

    /**
     * 修改。
     */
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 删除。
     */
    public void deleteById(String id) {
        spitDao.deleteById(id);
    }

    /**
     * 根据上级 ID 查询吐槽数据。
     *
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentid, int page, int size) {
        return spitDao.findByParentidOrderByPublishtime(parentid, PageRequest.of(page - 1, size));
    }

    /**
     * 点赞只修改对应的字段。
     *
     * @param id
     */
    public void thumbup(String id) {
        // 创建条件。
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        // 创建修改对象。
        Update update = new Update();
        // 增加字段值。
        update.inc("thumbup", 1);

        // 使用模块进行修改。
        mongoTemplate.updateFirst(query, update, "spit");
    }
//      /**
//       * 点赞
//       * @param id
//       */
//      public void thumbup(String id) {
//          //  1.先根据id查询该吐槽记录
//          Spit spit = findById(id);
//          //  2.修改字段
//          spit.setThumbup(spit.getThumbup() + 1);
//          //  3.保存
//          update(spit);
//      }

}
