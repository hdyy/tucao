package bupt.yangyang.tucao;

public class NetWorker implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				System.out.println("networker running");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
