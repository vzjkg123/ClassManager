package top.ltcnb.classmanager.Enum;

import lombok.Getter;

@Getter
public enum Status {
    ERROR(1, "");
    final Integer code;
    final String desc;

    Status(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
