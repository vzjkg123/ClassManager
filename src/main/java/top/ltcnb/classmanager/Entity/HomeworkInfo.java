package top.ltcnb.classmanager.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class HomeworkInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long posterId;
    private Long classId;
    private String Title;
    private String desc;
    private List<String> attachments;
}
