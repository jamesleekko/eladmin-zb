package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.ArticleKind;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleKindRepository extends JpaRepository<ArticleKind, Integer> {
}
