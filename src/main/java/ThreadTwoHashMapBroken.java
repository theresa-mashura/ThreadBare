import java.util.HashMap;
import java.util.Set;

public class ThreadTwoHashMapBroken extends Thread {
    HashMap<String, Thread> threadMap;

    public ThreadTwoHashMapBroken(String name) {
        super(name);
        this.threadMap = new HashMap<>();
    }

    @Override
    public void run() {
        System.out.println("ThreadTwoHashMapB - START "+Thread.currentThread().getName());
        try {
            Thread.sleep(1);
            //Get database connection, delete unused data from DB
            doDBProcessing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ThreadTwoHashMapB - END "+Thread.currentThread().getName());
    }

    private void doDBProcessing() throws InterruptedException {
        Thread.sleep(5);
    }

    // Run this
    public static void main(String[] args){
        ThreadTwoHashMapBroken tm = new ThreadTwoHashMapBroken(""+10);

        // What's wrong with this idea??...
        new Thread("Run of " + 6){
            public void run(){
                tm.runMapOfSize(6);
            }
        }.start();
        new Thread("Run of " + 8){
            public void run(){
                tm.runMapOfSize(8);
            }
        }.start();

    }

    private void runMapOfSize(int size) {
        System.out.println("Constructing HashMap of Size " + size);
        Integer threadCount = size;

        // THREAD 6 PUTS THE THREADS INTO THE HASH MAP
        for (int i = 0; i < threadCount; i++) {
            this.threadMap.put("T"+ i, new ThreadTwoHashMapBroken("T"+ i));
            System.out.println("MAP " + size + " PUT T" + i);
        }

        // THEN:
        // THREAD 6 ITERATES THROUGH HASH MAP TO EXEC START() ON EACH THREAD
        // WHILE THIS IS HAPPENING...THREAD 8 BEGINS TO RUN THIS METHOD AS WELL...
            // THREAD 8 STARTS PUTTING NEW THREADS INTO THE HASH MAP...ALTERS THE HASH MAP
            // IF THREAD 6 IS STILL ITERATING...WILL THROW ConcurrentModificationException B/C THE HASH MAP HAS BEEN ALTERED WHILE WE'RE ITERATING THROUGH IT

            // IF THREAD 6 IS ALREADY FINISHED ITERATING BY THE TIME THREAD 8 STARTS ALTERING THE HASH MAP, NO EXCEPTION WILL BE THROWN...I THINK THAT'S HOW IT WORKS?
        System.out.println("Starting Threads in HashMap - Size " + size);
        Set<String> names = this.threadMap.keySet();
        for (String name : names) {
            this.threadMap.get(name).start();
        }
        System.out.println("Thread HashMap, all have been started - Size " + size);

        // AFTER EXCEPTION GETS THROWN, PROGRAM WILL STILL FINISH RUNNING....
        // AS THERE ARE NOW ACTIVE THREADS, SO THOSE THREADS WILL FINISH THEIR EXECUTION OF RUN()

        /****************************************************************************************/

        // SEEMS IT CAN ALSO GET IllegalThreadState EXCEPTION, DEPENDING ON THE TIMING...
            // BECAUSE WE CALLED START() ON A THREAD ALREADY EXECUTING RUN()
            // OR B/C WE CALLED START() ON A THREAD THAT ALREADY EXECUTED RUN()
                // I THINK IF THREAD 6 FILLS THE HASHMAP...
                    // THEN THREAD 8 FILLS IT (AND OVERWRITES WHAT THREAD 6 DID)...
                    // THEN THREAD 6 MAY ITERATE AND RUN() THREADS ADDED BY THREAD 8
                    // WHEN THREAD 8 TRIED TO ITERATE AND RUN(), WON'T WORK

    }
}

