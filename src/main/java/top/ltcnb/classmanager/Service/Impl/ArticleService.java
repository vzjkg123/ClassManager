package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Mapper.ArticleMapper;
import top.ltcnb.classmanager.Entity.Article;
import top.ltcnb.classmanager.Service.IArticleService;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
