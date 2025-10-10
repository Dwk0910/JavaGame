package org.neatore.javagame.util;

public class DelayedTask {
    private long lastTime;
    private final Runnable runnable;
    private boolean isStarted = false;

    public DelayedTask(Runnable runnable) {
        this.runnable = runnable;
        this.lastTime = System.currentTimeMillis();
    }

    public void delayStart(long req_time_ms, boolean loop) {
        if (System.currentTimeMillis() - lastTime > req_time_ms) {
            if (loop || !isStarted) {
                isStarted = true;
                runnable.run();
                if (!loop) this.lastTime = System.currentTimeMillis();
            }
        }
    }
}
