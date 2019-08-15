package s;

public class Singleton {

	/**
	 * 在《Java并发编程实践》推荐使用如下代码：
	 * 静态类内部加载:
	 *    第一次加载Singleton类的时候并不会初始化instance，
	 *    只有第一次调用Singleton的getInstance() 才会导致instance初始化
	 *    因此，第一次调用getInstance() 方法会导致虚拟机加载SingletonHolder类
	 *    这种方式不仅能够保证线程安全，也能保证单例对象的唯一性，同事也延迟了单例的实例化
	 */
	private Singleton(){}
    public static Singleton getInstance(){
         return SingletonHolder.instance;
    }
    
    private static class SingletonHolder{
        private static final Singleton instance = new Singleton();
    }

}
