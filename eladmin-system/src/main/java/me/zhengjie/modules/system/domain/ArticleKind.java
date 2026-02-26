package me.zhengjie.modules.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "sys_article_kind")
public class ArticleKind implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID", hidden = true)
    private Integer id;

    @ApiModelProperty(value = "类型编码")
    private Integer kind;

    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "display_name")
    @ApiModelProperty(value = "显示名称")
    private String displayName;
}
