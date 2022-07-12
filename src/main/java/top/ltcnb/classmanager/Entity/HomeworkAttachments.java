package top.ltcnb.classmanager.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class HomeworkAttachments {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long posterId;
    private Long HomeworkId;
    private String originName;
    private String currentName;
}
