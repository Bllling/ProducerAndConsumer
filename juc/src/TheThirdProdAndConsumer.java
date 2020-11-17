import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ShareData3{
    private volatile boolean flag = true;
    private AtomicInteger atomicInteger = new AtomicInteger();
    BlockingQueue blockingQueue = null;
    public ShareData3(BlockingQueue blockingQueue){
        // 传接口
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }
    public void increment() throws InterruptedException {
        String data = null;
        boolean retValue;
        while (flag) {
            data = atomicInteger.incrementAndGet()+"";
            retValue = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);
            if (retValue){
                System.out.println(Thread.currentThread().getName()+"\t插入队列\t"+data+"成功");
            } else {
                System.out.println(Thread.currentThread().getName()+"\t插入队列\t"+data+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName()+"溜了溜了，下班了");
    }

    public void decrement() throws InterruptedException {
        String result ;
        while (flag) {
            result = (String) blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (result == null || "".equalsIgnoreCase(result)){
                flag = false;
                System.out.println("2s没有消费，超时了，下班下班溜了溜了");
                return;
            }
            System.out.println(Thread.currentThread().getName()+"消费"+result+"成功");

        }
        System.out.println(Thread.currentThread().getName()+"溜了溜了，下班了");
    }

    public void stop(){
        flag = false;
    }
}
public class TheThirdProdAndConsumer {
    public static void main(String[] args) throws InterruptedException {
        ShareData3 shareData = new ShareData3(new ArrayBlockingQueue(10));
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

        TimeUnit.SECONDS.sleep(5);

        shareData.stop();
        System.out.println("老板叫你们下班了");
    }
}
