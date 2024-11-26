package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("""
        SELECT MAX(a.id) FROM Article a
    """)
    Long findMaxId();

}
