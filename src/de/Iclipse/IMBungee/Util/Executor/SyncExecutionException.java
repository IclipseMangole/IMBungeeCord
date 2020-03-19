package de.Iclipse.IMBungee.Util.Executor;

public class SyncExecutionException extends Exception {
    private final String info;

    public SyncExecutionException(String info) {
        this.info = info;
    }

    public void printInfo() {
        System.out.println("[IM] Reporting... " + info + " Stack Trace is next");
        printStackTrace();
        System.out.println("[IM] Finished reporting");
    }
}
