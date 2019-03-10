package com.serverApp.serverApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serverApp.serverApp.models.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "SELECT COUNT(id) FROM articles", nativeQuery = true)
    int getNumArticles();

    @Query(value = "SELECT * FROM articles", nativeQuery = true)
    Article[] getAllArticles();

    @Query(value = "SELECT count(*) FROM articles WHERE url = ?1", nativeQuery= true)
    int getDuplicates(String title);

    @Query(value = "SELECT * FROM articles WHERE keyword = ?1 AND is_active = 1", nativeQuery = true)
    Article[] getAllArticlesWithKeyword(String keyword);
}
