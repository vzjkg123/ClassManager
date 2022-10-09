package top.ltcnb.classmanager.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class HomeworkInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long posterId;
    private Long classId;
    private String title;
    private String description;

    //作业文档存放地址
    private String infoLocation;
}
