package top.ltcnb.classmanager.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ltcnb.classmanager.Entity.User;
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
