package top.ltcnb.classmanager.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permissions {
    @TableId
    private Long userId;
    private Byte createClass;
    private Byte postArticle;
    private Byte systemAdminister;
}
