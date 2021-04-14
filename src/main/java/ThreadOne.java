public class ThreadOne implements Runnable {
    String name;
    Thread t;
    Boolean flag;

    ThreadOne(String threadname) {
        name = threadname;
        t = new Thread(this, name);
        System.out.println("New thread: " + t);
        t.start();
    }

    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Thread " + name + ": " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + name + "Interrupted");
        }
        System.out.println("Thread " + name + " exiting.");
    }
}


class MultiThread {
    public static void main(String args[]) {
        Experiment("One", 10000);
        Experiment("Two", 5000);
        Experiment("Three", 500);
    }

    private static void Experiment(String name, Integer limit) {
        System.out.println("\n\nExperiment "+name);
        new ThreadOne("One");
        new ThreadOne("Two");
        new ThreadOne("Three");
        try {
            System.out.println("THREAD " + Thread.currentThread().getName() + " WILL BE PUT TO SLEEP FOR " + limit + " MS.");
            Thread.sleep(limit);
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
        System.out.println("Main thread exiting.");
    }
}
