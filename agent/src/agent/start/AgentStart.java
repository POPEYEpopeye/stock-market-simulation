package agent.start;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import agent.base.BaseAgent;

public class AgentStart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String marketIP = args[0];
		//String marketIP = "192.168.1.107";
		
		int numberOfAgent = Integer.valueOf(args[1]);
		//int numberOfAgent = 100;
		
		final int recycleTime = Integer.valueOf(args[2]);
		//final int recycleTime = 100;
		
		final String marketUrl = "http://" + marketIP + ":8080/stockMarket/Market?";
		final String registerUrl = "http://" + marketIP + ":8080/stockMarket/Register?";
		
		 ArrayList<Thread> threadList = new ArrayList<Thread>();
		 long s_time = System.currentTimeMillis();
		
		for( int i =0; i < numberOfAgent; i++ ){
			Thread t = new Thread(){
				@Override
				public void run() {
					super.run();
					BaseAgent agentDemo = new AgentDemo(1000, 0, 1000, 0, 0, 0);
					for( int i = 0; i < recycleTime; i++ ){
						agentDemo.simulate( marketUrl, registerUrl );
						try {
							Thread.sleep( 1000 );
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(10000);
						agentDemo.refreshMessage();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			t.start();
			threadList.add(t);			
		}
		
		for(Iterator it = threadList.iterator();it.hasNext();){
            try{
                ((Thread)it.next()).join();
            }catch(Exception e){
                System.out.println(e);
            }
        }
        long e_time = System.currentTimeMillis();
        
        System.out.println("Total running time is " + (e_time - s_time)/ (double)1000 + "s" );
        
        FileWriter fw;
		try {
			File file = new File("time.log");
			if(file.exists())
				file.delete();
			fw = new FileWriter(file);
			String timeStr = "Total running time is " + (e_time - s_time) + "ms";  
			fw.write(timeStr, 0, timeStr.length());  
			fw.flush();  
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

}
