import java.util.ArrayList;

public class Task1 {
    
    public static void main(String[] args) throws InterruptedException
    {

        int argsize = 6;
        QueueControl queue = new QueueControl();

        Thread myThreads[] = new Thread[argsize];
        for (int j = 0; j < argsize/2; j++) {
            myThreads[j] = new Thread(new Producer(queue));
            myThreads[j].start();
            }
        for (int j = 3; j < argsize; j++) {
            myThreads[j] = new Thread(new Consumer(queue,j-2));
            myThreads[j].start();
        }
    }
}

class Consumer implements Runnable {
    private ArrayList<Integer> consbuffer = new ArrayList<Integer>(100);
    private QueueControl queue;
    private int id;
    public Consumer(QueueControl queue, int id) {
        this.queue = queue;
        this.id = id;
    }

    public void run(){
        while(true)
        { 
            try {
             consbuffer.add(this.queue.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (consbuffer.size() == 100)
            break;
        }
        System.out.println(id + ". Consumer buffer:  " + consbuffer);

    }
    
}



class Producer implements Runnable {
    private static int number = 100;
    private static int numberend = 201;
    QueueControl queue;
    int data;
    private ArrayList<Integer> prodbuffer = new ArrayList<Integer>();
    Producer(QueueControl queue)
    {
        for ( ;number < numberend; number++)
        {
            prodbuffer.add(number);
        }
        numberend += 100;
        this.queue = queue;
    }

    @Override
    public void run()
    {
        
        while(!prodbuffer.isEmpty())
        {
        try {

            data = prodbuffer.get(0);
            prodbuffer.remove(0);

            this.queue.put(data);
        } catch (Exception exp)
        {

        }
    }
    }
}


class QueueControl implements IBuffer{
    private static ArrayList<Integer> mainbuffer = new ArrayList<Integer>();
    private final int MAX_SIZE = 10;
    int data;
    public int test = 0;
    public synchronized void put(int v) throws InterruptedException{
        while(mainbuffer.size() == MAX_SIZE){
                wait();
        }
        mainbuffer.add(v);
        System.out.println("Main buff: " + mainbuffer);
        notify();
    }


    public synchronized int get() throws InterruptedException{
        notify();
        while(mainbuffer.isEmpty())
        {
            wait();
        }
        data = mainbuffer.get(0);
        mainbuffer.remove(0);
        return data;
    }
}

interface IBuffer{
    void put(int v) throws InterruptedException;
    int get() throws InterruptedException;
}
