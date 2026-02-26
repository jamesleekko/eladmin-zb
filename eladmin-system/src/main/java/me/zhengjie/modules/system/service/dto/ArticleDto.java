package me.zhengjie.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import java.io.Serializable;

@Getter
@Setter
public class ArticleDto extends BaseDTO implements Serializable {

    private Long id;

    private String title;

    private Integer type;

    private String articleKindName;

    private String fileName;

    private String filePath;

    private String content;

    private Long deptId;
}
