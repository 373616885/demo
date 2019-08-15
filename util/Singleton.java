package s;

public class Singleton {
	
	/**
	   *    ��һ�μ���Singleton���ʱ�򲢲����ʼ��instance��
	   *    ֻ�е�һ�ε���Singleton��getInstance() �Żᵼ��instance��ʼ��
	   *    ��ˣ���һ�ε���getInstance() �����ᵼ�����������SingletonHolder��
	   *    ���ַ�ʽ�����ܹ���֤�̰߳�ȫ��Ҳ�ܱ�֤���������Ψһ�ԣ�ͬ��Ҳ�ӳ��˵�����ʵ����
	 */
	private Singleton(){}
    public static Singleton getInstance(){
         return SingletonHolder.instance;
    }
    
    private static class SingletonHolder{
        private static final Singleton instance = new Singleton();
    }

}
