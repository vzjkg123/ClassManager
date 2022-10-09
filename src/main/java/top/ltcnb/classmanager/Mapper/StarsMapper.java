package top.ltcnb.classmanager.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.ltcnb.classmanager.Entity.Article;
import top.ltcnb.classmanager.Entity.Stars;

import java.util.List;

@Mapper
public interface StarsMapper extends BaseMapper<Stars> {
    @Select("select * from article as a, stars as s where a.file_name=s.article_name and s.star_user = ${id}")
    List<Article> getStarList(String id);
}
