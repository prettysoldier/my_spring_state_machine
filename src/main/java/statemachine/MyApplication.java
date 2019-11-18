package statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

import javax.annotation.Resource;

/**
 * @author hsj
 * @create 2019/11/18
 */
@SpringBootApplication
public class MyApplication implements CommandLineRunner {

    @Resource
    private StateMachine<OrderState, OrderEvent> stateMachine;

    public static void main(String[] args) {

        SpringApplication.run(MyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        stateMachine.sendEvent(OrderEvent.INIT_STATE);
        stateMachine.sendEvent(OrderEvent.RECEIVE_ORDER);
        stateMachine.sendEvent(OrderEvent.SCHEDULE);
    }
}
