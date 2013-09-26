package market.orderSerializable;

public class Order implements IMessage{
	
	private static final long serialVersionUID = -6715728675040627672L;		//generated serialVersionUID
	
	private Integer agentId;
    private Double price;
    private Integer volume;
    private Integer direction;  //0 mean sell, 1 mean buy.
    private Integer orderId;
    private Long createTime;


    public Order() {
        this.agentId = 0;
        this.price = 0d;
        this.volume = 0;
        this.direction = 0;
        this.orderId = 0;
    }

    public Order(Integer agentId, Double price, Integer volume, Integer direction, Integer orderId, Long createTime) {
        this.agentId = agentId;
        this.price = price;
        this.volume = volume;
        this.direction = direction;
        this.orderId = orderId;
        this.createTime = createTime;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
    	if( null == price )
    		return 0D;
        return price;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Integer getVolume() {
    	if( null == volume )
    		return 0;
        return volume;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getDirection() {
    	if( null == direction )
    		return 0;
        return direction;
    }

    public Integer getOrderId() {
    	if( null == orderId )
    		return 0;
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Long getCreateTime() {
    	if( null == createTime )
    		return 0L;
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

	@Override
	public String convertSelfToString() {
		Integer lackLength;
        String agentIdStr = String.valueOf(agentId);
        if(agentIdStr.length() <= 8){
            lackLength = 8 - agentIdStr.length();
            for(int i = 0; i < lackLength; i++){
                agentIdStr = "0" + agentIdStr;
            }
        }
        else{
            agentIdStr = agentIdStr.substring(agentIdStr.length() - 8);
        }
        
        String volumeStr = String.valueOf(volume);
        if(volumeStr.length() <= 10){
            lackLength = 10 - volumeStr.length();
            for(int i = 0; i < lackLength; i++){
                volumeStr = "0" + volumeStr;
            }
        }
        else{
            volumeStr = volumeStr.substring(0,10);
        }
        
        String priceStr = String.valueOf(price);
        if(priceStr.length() <= 8){
            lackLength = 8 - priceStr.length();
            for(int i = 0; i < lackLength; i++){
                priceStr = "0" + priceStr;
            }
        }
        else{
            priceStr = priceStr.substring( 0,8 );
        }
        
        String orderIdStr = String.valueOf(orderId);
        if(orderIdStr.length() <= 4){
            lackLength = 4 - orderIdStr.length();
            for(int i = 0; i < lackLength; i++){
                orderIdStr = "0" + orderIdStr;
            }
        }
        else{
            orderIdStr = orderIdStr.substring( 0,4);
        }
        
        String directionStr = String.valueOf(direction);

        String timeStr = Long.toString(createTime);
        
        return agentIdStr + priceStr + volumeStr + directionStr + orderIdStr /*+ timeStr*/;
	}

	@Override
	public void setStringForSelf(String message) {
        
        String agentIdStr = message.substring(0, 8);
        this.setAgentId(Integer.parseInt(agentIdStr));
        
        String priceStr = message.substring(8, 16);
        this.setPrice( Double.parseDouble(priceStr) );
        
        String volumeStr = message.substring(16, 26);
        this.setVolume(Integer.parseInt(volumeStr));
        
        
        
        
        String directionStr = message.substring(26, 27);
        this.setDirection(Integer.parseInt(directionStr));
        
        String orderIdStr = message.substring(27, 31);
        this.setOrderId(Integer.parseInt(orderIdStr));
        
        String timeStr = message.substring(31);
        if( null!= timeStr && !"".equals(timeStr.trim()) )
        	this.setCreateTime(Long.parseLong(timeStr));
        else
        	this.setCreateTime(System.currentTimeMillis());
//        or.setCreateTime(Long.parseLong(timeStr));
        
	}
}
