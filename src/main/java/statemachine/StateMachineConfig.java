package statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.state.StateMachineState;

import java.util.EnumSet;

/**
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
        states.withStates().initial(OrderState.WAIT_SUBMIT).states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(OrderState.WAIT_SUBMIT).target(OrderState.WAIT_RECEIVING).event(OrderEvent.INIT_STATE)
                .and()
                .withExternal()
                .source(OrderState.WAIT_RECEIVING).target(OrderState.WAIT_SCHEDULING).event(OrderEvent.RECEIVE_ORDER)
                .and()
                .withExternal()
                .source(OrderState.WAIT_SCHEDULING).target(OrderState.PROCESSING).event(OrderEvent.SCHEDULE);
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
