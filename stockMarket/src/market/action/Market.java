package market.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import market.dao.DataBase;
import market.orderSerializable.Order;
import market.orderSerializable.Trade;

public class Market extends HttpServlet {
	
	public static Stock tradeStock = new Stock();
	private TradeBuffer tradeBuffer ;
	
	public Market() {
		super();
	}
	
	public void init() throws ServletException {
		super.init();
		
		tradeBuffer = new TradeBuffer();
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		super.doGet(request, response);
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
        String receMsg = request.getQueryString();
        String msgType = receMsg.substring(0, 3);
        
        //000  返回股票的实时价格
        if ("000".equals(msgType)) {
        	DecimalFormat df = new DecimalFormat("#.00");
        	out.write( df.format( getStockPrice() ) );
        }
        //100 刷新消息
        else if ("100".equals(msgType)) {
        	MarketDataServlet.requestTime ++;
            String msgBody = receMsg.substring(3);
            out.write(handleRefresh(msgBody));
        }
        //200  提交订单
        else if ( "200".equals(msgType) ) {
        	MarketDataServlet.requestTime ++;
            String msgBody = receMsg.substring(3);
            out.write(handleSubmit(msgBody));
        }
        else if( "300".equals(msgType) ){
        	out.write( Register.getAgentId() );
        }
        else{
        	String result = customResponse(receMsg);
        	if( null != result && !"".equalsIgnoreCase(result) )
        		out.write(result);
        	else
        		out.write("NO");
        }
        
        out.flush();
        out.close();
        
	}
	
	// return the latest stock price
	private double getStockPrice(){
		return tradeStock.getStockPrice();
	}
	
	//handle refresh request
	private String handleRefresh(String msgBody){
		int agentId = Integer.valueOf(msgBody);
        List<Trade> traList = new ArrayList<Trade>();
        synchronized (tradeBuffer) {
            traList = tradeBuffer.getTradeList(agentId);
        }
        
        if (!traList.isEmpty()) {
            String traOrders = "";
            for (int i = 0; i < traList.size(); i++) {
                traOrders += traList.get(i).convertSelfToString();
            }
            return traOrders;
        } else {
            return "NO";
        }
	}
	
	//handle the submit
	private String handleSubmit(String msgBody){
		List<Trade> traList = new ArrayList<Trade>();
        
        Order or = new Order();
        or.setStringForSelf(msgBody);
        int agentId = or.getAgentId();
        
        //insert order into trade Area and trading
        synchronized ( Market.tradeStock ) {
            if ( 0 == Market.tradeStock.insertIntoQueue(or) ) {
            	traList = Market.tradeStock.auction();
            }
        }
        
        //trading is success
        if ( !traList.isEmpty() ) {
            //insert trade result into database( Mysql )
            DataBase db = DataBase.getInstance();
            for (int i = 0; i < traList.size(); i++) {
            	Trade order = traList.get(i);
                db.insertTradeOrder(order);
            }
            
            synchronized (tradeBuffer) {
            	tradeBuffer.addTradeList( traList );
                traList = tradeBuffer.getTradeList(agentId);
            }
            
            String traOrders = "";
            for (int i = 0; i < traList.size(); i++) {
                traOrders += traList.get(i).convertSelfToString();
            }
            return traOrders;
            
            //and insert trade result into tradeOrder buffer
        } else {
            return "NO";
        }
	}
	
	/**
	 *  @see 自定义响应函数
	 * @param receMsg 从Agent接收到的参数
	 * @return	返回相应字符串
	 */
	public String customResponse(String receMsg){
		return "NO";
	}
	
	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}
}
