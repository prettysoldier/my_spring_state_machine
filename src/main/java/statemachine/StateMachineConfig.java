package statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

/**
 * 状态机的配置类：配置初始状态，状态转移表，监听器
 * @author hsj
 * @create 2019/11/18
 */
@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
        config.withConfiguration().autoStartup(true).listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderState.WAIT_SUBMIT)
                .end(OrderState.COMPLETED)
                .state(OrderState.WAIT_RECEIVING, null, new Action<OrderState, OrderEvent>() {
                    @Override
                    public void execute(StateContext<OrderState, OrderEvent> context) {
                        System.out.println("离开待接单");
                    }
                })
                .state(OrderState.CANCELED, cancelAction(), cancelAction())
                .end(OrderState.CANCELED)
                .end(OrderState.CLOSED)
                .states(EnumSet.allOf(OrderState.class))

                    .and()
                    .withStates()
                    .parent(OrderState.WAIT_SUBMIT)
                    .initial(OrderState.WAIT_RECEIVING)
                    .state(OrderState.WAIT_RECEIVING);
    }

    /**
     * 配置迁移表
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(OrderState.WAIT_SUBMIT).target(OrderState.WAIT_RECEIVING).event(OrderEvent.INIT_STATE)

                .and()
                .withExternal()
                .source(OrderState.WAIT_SUBMIT).target(OrderState.CANCELED).event(OrderEvent.CANCEL)
                .guard(guard())
                .action(cancelAction())

                .and()
                .withExternal()
                .source(OrderState.WAIT_RECEIVING).target(OrderState.WAIT_SCHEDULING).event(OrderEvent.RECEIVE_ORDER)
                .guardExpression("true")

                .and()
                .withExternal()
                .source(OrderState.WAIT_SCHEDULING).target(OrderState.PROCESSING).event(OrderEvent.SCHEDULE);

    }

    @Bean
    public Guard<OrderState, OrderEvent> guard() {
        return new Guard<OrderState, OrderEvent>() {

            @Override
            public boolean evaluate(StateContext<OrderState, OrderEvent> context) {

                return true;
            }
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> cancelAction() {
        return new Action<OrderState, OrderEvent>() {

            @Override
            public void execute(StateContext<OrderState, OrderEvent> context) {
                System.out.println("删除订单");
            }
        };
    }

    @Bean
    public StateMachineListener<OrderState, OrderEvent> listener() {
        return new StateMachineListenerAdapter<OrderState, OrderEvent>() {
            @Override
            public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
                if(from == null){
                    System.out.println("初始化状态：【" + to.getId().getDesc() + "】");
                    return;
                }
                System.out.println("状态从【" + from.getId().getDesc() + "】变成【" + to.getId().getDesc() + "】");
            }
        };
    }
}
