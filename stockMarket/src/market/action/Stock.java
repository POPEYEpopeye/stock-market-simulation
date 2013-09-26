package market.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import market.orderSerializable.Order;
import market.orderSerializable.Trade;

public class Stock {
	private String stockId;
	private List<Order> buyOrderQueue = new LinkedList<Order>();
	private List<Order> sellOrderQueue = new LinkedList<Order>();
	private Double stockPrice = 1D ;
	
	/**
	 * @param or, the order will insert into buy queue
	 * @return i, the index order or insert.
	 */
	public int insertIntoBuyOrderQueue(Order or) {
		int i = 0;
		synchronized (buyOrderQueue) {
			if ( 0 == buyOrderQueue.size() ) {
				buyOrderQueue.add(0, or);
			} else {
				for (i = 0; i < buyOrderQueue.size(); i++) {
					if (or.getPrice() >= buyOrderQueue.get(i).getPrice()) {
						buyOrderQueue.add(i, or);
						break;
					}
				}
				if (i == buyOrderQueue.size()) {
					buyOrderQueue.add(i, or);
				}
			}
		}
		return i;
	}
	
	/**
	 * 
	 * @param or, the order will insert into sell queue
	 * @return i, the index order or insert.
	 */
	public int insertIntoSellOrderQueue(Order or) {
		int i = 0;
		synchronized (sellOrderQueue) {
			if (0 == sellOrderQueue.size()) {
				sellOrderQueue.add(0, or);
			} else {
				for (i = 0; i < sellOrderQueue.size(); i++) {
					if (or.getPrice() <= sellOrderQueue.get(i).getPrice()) {
						sellOrderQueue.add(i, or);
						break;
					}
				}
				if (i == sellOrderQueue.size()) {
					sellOrderQueue.add(i, or);
				}
			}
		}
		return i;
	}

	/**
	 * @param or, the order will insert into trade queue
	 * @return the index order or insert. 0 means should call method
	 *         tradeProcess to match orders else means no need to match orders
	 */
	public int insertIntoQueue(Order or) {
		if (0 == or.getDirection()) {
			return insertIntoSellOrderQueue(or);
		} else {
			return insertIntoBuyOrderQueue(or);
		}
	}
	
	public List<Trade> auction() {
		synchronized (buyOrderQueue) {
			synchronized (sellOrderQueue) {
				return auctionOrderList();
			}
		}
	}
	
	/**
	 * @return ArrayList<Trade>, include already match orders.
	 */
	private List<Trade> auctionOrderList() {
		List<Trade> tra = new ArrayList<Trade>();
		while ( buyOrderQueue.size() != 0 && sellOrderQueue.size() != 0 && buyOrderQueue.get(0).getPrice() >= sellOrderQueue.get(0).getPrice() ) 
		{
			Order buyOrder = buyOrderQueue.get(0);
			Order sellOrder = sellOrderQueue.get(0);
			Trade tempTrade = new Trade();
			
			// set the price of tradeOrder
			if ( buyOrder.getCreateTime() <= sellOrder.getCreateTime() ) {
				tempTrade.setTradePrice(buyOrder.getPrice());
				synchronized (stockPrice) {
					stockPrice = buyOrder.getPrice();
				}
			} else {
				tempTrade.setTradePrice(sellOrder.getPrice());
				synchronized (stockPrice) {
					stockPrice = sellOrder.getPrice();
				}
			}
			// set the Ids of tradeOrder
			tempTrade.setBuyId(buyOrder.getAgentId());
			tempTrade.setBuyOrderId(buyOrder.getOrderId());
			tempTrade.setSellId(sellOrder.getAgentId());
			tempTrade.setSellOrderId(sellOrder.getOrderId());

			tempTrade.setTradeTime(System.currentTimeMillis());
			// set the volume of tradeOrder

			if (buyOrder.getVolume() == sellOrder.getVolume()) {
				tempTrade.setTradeVolume(buyOrder.getVolume());
				tra.add(tempTrade);
				sellOrderQueue.remove(0);
				buyOrderQueue.remove(0);
			} else if (buyOrder.getVolume() > sellOrder.getVolume()) {
				tempTrade.setTradeVolume(sellOrder.getVolume());
				tra.add(tempTrade);
				sellOrderQueue.remove(0);
				buyOrderQueue.get(0).setVolume( buyOrder.getVolume() - sellOrder.getVolume() );
			} else {
				tempTrade.setTradeVolume(buyOrder.getVolume());
				tra.add(tempTrade);
				sellOrderQueue.get(0).setVolume( sellOrder.getVolume() - buyOrder.getVolume() );
				buyOrderQueue.remove(0);
			}
		}
		return tra;
	}

	public void printTradeList(ArrayList<Trade> tr) {
		if (!tr.isEmpty()) {
			System.out.println();
			System.out.println("Match record:");
			for (int s = 0; s < tr.size(); s++) {
				System.out.println(tr.get(s).getBuyId() + " "
						+ tr.get(s).getBuyOrderId() + " "
						+ tr.get(s).getSellId() + " "
						+ tr.get(s).getSellOrderId() + " "
						+ tr.get(s).getTradePrice() + " "
						+ tr.get(s).getTradeVolume());
			}
		} else {
			System.out.println("No match!");
		}
	}
	
	public String getStockId() {
		return stockId;
	}
	
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	
	public List<Order> getBuyOrderQueue() {
		return buyOrderQueue;
	}
	
	public void setBuyOrderQueue(LinkedList<Order> buyOrderQueue) {
		if( null == buyOrderQueue )
			buyOrderQueue = new LinkedList<Order>();
		this.buyOrderQueue = buyOrderQueue;
	}
	
	public List<Order> getSellOrderQueue() {
		return sellOrderQueue;
	}
	
	public void setSellOrderQueue(LinkedList<Order> sellOrderQueue) {
		if( null == sellOrderQueue )
			sellOrderQueue = new LinkedList<Order>();
		this.sellOrderQueue = sellOrderQueue;
	}
	
	public Double getStockPrice() {
		if( null == stockPrice )
			return 0D;
		return stockPrice;
	}
	
	private void setStockPrice( Double stockPrice ) {
		this.stockPrice = stockPrice;
	}
	
}
