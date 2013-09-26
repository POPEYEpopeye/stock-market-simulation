/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import market.action.MarketDataServlet;
import market.orderSerializable.Order;
import market.orderSerializable.Trade;


public class DataBase {
	
	private static DataBase _instance = null;
	
	public static DataBase getInstance(){
		if(_instance == null){
			_instance = new DataBase();
		}
		return _instance;
	}
	private DataBase(){
		
	}
	
    /**
     * Delete previous orders from "agentOrders".
     *
     * @param agentId
     */
    public void deletePreOrders(int agentId) {
    	Connection con = null;
    	Statement stmt = null;
    	try {
        	con = DbPoolUtil.getInstance().getConnection();
            stmt = con.createStatement();
            String delete = "delete from agentOrders where agentId = " + agentId;
            stmt.executeUpdate(delete);
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Delete previous orders failed!");
        } catch (Exception e) {
			e.printStackTrace();
		}finally{
			DbPoolUtil.closeStatement(stmt);
            DbPoolUtil.closeConnection(con);
		}
    }

    /**
     * Insert orders into table "agentOrders".
     *
     * @param or a order
     */
    public void insertOrder(Order or) {
    	Connection con = null;
    	PreparedStatement pstmtAgent = null;
    	try {
        	con = DbPoolUtil.getInstance().getConnection();
        	pstmtAgent = con.prepareStatement("insert into agentOrders(AgentId,Price,Volume,Direction,OrderId,CreateTime) values("
                  + "?,?,?,?,?,?)");
            pstmtAgent.setInt(1, or.getAgentId());
            pstmtAgent.setDouble(2, or.getPrice());
            pstmtAgent.setInt(3, or.getVolume());
            pstmtAgent.setInt(4, or.getDirection());
            pstmtAgent.setInt(5, or.getOrderId());
            pstmtAgent.setLong(6, or.getCreateTime());
            pstmtAgent.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Insert order failed!");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DbPoolUtil.closePrepStmt(pstmtAgent);
			DbPoolUtil.closeConnection(con);
		}
    }

    /**
     * Insert trade orders into table "tradeOrder".
     *
     * @param tra a trade order
     */
    public void insertTradeOrder(Trade tra) {
    	Connection con = null;
    	PreparedStatement pstmtServer = null;
        try {
        	con = DbPoolUtil.getInstance().getConnection();
        	pstmtServer = con.prepareStatement("insert into tradeOrders(BuyId, BuyOrderId, SellId, SellOrderId, TradePrice, TradeVolume, TradeTime) values("
                    + " ?,?,?,?,?,?,? )");
            pstmtServer.setInt( 1, tra.getBuyId() );
            pstmtServer.setInt( 2, tra.getBuyOrderId() );
            pstmtServer.setInt( 3, tra.getSellId() );
            pstmtServer.setInt( 4, tra.getSellOrderId() );
            pstmtServer.setDouble( 5, tra.getTradePrice() );
            pstmtServer.setInt( 6, tra.getTradeVolume() );
            pstmtServer.setLong( 7, tra.getTradeTime() );
            pstmtServer.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Insert tradeOrder failed!");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DbPoolUtil.closePrepStmt(pstmtServer);
			DbPoolUtil.closeConnection(con);
		}

    }
    
    public List<Trade> getLastestTradeOrders( Long fromTime, Long endTime ){
    	List<Trade> result = new ArrayList<Trade>();
    	
    	String sql = " select T.* from tradeOrders T where " ;
    	if( fromTime != null )
    		sql += " T.TradeTime >= "+fromTime +" and ";
    	if( endTime != null )
    		sql += " T.TradeTime <= "+endTime + " and ";
    	sql += " 1=1 order by T.TradeId desc ";
    	Connection con = null;
    	Statement st = null ;
    	try {
    		con = DbPoolUtil.getInstance().getConnection();
			st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			for( int i = 0; i < MarketDataServlet.SIZE && rs.next(); i++ ){
				Trade to = new Trade();
				to.setBuyId(rs.getInt(2));
				to.setBuyOrderId(rs.getInt(3));
				to.setSellId(rs.getInt(4));
				to.setSellOrderId(rs.getInt(5));
				to.setTradePrice(rs.getDouble(6));
				to.setTradeVolume(rs.getInt(7));
				to.setTradeTime(rs.getLong(8));
				result.add(to);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DbPoolUtil.closeStatement(st);
			DbPoolUtil.closeConnection(con);
		}
    	return result;
    }
    
    public Trade getLatestTradeOrder(){
    	Trade tOrder = new Trade();
    	
    	String sql = "select * from tradeOrders order by TradeId desc limit 1";
    	Connection con = null;
    	Statement st = null ;
    	try {
    		con = DbPoolUtil.getInstance().getConnection();
			st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				
				tOrder.setBuyId(rs.getInt(2));
				tOrder.setBuyOrderId(rs.getInt(3));
				tOrder.setSellId(rs.getInt(4));
				tOrder.setSellOrderId(rs.getInt(5));
				tOrder.setTradePrice(rs.getDouble(6));
				tOrder.setTradeVolume(rs.getInt(7));
				tOrder.setTradeTime(rs.getLong(8));
			}
    	}catch (SQLException e) {
			e.printStackTrace();
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DbPoolUtil.closeStatement(st);
			DbPoolUtil.closeConnection(con);
		}
    	return tOrder;
    }
    
}