package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Set;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    Article findByTitle(String title);

    void deleteAllByIdIn(Set<Long> ids);
}
