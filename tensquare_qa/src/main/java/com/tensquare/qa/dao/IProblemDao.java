package com.tensquare.qa.dao;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口。
 */
public interface IProblemDao extends JpaRepository<Problem, String>, JpaSpecificationExecutor<Problem> {

    /**
     * 最新问答列表。
     *
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select pl.problemid from Pl pl where pl.labelid = ?1) order by p.replytime desc")
    Page<Problem> findNewListByLabelid(String labelid, Pageable pageable);

    /**
     * 热门问答列表。
     *
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select pl.problemid from Pl pl where pl.labelid = ?1) order by p.reply desc")
    Page<Problem> findHotListByLabelid(String labelid, Pageable pageable);

    /**
     * 等待回答列表。
     *
     * @param labelid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select pl.problemid from Pl pl where pl.labelid = ?1) and reply = 0 order by p.createtime desc")
    Page<Problem> findWaitListByLabelid(String labelid, Pageable pageable);

}
