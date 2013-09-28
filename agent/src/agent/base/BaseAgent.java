package agent.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import agent.orderSerializable.Order;
import agent.orderSerializable.Trade;
import agent.util.DataBase;
import agent.util.NumberUtil;

public abstract class BaseAgent {
	
	private String marketUrl;
	private String registertUrl;
	
	private int agentId = -1;
	private double initCash;
	private int initHold;
	private double availableCash;
	private double lockedCash;
	private int availableHold;
	private int lockedHold;
	
	protected DataBase db;
	
	public BaseAgent(double initCash, int initHold, double availableCash, double lockedCash, int availableHold, int lockedHold ){
		db = new DataBase();
		this.initCash = initCash;
		this.initHold = initHold;
		this.availableCash = availableCash;
		this.lockedCash = lockedCash;
		this.availableHold = availableHold;
		this.lockedHold = lockedHold;
	}
	
	public void simulate( String marketUrl, String registerUrl){
		this.marketUrl = marketUrl;
		this.registertUrl = registerUrl;
		if( regist() ){
			agentRunningLogic();
		}
	}
	
	/**
	 * @see the running process of agents
	 */
	public abstract void agentRunningLogic();
	
	public String getMessageFromMarket( String tag ){
		String requestUrl = marketUrl+tag;
		
		String result = "";
		String lines = "";
		try {
			URL url = new URL(requestUrl);
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
			
			int responseCode = httpUrlConnection.getResponseCode();
			
			if( responseCode == HttpURLConnection.HTTP_OK ){
				
				BufferedReader reader = new BufferedReader( new InputStreamReader( httpUrlConnection.getInputStream() ) );
				
				while ( (lines = reader.readLine()) != null ) {
					result = result + lines;
				}
				reader.close();
			}
			httpUrlConnection.disconnect();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return result;
	}
	
	
	
	/**
	 * @see register to StockMarket to get a unique agent Id
	 * @param Id: the Id to set
	 */
	private boolean regist() {
		if ( this.agentId < 0) {
			try {
				URL url = new URL(registertUrl);
				HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
				
				String lines="";
				String result = "";
				
				//一直尝试连接
				for( int i = 0; i < 5 && httpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK ; i++ ){
					httpUrlConnection.disconnect();
					httpUrlConnection = (HttpURLConnection) url.openConnection();
				}
				
				if( httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK ){
					BufferedReader reader = new BufferedReader( new InputStreamReader( httpUrlConnection.getInputStream() ) );
					while ( (lines = reader.readLine()) != null ) {
						result = result + lines;
					}
					
					httpUrlConnection.disconnect();
					
					if( null != result && NumberUtil.isInteger( result.trim() ) ){
						this.agentId = Integer.parseInt( result.trim() );
						return true;
					}
				}
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		else
			return true;
		
		return false;
	}
	
	/**
	 * @see submit an order to the market
	 * @param or: the order to be submitted
	 * @return: the message returned from market
	 */
	public String submitOrder(Order or){
		if( null != or ){
			or.setCreateTime(System.currentTimeMillis());
			int orderId = db.insertOrder(or);
			or.setId(orderId);
			return getMessageFromMarket( "200" + or.convertSelfToString() );
		}
		else
			return "";
	}
	
	/**
	 * @see resove the Message got from the market
	 * @param marketMessage: the Message got from the market
	 * @return : the trade result of the market
	 */
	public List<Trade> resoveMessageForTradeList( String marketMessage ){
		
		List<Trade> tradeList = new ArrayList<Trade>();
		if ( null != marketMessage && !"".equals( marketMessage.trim() ) && !marketMessage.substring(0, 2).equals("NO") ) {
			
			int num = marketMessage.length() / 42;
			
			for (int j = 0; j < num; j++) {
				Trade tempTra = new Trade();
				tempTra.setStringForSelf( marketMessage.substring(j * 42, (j + 1) * 42) );
				tradeList.add(tempTra);
			}
		}
		return tradeList;
	}
	
	public double getStockPrice(){
		String stockPriceStr = getMessageFromMarket("000");
		if( null != stockPriceStr && !"".equalsIgnoreCase(stockPriceStr.trim()) && NumberUtil.isDouble(stockPriceStr) ){
			return Double.parseDouble(stockPriceStr);
		}
		return 0;
	}
	
	/**
	 * @see 
	 * @return
	 */
	public String refreshMessage() {
		return getMessageFromMarket("002");
	}
	
	/**
	 * @see agent deciside for an order
	 * @return
	 */
	public abstract Order decision();
	
	public String getMarketUrl() {
		return marketUrl;
	}

	public void setMarketUrl(String marketUrl) {
		this.marketUrl = marketUrl;
	}

	public String getRegistertUrl() {
		return registertUrl;
	}

	public void setRegistertUrl(String registertUrl) {
		this.registertUrl = registertUrl;
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}

	public double getAvailableCash() {
		return availableCash;
	}

	public void setAvailableCash(double availableCash) {
		this.availableCash = availableCash;
	}

	public double getLockedCash() {
		return lockedCash;
	}

	public void setLockedCash(double lockedCash) {
		this.lockedCash = lockedCash;
	}

	public int getAvailableHold() {
		return availableHold;
	}

	public void setAvailableHold(int availableHold) {
		this.availableHold = availableHold;
	}

	public int getLockedHold() {
		return lockedHold;
	}

	public void setLockedHold(int lockedHold) {
		this.lockedHold = lockedHold;
	}
	
}
