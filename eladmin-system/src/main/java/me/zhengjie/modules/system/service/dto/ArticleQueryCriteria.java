package me.zhengjie.modules.system.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ArticleQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String title;

    @Query
    private Integer type;

    @Query
    private String articleKindName;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
