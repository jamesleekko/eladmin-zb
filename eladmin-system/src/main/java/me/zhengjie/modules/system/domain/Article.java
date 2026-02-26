package me.zhengjie.modules.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "sys_article_list")
public class Article extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "文章标题")
    private String title;

    @NotNull
    @Column(name = "type")
    @ApiModelProperty(value = "文章类型")
    private Integer type;

    @Column(name = "article_kind_name")
    @ApiModelProperty(value = "文件类型")
    private String articleKindName;

    @Column(name = "file_name")
    @ApiModelProperty(value = "文件名")
    private String fileName;

    @Column(name = "file_path")
    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @Column(name = "dept_id")
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
