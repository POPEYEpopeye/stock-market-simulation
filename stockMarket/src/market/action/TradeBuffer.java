package market.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import market.orderSerializable.Trade;

public class TradeBuffer {
	//每次插入都插在最开始的位置
    private static List<TradeWithTag> buffer = new LinkedList<TradeWithTag>();
    
    public void addTradeList( List<Trade> traList ){
    	if( null != traList ){
    		for( int i = 0; i < traList.size(); i++ ){
    			addTrade( traList.get(i) );
    		}
    	}
    }
    
    public void addTrade(Trade trade){
    	if( trade != null ){
    		TradeWithTag trawg = new TradeWithTag(trade);
    		synchronized (buffer) {
    			buffer.add(0, trawg);
			}
    	}
    }
    
    public List<Trade> getTradeList(int agentId){
        List<Trade> traList = new ArrayList<Trade>();
        
        synchronized (buffer) {
        	for( int i = 0; i < buffer.size(); i++ ){
            	if( null != buffer.get(i) && null != buffer.get(i).getTrade() ){
            		TradeWithTag trawg = buffer.get(i);
                    if( agentId == trawg.getTrade().getBuyId() && trawg.getBuyAgentTag() != 0 ){
                    	trawg.minusBuyAgentTag();
                        traList.add( buffer.get(i).getTrade() );
                    }
                    else if( agentId == trawg.getTrade().getSellId() && trawg.getSellAgentTag() != 0 ){
                    	trawg.minusSellAgentTag();
                    	traList.add( buffer.get(i).getTrade() );
                    }
                    if( trawg.getBuyAgentTag() == 0 && trawg.getSellAgentTag() == 0 ){
                		buffer.remove(i);
                		i--;
                	}
            	}
            	else{
            		buffer.remove(i);
                	i--;
            	}
            }
		}
        
        return traList;
    }
    
    public synchronized int getSize(){
        return buffer.size();
    }
    
    public boolean printBuffer(){
    	synchronized (buffer) {
    		for(int i = 0; i < buffer.size(); i++){
            	TradeWithTag trawg = buffer.get(i);
            	if( null != trawg && null != trawg.getTrade() )
            		System.out.println( trawg.getTrade().convertSelfToString() );
            }
		}
        return true;
    }
    
    public class TradeWithTag{
    	private Trade trade;
    	private int buyAgentTag;
    	private int sellAgentTag;
    	
    	public TradeWithTag( Trade trade ) {
    		this.trade = trade;
    		buyAgentTag = 1;
    		sellAgentTag = 1;
    	}
    	
		public Trade getTrade() {
			return trade;
		}
		
		public void minusBuyAgentTag(){
			this.buyAgentTag = 0;
		}
		public int getBuyAgentTag(){
			return this.buyAgentTag;
		}
		public void minusSellAgentTag() {
			this.sellAgentTag = 0;
		}
		public int getSellAgentTag(){
			return this.sellAgentTag;
		}
    }
}