package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.service.dto.ArticleDto;
import me.zhengjie.modules.system.service.dto.ArticleQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ArticleService {

    ArticleDto findById(Long id);

    void create(Article resources);

    void update(Article resources);

    void delete(Set<Long> ids);

    PageResult<ArticleDto> queryAll(ArticleQueryCriteria criteria, Pageable pageable);

    List<ArticleDto> queryAll(ArticleQueryCriteria criteria);

    void download(List<ArticleDto> queryAll, HttpServletResponse response) throws IOException;
}
