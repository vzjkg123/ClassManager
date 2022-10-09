package top.ltcnb.classmanager.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.ltcnb.classmanager.Entity.Classes;

import java.util.List;


@Mapper
public interface ClassesMapper extends BaseMapper<Classes> {
    @Select("select * from user_class,classes WHERE user_class.user_id = ${id} AND user_class.class_id = user_class.class_id;\n")
    List<Classes> listMyClass(Long id);
}
