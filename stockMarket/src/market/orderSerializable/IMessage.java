package market.orderSerializable;

import java.io.Serializable;

public interface IMessage extends Serializable{
	
	public String convertSelfToString();	//��IMessageʵ��ת��Ϊ�ַ���
	public void setStringForSelf(String message);	//��String ת����IMessageʵ��
	
}
