package agent.orderSerializable;

public class Trade implements IMessage {

	private static final long serialVersionUID = 4719220789711904826L;	//generated serialVersionUID
	
	private int buyId;
    private int buyOrderId;
    private int sellId;
    private int sellOrderId;
    private double tradePrice;
    private int tradeVolume;
    private Long tradeTime;
    
    public Trade() {
        buyId = 0;
        buyOrderId = 0;
        sellId = 0;
        sellOrderId = 0;
        tradePrice = 0;
        tradeVolume = 0;
        setTradeTime(0L);
    }

    public Trade(int buyid, int buyOrderId, int sellid, int sellOrderId, double tradeprice, int tradevolume) {
        this.buyId = buyid;
        this.buyOrderId = buyOrderId;
        this.sellId = sellid;
        this.sellOrderId = sellOrderId;
        this.tradePrice = tradeprice;
        this.tradeVolume = tradevolume;
    }

    public int getBuyId() {
        return buyId;
    }

    public void setBuyId(int buyid) {
        this.buyId = buyid;
    }

    public int getSellId() {
        return sellId;
    }

    public void setSellId(int sellid) {
        this.sellId = sellid;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(double tradeprice) {
        this.tradePrice = tradeprice;
    }

    public int getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(int tradevolume) {
        this.tradeVolume = tradevolume;
    }

    public int getBuyOrderId() {
        return buyOrderId;
    }

    public void setBuyOrderId(int buyOrderId) {
        this.buyOrderId = buyOrderId;
    }

    public int getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(int sellOrderId) {
        this.sellOrderId = sellOrderId;
    }

	public void setTradeTime(Long tradeTime) {
		this.tradeTime = tradeTime;
	}

	public Long getTradeTime() {
		return tradeTime;
	}

	@Override
	public String convertSelfToString() {
		int lackLength;
        String buyIdStr = String.valueOf(buyId);
        if (buyIdStr.length() <= 8) {
            lackLength = 8 - buyIdStr.length();
            for (int i = 0; i < lackLength; i++) {
                buyIdStr = "0" + buyIdStr;
            }
        } else {
            buyIdStr = buyIdStr.substring(0,8);
        }

        String buyOrderIdStr = String.valueOf(buyOrderId);
        if (buyOrderIdStr.length() <= 4) {
            lackLength = 4 - buyOrderIdStr.length();
            for (int i = 0; i < lackLength; i++) {
                buyOrderIdStr = "0" + buyOrderIdStr;
            }
        } else {
            buyOrderIdStr = buyOrderIdStr.substring(0,4);
        }

        String sellIdStr = String.valueOf(sellId);
        if (sellIdStr.length() <= 8) {
            lackLength = 8 - sellIdStr.length();
            for (int i = 0; i < lackLength; i++) {
                sellIdStr = "0" + sellIdStr;
            }
        } else {
            sellIdStr = sellIdStr.substring(0,8);
        }

        String sellOrderIdStr = String.valueOf(sellOrderId);
        if (sellOrderIdStr.length() <= 4) {
            lackLength = 4 - sellOrderIdStr.length();
            for (int i = 0; i < lackLength; i++) {
                sellOrderIdStr = "0" + sellOrderIdStr;
            }
        } else {
            sellOrderIdStr = sellOrderIdStr.substring(0,4);
        }
        
        String tradePriceStr = String.valueOf(tradePrice);
        if (tradePriceStr.length() <= 8) {
            lackLength = 8 - tradePriceStr.length();
            for (int i = 0; i < lackLength; i++) {
                tradePriceStr = "0" + tradePriceStr;
            }
        } else {
            tradePriceStr = tradePriceStr.substring(0,8);
        }
        
        String tradeVolumeStr = String.valueOf(tradeVolume);
        if (tradeVolumeStr.length() <= 10) {
            lackLength = 10 - tradeVolumeStr.length();
            for (int i = 0; i < lackLength; i++) {
                tradeVolumeStr = "0" + tradeVolumeStr;
            }
        } else {
            tradeVolumeStr = tradeVolumeStr.substring(0,10);
        }
        return buyIdStr + buyOrderIdStr + sellIdStr + sellOrderIdStr
                + tradePriceStr + tradeVolumeStr;
	}
	
	@Override
	public void setStringForSelf(String message) {
        
        String buyIdStr = message.substring(0, 8);
        this.setBuyId(Integer.parseInt(buyIdStr));
        
        String buyOrderIdStr = message.substring(8, 12);
        this.setBuyOrderId(Integer.parseInt(buyOrderIdStr));
        
        String sellIdStr = message.substring(12, 20);
        this.setSellId(Integer.parseInt(sellIdStr));
        
        String sellOrderIdStr = message.substring(20, 24);
        this.setSellOrderId(Integer.parseInt(sellOrderIdStr));
        
        String tradePriceStr = message.substring(24, 32);
        this.setTradePrice(Double.parseDouble(tradePriceStr));
        
        String tradeVolumeStr = message.substring(32, 42);
        this.setTradeVolume(Integer.parseInt(tradeVolumeStr));
        
	}
}
