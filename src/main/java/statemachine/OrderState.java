package statemachine;

/**
 * @author hsj
 * @create 2019/11/18
 */
public enum OrderState {

    WAIT_SUBMIT(0,"WAIT_SUBMIT","待提交(草稿状态)"),
    WAIT_RECEIVING(5,"WAIT_RECEIVING","待接单"),
    WAIT_SCHEDULING(10,"WAIT_SCHEDULING","待调度"),
    PROCESSING(15,"PROCESSING","进行中"),
    COMPLETED(20,"COMPLETED","已完成"),
    CLOSED(99,"CLOSED","已关闭"),
    CANCELED(100,"CANCELED","已取消"),
            ;

    private Integer code;
    private String key;
    private String desc;

    OrderState(Integer code, String key, String desc) {
        this.code = code;
        this.key = key;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
