package agent.start;

import java.util.List;
import java.util.Random;

import agent.base.BaseAgent;
import agent.orderSerializable.Order;
import agent.orderSerializable.Trade;
import agent.util.NumberUtil;


public class AgentDemo extends BaseAgent {
	
	private double stockPrice;
	
	public AgentDemo(double initCash, int initHold, double availableCash, double lockedCash, int availableHold, int lockedHold ){
		super(initCash, initHold, availableCash, lockedCash, availableHold, lockedHold);
	}
	//************************************//
	//must implement a default constructor 
	public AgentDemo(){
		super(1000, 0, 1000, 0, 0, 0);
	}
	
	@Override
	public void agentRunningLogic(){
		Order or = decision();
		String msgFromMarket = "";
		if( null != or ){
			msgFromMarket = submitOrder(or);
		}
		else{
			msgFromMarket = refreshMessage();
		}
		List<Trade> tradeList = resoveMessageForTradeList(msgFromMarket);
	}
	
	@Override
	public Order decision() {
		Order or = new Order();
		
		stockPrice = getStockPrice();
		if( Math.random() < 0.3 )
			return null;
		else{
			Random random = new Random();
			or.setPrice( random.nextInt(3)+9.0 );
	        or.setVolume(random.nextInt(20) + 1);	//1-20	
	        or.setAgentId( this.getAgentId() );
	        or.setDirection( random.nextInt(2) );
	        
	        consumingTime();
	        
	        return or;
		}
			
	}
	
	private int getAgentCount(){
		int agentCount = -1;
		
		String countStr = getMessageFromMarket("300");
		if( null != countStr && !"".equals( countStr.trim() ) && NumberUtil.isNumber( countStr ) )
			agentCount = Integer.parseInt(countStr);
		return agentCount;
	}
	
	private void consumingTime(){
		double N = 10000000.0;
		double sum = 0.0;
		for(int i = 0; i < N; i++){
			double temp = Math.pow((i + 0.5) / (double)N, 2);
			double temp1 = 4 / (1 + temp);
			sum = sum + temp1;			
		}
		double result = sum / N;
	}
	
}
