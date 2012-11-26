package epfl.sweng.test.mocking;

/**
 * 
 * @author cyril
 *
 */
public abstract class ServerSimulatorFactory {

	private static ServerSimulator mInstance;
	
	
	public static ServerSimulator getInstance() {
		if (mInstance == null) {
			mInstance = new QuizServerSimulator();
		}
		return mInstance;
	} 
	
	public static void setInstance(ServerSimulator serverSimulator) {
		mInstance = serverSimulator;
	} 
	
	
}
