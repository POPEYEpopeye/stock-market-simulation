package agent.base;

public class Stock {
	
	private String stockId="000000";
	private  String stockName = "";
	private Long directag;
	private Long avilableAmount = 0L;
	private Long lockedAmount = 0L;
	
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public Long getDirectag() {
		return directag;
	}
	public void setDirectag(Long directag) {
		this.directag = directag;
	}
	public Long getAvilableAmount() {
		return avilableAmount;
	}
	public void setAvilableAmount(Long avilableAmount) {
		this.avilableAmount = avilableAmount;
	}
	public Long getLockedAmount() {
		return lockedAmount;
	}
	public void setLockedAmount(Long lockedAmount) {
		this.lockedAmount = lockedAmount;
	}
	
}