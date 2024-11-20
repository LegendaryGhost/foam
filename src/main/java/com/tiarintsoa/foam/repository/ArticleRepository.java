package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
