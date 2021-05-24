package s;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singleton3 {

	private Singleton3() {
	}

	private static volatile Singleton3 instance;

	public static Singleton3 getInstance() {
		if (instance == null) { // Single Checked
			synchronized (Singleton3.class) {
				if (instance == null) { // Double Checked
					// instance = new Singleton()这句，这并非是一个原子操作，事实上在 JVM 中这句话大概做了下面 3 件事情
					/*
					 * 1.给 instance 分配内存
					 * 2. 调用 Singleton 的构造函数来初始化成员变量
					 * 3.将instance对象指向分配的内存空间（执行完这步instance 就为非 null 了）
					 * 4.但是在 JVM 的即时编译器中存在指令重排序的优化。也就是说上面的第二步和第三步的顺序是不能保证的，
					 * 最终的执行顺序可能是 1-2-3 也可能是 1-3-2。如果是后者，则在 3 执行完毕、2 未执行之前，被线程二抢占了，
					 * 这时 instance 已经是非 null 了（但却没有初始化），
					 * 所以线程二会直接返回 instance，然后使用，然后顺理成章地报错
					 * 解决 我们只需要将 instance 变量声明成 volatile 就可以了
					 */
					// 但是特别注意在 Java 5 以前的版本使用了 volatile 的双检锁还是有问题的。
					//其原因是 Java 5 以前的 JMM （Java 内存模型）是存在缺陷的，即时将变量声明成 volatile
					//也不能完全避免重排序，主要是 volatile 变量前后的代码仍然存在重排序问题。
					//这个 volatile 屏蔽重排序的问题在 Java 5 中才得以修复，所以在这之后才可以放心使用 volatile
					instance = new Singleton3(); // error
				}
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + ":" + Singleton2.getInstance());
				}
			});
		}
	}

}
