package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Article;
import me.zhengjie.modules.system.domain.ArticleKind;
import me.zhengjie.modules.system.domain.data.NewsCategory;
import me.zhengjie.modules.system.repository.ArticleKindRepository;
import me.zhengjie.modules.system.repository.NewsCategoryRepository;
import me.zhengjie.modules.system.service.ArticleService;
import me.zhengjie.modules.system.service.dto.ArticleDto;
import me.zhengjie.modules.system.service.dto.ArticleQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：文章管理")
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleKindRepository articleKindRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private static final String ENTITY_NAME = "article";

    @ApiOperation("获取文章类型列表")
    @GetMapping(value = "/types")
    @PreAuthorize("@el.check('article:list')")
    public ResponseEntity<List<NewsCategory>> getArticleTypes() {
        return new ResponseEntity<>(newsCategoryRepository.findAll(), HttpStatus.OK);
    }

    @ApiOperation("获取文件类型列表")
    @GetMapping(value = "/kinds")
    @PreAuthorize("@el.check('article:list')")
    public ResponseEntity<List<ArticleKind>> getArticleKinds() {
        return new ResponseEntity<>(articleKindRepository.findAll(), HttpStatus.OK);
    }

    @ApiOperation("导出文章数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('article:list')")
    public void exportArticle(HttpServletResponse response, ArticleQueryCriteria criteria) throws IOException {
        articleService.download(articleService.queryAll(criteria), response);
    }

    @ApiOperation("查询文章")
    @GetMapping
    @PreAuthorize("@el.check('article:list')")
    public ResponseEntity<PageResult<ArticleDto>> queryArticle(ArticleQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(articleService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增文章")
    @ApiOperation("新增文章")
    @PostMapping
    @PreAuthorize("@el.check('article:add')")
    public ResponseEntity<Object> createArticle(@Validated @RequestBody Article resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        articleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改文章")
    @ApiOperation("修改文章")
    @PutMapping
    @PreAuthorize("@el.check('article:edit')")
    public ResponseEntity<Object> updateArticle(@Validated(Article.Update.class) @RequestBody Article resources) {
        articleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除文章")
    @ApiOperation("删除文章")
    @DeleteMapping
    @PreAuthorize("@el.check('article:del')")
    public ResponseEntity<Object> deleteArticle(@RequestBody Set<Long> ids) {
        articleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
