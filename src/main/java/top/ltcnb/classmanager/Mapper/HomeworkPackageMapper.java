package top.ltcnb.classmanager.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.ltcnb.classmanager.Entity.HomeworkPackage;

import java.util.List;

@Mapper
public interface HomeworkPackageMapper extends BaseMapper<HomeworkPackage> {
    @Select("select * from homework_package where current_timestamp>date_add(time,interval 1 day)")
    List<HomeworkPackage> getExpirePackage();
}
