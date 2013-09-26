package market.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import market.dao.DataBase;
import market.orderSerializable.Order;
import market.orderSerializable.Trade;

public class MarketDataServlet extends HttpServlet {
	
	public static Long requestTime = 0L;
	public final static int SIZE = 80;
	private DataBase db;
	
	/**
	 * Constructor of the object.
	 */
	public MarketDataServlet() {
		super();
	}
	
	/**
	 * Initialization of the servlet. <br>
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		db = DataBase.getInstance();
		requestTime = 0L;
	}
	
	/**
	 * The doGet method of the servlet. <br>
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String requestStr = request.getQueryString();
		PrintWriter out = response.getWriter();
	
		out.println( getPriceVolume() + ":" + getPrices() + ":" + MarketDataServlet.requestTime );
		
		out.flush();
		out.close();
	}
	
	//return the price, volume, time of the latest trade
	private String getPriceVolume(){
		Trade latestOrder = db.getLatestTradeOrder();
		StringBuilder price_volume = new StringBuilder();
		if(latestOrder != null)
			price_volume.append( latestOrder.getTradePrice() + ";" + latestOrder.getTradeVolume() + ";" + latestOrder.getTradeTime() );
		return price_volume.toString();
	}
	
	//return the top 5 buy prices and low 5 sell prices
	private String getPrices(){
		DecimalFormat df = new DecimalFormat("#.00");
		
		StringBuilder priceStr = new StringBuilder();
		synchronized ( Market.tradeStock.getBuyOrderQueue() ) {
			List<Order> buyOrderQueue = Market.tradeStock.getBuyOrderQueue();
			int i = 0;
			if( buyOrderQueue.size() > 0 ){
				double price = buyOrderQueue.get(i).getPrice();
				int volume = buyOrderQueue.get(i).getVolume();
				i++;
				while( i < buyOrderQueue.size() ){
					if( buyOrderQueue.get(i).getPrice() - price < 0.01 && buyOrderQueue.get(i).getPrice() - price > -0.01  ){
						volume += buyOrderQueue.get(i).getVolume();
						i++;
					}
					else
						break;
				}
				priceStr.append( df.format( price ) +","+volume );
			}
			for( int j = 0 ; j < 4 && j < buyOrderQueue.size() && i < buyOrderQueue.size(); j++ ){
				double price = buyOrderQueue.get(i).getPrice();
				int volume = buyOrderQueue.get(i).getVolume();
				i++;
				while( i < buyOrderQueue.size() ){
					if( buyOrderQueue.get(i).getPrice() - price < 0.01 && buyOrderQueue.get(i).getPrice() - price > -0.01  ){
						volume += buyOrderQueue.get(i).getVolume();
						i++;
					}
					else
						break;
				}
				priceStr.append( "&"+ df.format( price ) +","+volume );
			}
		}
		priceStr.append("#");
		
		synchronized ( Market.tradeStock.getSellOrderQueue() ) {
			List<Order> sellOrderQueue = Market.tradeStock.getSellOrderQueue();
			int i = 0;
			if( sellOrderQueue.size() > 0 ){
				double price = sellOrderQueue.get(i).getPrice();
				int volume = sellOrderQueue.get(i).getVolume();
				i++;
				while( i < sellOrderQueue.size() ){
					if( sellOrderQueue.get(i).getPrice() - price < 0.01 && sellOrderQueue.get(i).getPrice() - price > -0.01  ){
						volume += sellOrderQueue.get(i).getVolume();
						i++;
					}
					else
						break;
				}
				priceStr.append( df.format( price ) +","+volume );
			}
			for( int j = 0 ; j < 4 && j < sellOrderQueue.size() && i < sellOrderQueue.size(); j++ ){
				double price = sellOrderQueue.get(i).getPrice();
				int volume = sellOrderQueue.get(i).getVolume();
				i++;
				while( i < sellOrderQueue.size() ){
					if( sellOrderQueue.get(i).getPrice() - price < 0.01 && sellOrderQueue.get(i).getPrice() - price > -0.01  ){
						volume += sellOrderQueue.get(i).getVolume();
						i++;
					}
					else
						break;
				}
				priceStr.append( "&"+ df.format( price ) +","+volume );
			}
		}
		return priceStr.toString();
	}
	
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}
}
