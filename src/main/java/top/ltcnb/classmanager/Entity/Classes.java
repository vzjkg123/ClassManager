package top.ltcnb.classmanager.Entity;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Classes {
    @TableId
    private Long id;
    private String name;
    private String description;
}
