package top.ltcnb.classmanager.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.ltcnb.classmanager.Entity.Submit;

import java.util.List;

@Mapper
public interface SubmitMapper extends BaseMapper<Submit> {
    @Select("select * from submit where time = (select MAX(time) from submit where user_id = ${userId} AND homework_id = ${homeworkId})")
    Submit getLastHomeworkInfo(Long userId, Long homeworkId);

    @Select("select * from submit, (select user_id as id, MAX(time) as t from (select * from submit where homework_id = ${homeworkId} AND user_id = (select user_id from user_class where class_id = ${classId})) as a group by user_id) as b where submit.user_id = id AND submit.time = t")
    List<Submit> getAllHomeworkName(Long homeworkId, Long classId);


}
