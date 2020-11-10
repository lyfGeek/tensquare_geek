package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽 dao。
 */
public interface SpitDao extends MongoRepository<Spit, String> {

    /**
     * 根据上级 ID 查询吐槽评论。
     *
     * @param parentid
     * @param pageable
     * @return
     */
    Page<Spit> findByParentidOrderByPublishtime(String parentid, Pageable pageable);

}
