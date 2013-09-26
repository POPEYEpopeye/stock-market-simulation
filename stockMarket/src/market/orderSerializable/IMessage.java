package market.orderSerializable;

import java.io.Serializable;

public interface IMessage extends Serializable{
	
	public String convertSelfToString();	//将IMessage实例转化为字符串
	public void setStringForSelf(String message);	//将String 转化成IMessage实例
	
}
