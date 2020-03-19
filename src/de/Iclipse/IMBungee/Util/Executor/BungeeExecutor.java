package de.Iclipse.IMBungee.Util.Executor;

import de.Iclipse.IMBungee.Data;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class BungeeExecutor extends IExecutor {

    @Override
    public Callback execute(final Runnable runnable, final Callback callback) {
        // BungeeCord hat keinen Mainthread
        runnable.run();
        callback.done();
        return callback;
    }

    @Override
    public Callback executeAsync(final Runnable runnable, final Callback callback) {
        ProxyServer.getInstance().getScheduler().runAsync(Data.instance, () -> {
            runnable.run();
            callback.done();
        });
        return callback;
    }

    @Override
    public Callback repeat(Consumer<Integer> consumer, int times, long interval, TimeUnit timeUnit, Callback callback) {
        //BungeeCord Scheduler ist immer Async
        return repeatAsync(consumer, times, interval, timeUnit, callback);
    }

    @Override
    public Callback repeatAsync(Consumer<Integer> consumer, int times, long interval, TimeUnit timeUnit, Callback callback) {
        TimerTask timer = new TimerTask();
        timer.task = ProxyServer.getInstance().getScheduler().schedule(Data.instance, () -> {
            if (times == timer.count) {
                timer.cancelTask();
                callback.done();
                return;
            }

            consumer.accept(timer.count);
            timer.countUp();
        }, 0, interval, timeUnit);
        return callback;
    }

    @Override
    public Callback executeLater(Runnable runnable, long l, TimeUnit timeUnit, Callback callback) {
        //BungeeCord Scheduler ist immer Async
        return executeLater(runnable, l, timeUnit, callback);
    }

    @Override
    public Callback executeLaterAsync(Runnable runnable, long l, TimeUnit timeUnit, Callback callback) {
        ProxyServer.getInstance().getScheduler().schedule(Data.instance, () -> {
            runnable.run();
            callback.done();
        }, l, timeUnit);
        return callback;
    }

    @Override
    public Callback executeResult(final ResultRunnable runnable, final Callback callback) {
        // BungeeCord hat keinen Mainthread
        runnable.run();
        if (runnable.hadSuccess())
            callback.done();
        return callback;
    }

    @Override
    public Callback executeResultAsync(final ResultRunnable runnable, final Callback callback) {
        ProxyServer.getInstance().getScheduler().runAsync(Data.instance, () -> {
            runnable.run();
            if (runnable.hadSuccess())
                callback.done();
        });
        return callback;
    }

    @Override
    public boolean isAsyncThread() {
        return !Thread.currentThread().getName().contains("Netty");
    }

    private class TimerTask {
        public ScheduledTask task;
        public int count;

        public void countUp() {
            count += 1;
        }

        public void cancelTask() {
            task.cancel();
        }
    }
}