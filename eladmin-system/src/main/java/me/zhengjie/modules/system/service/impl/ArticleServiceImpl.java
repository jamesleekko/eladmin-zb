package me.zhengjie.modules.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.repository.ArticleRepository;
import me.zhengjie.modules.system.service.ArticleService;
import me.zhengjie.modules.system.service.dto.ArticleDto;
import me.zhengjie.modules.system.service.dto.ArticleQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.ArticleMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public PageResult<ArticleDto> queryAll(ArticleQueryCriteria criteria, Pageable pageable) {
        Page<Article> page = articleRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),
                pageable);
        return PageUtil.toPage(page.map(articleMapper::toDto).getContent(), page.getTotalElements());
    }

    @Override
    public List<ArticleDto> queryAll(ArticleQueryCriteria criteria) {
        List<Article> list = articleRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return articleMapper.toDto(list);
    }

    @Override
    public ArticleDto findById(Long id) {
        Article article = articleRepository.findById(id).orElseGet(Article::new);
        ValidationUtil.isNull(article.getId(), "Article", "id", id);
        return articleMapper.toDto(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Article resources) {
        articleRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Article resources) {
        Article article = articleRepository.findById(resources.getId()).orElseGet(Article::new);
        ValidationUtil.isNull(article.getId(), "Article", "id", resources.getId());
        article.setTitle(resources.getTitle());
        article.setType(resources.getType());
        article.setArticleKindName(resources.getArticleKindName());
        article.setFileName(resources.getFileName());
        article.setFilePath(resources.getFilePath());
        article.setContent(resources.getContent());
        article.setDeptId(resources.getDeptId());
        articleRepository.save(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        articleRepository.deleteAllByIdIn(ids);
    }

    @Override
    public void download(List<ArticleDto> articleDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ArticleDto dto : articleDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文章标题", dto.getTitle());
            map.put("文章类型", dto.getType());
            map.put("文件类型", dto.getArticleKindName());
            map.put("文件名", dto.getFileName());
            map.put("创建日期", dto.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
