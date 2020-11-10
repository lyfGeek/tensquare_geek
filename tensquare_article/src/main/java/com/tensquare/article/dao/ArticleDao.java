package com.tensquare.article.dao;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口。
 */
public interface ArticleDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

    /**
     * 文章审核。
     *
     * @param id
     */
    @Modifying// 注意:如果@Query注解来进行更新,必须带上此注解,否则无法完成
    @Query("update Article a set a.state ='1' where a.id=?1")
    void examine(String id);

    /**
     * 点赞。
     *
     * @param id
     */
    @Modifying
    @Query("update Article a set a.thumbup=(a.thumbup+1) where a.id=?1")
    void thumbUp(String id);

}
