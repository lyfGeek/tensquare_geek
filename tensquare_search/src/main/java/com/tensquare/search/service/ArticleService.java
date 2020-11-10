package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import util.IdWorker;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 添加
     *
     * @param article
     */
    public void add(Article article) {

        article.setId(idWorker.nextId() + "");
        articleDao.save(article);
    }

    /**
     * 搜索文章
     *
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findSearch(String keywords, int page, int size) {
        return articleDao.findByTitleOrContentLike(keywords, keywords, PageRequest.of(page - 1, size));

    }

}
