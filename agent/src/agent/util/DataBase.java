package agent.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import agent.orderSerializable.Order;
import agent.orderSerializable.Trade;

public class DataBase {

    Connection con;
    String drive = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/test";
    String username = "root";
    String password = "root";
    PreparedStatement pstmtAgent;
    PreparedStatement pstmtServer;
    PreparedStatement pstmtStoreAtt;
    PreparedStatement pstmtDecisionLog;

    private static DataBase _instance;
    
    /**
     * constructed function with no params.
     */
    public DataBase() {
        try {
            Class.forName(drive);
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connect to DataBase sucessed!");
            pstmtAgent = con.prepareStatement("insert into agentOrders(AgentId,Price,Volume,Direction,OrderId,CreateTime) values(" +
                    "?,?,?,?,?,?)");
            pstmtServer = con.prepareStatement("insert into tradeOrders(BuyId, BuyOrderId, SellId, SellOrderId, TradePrice, TradeVolume, TradeTime) values(" +
                    "?,?,?,?,?,?,?)");
            pstmtStoreAtt = con.prepareStatement("insert into attOfAgent values(" +
                    "?,?,?,?,?)");
            pstmtDecisionLog = con.prepareStatement("insert into decisionLog(agentId, orignalPrice, orignalVolume, preId, prePrice, preVolume, postId, postPrice, postVolume, " +
        			" resultPrice, resultVolume, decisionTime ) values(?,?,?,?,?,?,?,?,?,?,?,?)");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connect to DataBase Failed!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Create database drive Failed!");
        }
    }
    
    public static DataBase getInstance(){
    	if( _instance == null )
    		_instance = new DataBase();
    	return _instance;
    }
    
    /**
     * constructed function with params.
     *
     * @param drive,url,username,password
     */
    public DataBase(String drive, String url, String username, String password) {
        this.drive = drive;
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            Class.forName(drive);
            con = DriverManager.getConnection(url, username, password);
            //System.out.println("Connect to DataBase sucessed!");
            pstmtAgent = con.prepareStatement("insert into agentOrders values(" +
                    "?,?,?,?,?,?)");
            pstmtServer = con.prepareStatement("insert into tradeOrders values(" +
                    "?,?,?,?,?,?)");
            pstmtStoreAtt = con.prepareStatement("insert into attOfAgent values(" +
                    "?,?,?,?,?)");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Create database drive Failed!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connect to DataBase Failed!");
        }
    }
    
    /**
     * Delete previous orders from "agentOrders".
     *
     * @param agentId
     */
    public void deletePreOrders(int agentId){
        try {
            Statement stmt = con.createStatement();
            String delete = "delete from agentOrders where agentId = " + agentId; 
            stmt.executeUpdate(delete);
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Delete previous orders failed!");
        }
    }
     /**
     * Insert orders into table "agentOrders".
     *
     * @param or a order
     */
    public void insertOrder(Order or) {
        try {
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
        }

    }
    
    /**
     * Insert trade orders into table "tradeOrder".
     *
     * @param tra a trade order
     */
    public void insertTradeOrder(Trade tra) {
        try {
            pstmtServer.setInt(1, tra.getBuyId());
            pstmtServer.setInt(2, tra.getBuyOrderId());
            pstmtServer.setInt(3, tra.getSellId());
            pstmtServer.setInt(4,tra.getSellOrderId());
            pstmtServer.setDouble(5, tra.getTradePrice());
            pstmtServer.setInt(6, tra.getTradeVolume());
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Insert tradeOrder failed!");
        }

    }
    /**
     * Insert trade orders into table "tradeOrder".
     *
     * @param tra a trade order
     */
//    public void storeAttributes(attributesOfAgent att){
//        try {
//            pstmtStoreAtt.setInt(1, att.getId());
//            pstmtStoreAtt.setDouble(2, att.getAvailableCash());
//            pstmtStoreAtt.setDouble(3, att.getLockedCash());
//            pstmtStoreAtt.setInt(4, att.getAvailableHold());
//            pstmtStoreAtt.setInt(5, att.getLockedHold());
//            pstmtStoreAtt.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(dataBase.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Store attributes failed!");
//        }
//        
//    }
    

    public void disconnectDB() {
        try {
            con.close();
            System.out.println("Connection drop!");
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertDecisionLog(Order orignalOrder, List<Order> referencedOrderList, Order resultOrder){
    	try {
			
    		pstmtDecisionLog.setString(1, orignalOrder.getAgentId()+"");
			pstmtDecisionLog.setDouble(2, orignalOrder.getPrice());
			pstmtDecisionLog.setLong(3, orignalOrder.getVolume());
			if( referencedOrderList.size() > 0 ){
				pstmtDecisionLog.setString(4, referencedOrderList.get(0).getAgentId()+"");
				pstmtDecisionLog.setDouble(5, referencedOrderList.get(0).getPrice());
				pstmtDecisionLog.setLong(6, referencedOrderList.get(0).getVolume());
				pstmtDecisionLog.setString(7, "");
				pstmtDecisionLog.setDouble(8, 0);
				pstmtDecisionLog.setLong(9, 0);
				if( referencedOrderList.size() > 1 ){
					pstmtDecisionLog.setString(7, referencedOrderList.get(1).getAgentId()+"");
					pstmtDecisionLog.setDouble(8, referencedOrderList.get(1).getPrice());
					pstmtDecisionLog.setLong(9, referencedOrderList.get(1).getVolume());
				}
			}
			
			pstmtDecisionLog.setDouble(10, resultOrder.getPrice());
			pstmtDecisionLog.setLong(11, resultOrder.getVolume());
			pstmtDecisionLog.setLong(12, resultOrder.getCreateTime());
			
			pstmtDecisionLog.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DataBase db = new DataBase();
        //java.util.ArrayList<tradeOrder> traList =
        // new java.util.ArrayList();

        //db.insertTradeOrder(new tradeOrder(8, 10, 5.55, 62));
        //db.insertTradeOrder(new tradeOrder(9, 8, 5.1, 60));
        //db.insertTradeOrder(new tradeOrder(16, 10, 7.3, 20));

        //db.getLatestOneOrder();
        //System.out.println(db.getMyTradeOrder(11, 108, traList));
        //System.out.println(traList.size());
        //if (0 == traList.size()) {
        // System.out.println("Have no trade orders belong to me!");
        //}
        db.disconnectDB();
    }
}