package template.main;

public class ThreadStackSize {
    public static void main(String[] args) throws Exception {
        // 设置栈大小为100MB
        Thread thread = new Thread(null, new RunnableTask(), "CustomThread", 1024 * 1024 * 100);
        thread.start();
    }

    private static class RunnableTask implements Runnable {
        @Override
        public void run() {
        }
    }
}
