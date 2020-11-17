class ShareData1{
    private int num = 0;

    public synchronized void increment() throws InterruptedException {
        while (num != 0){ wait();}
        ++num;
        System.out.println(Thread.currentThread().getName()+"\t"+num);
        notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        while (num == 0){ wait();}
        --num;
        System.out.println(Thread.currentThread().getName()+"\t"+num);
        notifyAll();
    }
}
public class TheFirstProdAndConsumer {
    public static void main(String[] args) {
        ShareData1 shareData = new ShareData1();
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    shareData.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                try {
                    shareData.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"B").start();
    }
}
