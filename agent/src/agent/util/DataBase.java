package agent.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            pstmtAgent = con.prepareStatement("insert into agentOrders(AgentId,Price,Volume,Direction,CreateTime) values(" +
                    "?,?,?,?,?)");
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
                    "?,?,?,?,?)");
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
     * Insert orders into table "agentOrders".
     *
     * @param or a order
     */
    public int insertOrder(Order or) {
    	Statement stmt = null;
    	int currentKey = -1;
        try {
            pstmtAgent.setInt(1, or.getAgentId());
            pstmtAgent.setDouble(2, or.getPrice());
            pstmtAgent.setInt(3, or.getVolume());
            pstmtAgent.setInt(4, or.getDirection());
            pstmtAgent.setLong(5, System.currentTimeMillis());
            pstmtAgent.executeUpdate();
            
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            if(rs.next()){
            	currentKey = rs.getInt(1);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Insert order failed!");
        }
        return currentKey;
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
     * Insert agent attributes into table "attOfAgent".
     *
     * @param agent of type BaseAgent
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
    
}