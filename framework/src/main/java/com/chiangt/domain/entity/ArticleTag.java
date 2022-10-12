package com.chiangt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2022-09-30 17:06:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_article_tag")
public class ArticleTag {
    //文章id
    private Long articleId;
    //标签id
    private Long tagId;
}

