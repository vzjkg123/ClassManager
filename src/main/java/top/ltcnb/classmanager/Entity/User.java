package top.ltcnb.classmanager.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {
    @TableId
    private Integer id;
    private String name;
    private String account;
    private String password;
    private String tel;
}
