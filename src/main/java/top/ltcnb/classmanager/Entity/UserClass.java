package top.ltcnb.classmanager.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserClass {
    @TableId
    private Long userId;
    private Long classId;
    private Long isAdmin;
}
