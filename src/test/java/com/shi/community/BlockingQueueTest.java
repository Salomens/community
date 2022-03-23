package com.shi.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//阻塞队列
//生产者消费者模式
public class BlockingQueueTest {
    public static void main(String[] args) {
        //实例化阻塞队列
        //生产者消费者共用一个阻塞队列
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
        //new一个生产者线程
        Thread  producer = new Thread(new Producer(queue));
        producer.start();
        //new消费线程 一个生产者生产,三个消费者消费
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();


    }
}
//生产者
class Producer implements Runnable{

    //添加阻塞队列
    //实例化
    private BlockingQueue<Integer> queue;
    public Producer(BlockingQueue<Integer> queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {//生产者生产100个数据
                Thread.sleep(20);//每次生产间隔20ms
                queue.put(i);
                System.out.println(Thread.currentThread().getName() + "生产:" + queue.size());

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
//消费者
class Consumer implements Runnable{
    //添加阻塞队列
    //实例化
    private BlockingQueue<Integer> queue;
    public Consumer(BlockingQueue<Integer> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true){//生产者不停在消费
                Thread.sleep(new Random().nextInt(1000));
                queue.take();
                System.out.println(Thread.currentThread().getName() + "消费:" + queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
