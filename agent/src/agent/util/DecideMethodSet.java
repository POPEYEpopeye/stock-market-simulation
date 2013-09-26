package agent.util;

import java.util.Random;
import agent.base.BaseAgent;
import agent.orderSerializable.Order;

public class DecideMethodSet {

    /**
     * Random decide order method
     *
     * @param att,attribute of Agent
     * @param tra,the latest trade Order
     * @return Order,decide result.
     */
    public static Order randomDecide(BaseAgent agent, double stockPrice) {
        Order or = new Order();
        Random random = new Random();
        
        or.setPrice( random.nextInt(3)+9.0 );
        or.setVolume(random.nextInt(20) + 1);	//1-20	
        or.setAgentId( agent.getAgentId() );
        or.setDirection( random.nextInt(2) );
        
        return or;
    }
}
