package top.codecrab.vueblog.common.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int code;
    private String message;
    private Object data;

    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    public static Result fail(String message) {
        return new Result(400, message, null);
    }

    public static Result fail(String message, Object data) {
        return new Result(400, message, data);
    }
}
