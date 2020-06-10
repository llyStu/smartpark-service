package com.vibe.service.information.push;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class MessageQueue<T> {
    private final ArrayBlockingQueue<T> msgs;
    private final AtomicLong prevUpdate;
    private final int limitCapacity, outCapacity;
    private Consumer<List<T>> callback;

    public MessageQueue(int capacity, Consumer<List<T>> callback) {
        this.msgs = new ArrayBlockingQueue<>(capacity);
        this.prevUpdate = new AtomicLong(0);
        int tmp = capacity / 4;
        this.limitCapacity = tmp * 3;
        this.outCapacity = tmp * 2;
        this.callback = callback;
    }

    public ScheduledFuture<?> addHeartBeat(final int heartime, ScheduledExecutorService scheduledThread) {
        final int interval = heartime / 2;
        return scheduledThread.scheduleWithFixedDelay(() -> {
            final long prev = prevUpdate.get();
            final long now = System.currentTimeMillis();
            if (now - prev > interval && prevUpdate.compareAndSet(prev, now)) {
                ArrayList<T> out = new ArrayList<T>(0);
                msgs.drainTo(out);
                if (out.size() != 0)
                    callback.accept(out);
            }
        }, heartime, heartime, TimeUnit.MILLISECONDS);
    }

    public void clear() {
        this.msgs.clear();
    }

    public boolean add(T msg) {
        final long prev = prevUpdate.get();
        final long now = System.currentTimeMillis();
        if (msgs.size() > this.limitCapacity && prevUpdate.compareAndSet(prev, now)) {
            ArrayList<T> out = new ArrayList<T>(0);
            msgs.drainTo(out, this.outCapacity);
            if (out.size() != 0) callback.accept(out);
        }
        return msgs.offer(msg);
    }
//	
//	
//	public static void main(String[] args) throws InterruptedException, ExecutionException {
//		ThreadFactory tf = cb -> {
//			Thread thread = new Thread(cb);
//			thread.setDaemon(true);
//			return thread;
//		};
//		
//		ConcurrentLinkedQueue<List<Integer>> a = new ConcurrentLinkedQueue<>();
//		MessageQueue<Integer> mq = new MessageQueue<Integer>(10, it -> {
//			a.add(it);
//		}).addHeartBeat(500, Executors.newSingleThreadScheduledExecutor(tf));
//		
//		val m  = new AtomicInteger(0);
//		
//		int N = 1000;
//		
//		ExecutorService pool = Executors.newFixedThreadPool(N, tf);
//		CountDownLatch startSignal = new CountDownLatch(1);
//		CountDownLatch stopSignal = new CountDownLatch(N);
//		for (int k = 0; k < N; ++k)  pool.execute(() -> {
//			try {
//				startSignal.await();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			int i;
//			while ((i = m.getAndIncrement()) < 1000000) {
//				while (!mq.add(i)) {
//					System.out.println("插入失败: "+ i);
//				}
//			}
//			stopSignal.countDown();
//		});
//		System.out.println("....."+ Thread.currentThread().getName() + "begin");
//		long s = System.nanoTime();
//		startSignal.countDown();
//		stopSignal.await();
//		Thread.sleep(500);
//		System.out.println("....."+ Thread.currentThread().getName() + "end: "+ java.time.Duration.ofNanos(System.nanoTime() - s));
//		Optional<Integer> sum = a.stream().flatMap(it -> it.stream()).reduce((p, q) -> p+q);
//		System.out.println(sum +"->"+( 999999*500000-sum.get()));
//		System.out.println(mq.msgs);
//	}

}
