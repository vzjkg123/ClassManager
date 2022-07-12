package top.ltcnb.classmanager.Service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import top.ltcnb.classmanager.Dao.ArticleMapper;
import top.ltcnb.classmanager.Entity.Article;
import top.ltcnb.classmanager.Service.IArticleService;

import java.util.List;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
