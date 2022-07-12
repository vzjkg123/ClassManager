package top.ltcnb.classmanager.Dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ltcnb.classmanager.Entity.Article;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
