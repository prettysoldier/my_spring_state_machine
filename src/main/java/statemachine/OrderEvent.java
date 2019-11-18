package statemachine;


/**
 * @author hsj
 * @create 2019/11/18
 */
public enum OrderEvent {
    INIT_STATE("INIT_STATE", "订单状态初始化"),
    RECEIVE_ORDER("RECEIVE_ORDER", "客服接单"),
    SCHEDULE("SCHEDULE", "调度接单"),
    COMPLETE("COMPLETE","完成订单"),
    CANCEL("CANCEL","取消订单"),
    CLOSE("CLOSE","关闭订单"),
    ;

    private String key;
    private String desc;

    OrderEvent(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
