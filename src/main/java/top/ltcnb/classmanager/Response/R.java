package top.ltcnb.classmanager.Response;

import lombok.Data;

@Data
public class R<T> {
    private final Integer status;
    private String msg;
    private T data;

    private R(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private R(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private R(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> R<T> success(String message) {
        return new R<>(1, message);
    }

    public static <T> R<T> success(T data) {
        return new R<>(1, data);
    }

    public static <T> R<T> failed(String message) {
        return new R<>(0, message);
    }

    public static <T> R<T> failed(String message, T data) {
        return new R<>(1, message, data);
    }

    public static <T> R<T> failed(T data) {
        return new R<>(0, data);
    }
}
