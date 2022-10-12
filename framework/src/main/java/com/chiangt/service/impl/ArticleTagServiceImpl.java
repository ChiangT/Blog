package com.chiangt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chiangt.domain.entity.ArticleTag;
import com.chiangt.mapper.ArticleTagMapper;
import com.chiangt.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-10 16:08:40
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

