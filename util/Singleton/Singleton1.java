package s;

public class Singleton1 {

	/**
	 * 因为在外面不可能创建对象，想调用方法就必须将方法static，
	 * 这样就可以通过:类名.方法名获取对象了,又因为静态方法里只能用静态成员，所以single必须static化
	 * 创建一个对象的任务完成了,一般情况下，我们另外在static加个final,
	 */
	private static final Singleton1 instance = new Singleton1();
	/**
	 * 既然在程序中只能创建一个对象，那也就是说在不能在其它类中任意创建对象，否则对象肯定就不止一个，
	 * 这就要求被创建那个类的构造方法不能public，也就是必须private，这个就保证了外部不能乱创建对象。
	 */
	private Singleton1() {

	}

	/**
	 * 外面创建不了对象，也就只能在内部创建对象了，因为我们讲究Java的封装特性，对象是private，所以我们要提供一个public方法供外界调用
	 */
	public static Singleton1 getInstance(){
		return Singleton1.instance;
	}

}
