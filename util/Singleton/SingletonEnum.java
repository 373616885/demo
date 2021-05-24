package s;

public enum SingletonEnum {

	/**
	 * Effective Java作者Josh Bloch 提倡的方式，简洁而完美
	 * 涉及到反序列化创建对象时会试着使用枚举的方式来实现单例
	 */
	INSTANCE;
	public void methods() {
		System.out.println("SingletonEnum");
	}
	
	public static void main(String[] args) {
		SingletonEnum.INSTANCE.methods();
	}
}
