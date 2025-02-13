/*
 * MIT License
 *
 * Copyright (c) 2020 Mark
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.andavin.util;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

/**
 * A class to make using the {@link BukkitScheduler} less
 * cumbersome and easier to use. Also, allows for easy
 * {@code do-while} loops and similar condition based looping
 * in timed loop tasks.
 *
 * @author Andavin
 * @since February 9, 2018
 */
@SuppressWarnings("UnusedReturnValue")
public final class Scheduler {

    private static FoliaLib foliaLib;

    /**
     * Run a task synchronously on the main thread using
     * the {@link BukkitScheduler}.
     *
     * @param run The {@link Runnable} to execute.
     * @return The {@link BukkitTask} that is returned after registering the task.
     */
    public static WrappedTask sync(Runnable run) {
        return foliaLib.getScheduler().runLater(run, 0);
    }

    /**
     * Run a task asynchronously on a separate thread using
     * the {@link BukkitScheduler}.
     *
     * @param run The {@link Runnable} to execute.
     * @return The {@link BukkitTask} that is returned after registering the task.
     */
    public static WrappedTask async(Runnable run) {
        return foliaLib.getScheduler().runLaterAsync(run, 0);
    }

    /**
     * Run a task synchronously after a specified amount of
     * ticks using the {@link BukkitScheduler}.
     *
     * @param run The {@link Runnable} task to execute.
     * @param delay The ticks (1 tick = 50 milliseconds, 20 ticks = 1 second) after which to run the task.
     * @return The {@link BukkitTask} that is returned after registering the task.
     */
    public static WrappedTask later(Runnable run, long delay) {
        return foliaLib.getScheduler().runLater(run, delay);
    }

    /**
     * Run a task asynchronously after a specified amount of
     * ticks using the {@link BukkitScheduler}.
     *
     * @param run The {@link Runnable} task to execute.
     * @param delay The ticks (1 tick = 50 milliseconds, 20 ticks = 1 second) after which to run the task.
     * @return The {@link BukkitTask} that is returned after registering the task.
     */
    public static WrappedTask laterAsync(Runnable run, long delay) {
        return foliaLib.getScheduler().runLaterAsync(run, delay);
    }

    /**
     * Run a task synchronously repeatedly after a specified amount
     * of ticks and repeated every period ticks until cancelled.
     *
     * @param run The {@link Runnable} task to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @return The {@link BukkitTask} that is returned after registering the task.
     */
    public static WrappedTask repeat(Runnable run, long delay, long period) {
        return foliaLib.getScheduler().runTimer(run, delay, period);
    }

    /**
     * Run a task asynchronously repeatedly after a specified amount
     * of ticks and repeated every period ticks until canceled.
     *
     * @param run The {@link Runnable} task to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @return The {@link BukkitTask} that is returned after registering the task.
     */
    public static WrappedTask repeatAsync(Runnable run, long delay, long period) {
        return foliaLib.getScheduler().runTimerAsync(run, delay, period);
    }

    /**
     * Run a task synchronously repeatedly until the condition is met
     * at which point it will be cancelled.
     * <p>
     * In behavior towards the condition, this is identical to an inverted
     * {@code while} loop. If the condition is {@code true} to begin with
     * or by the time that {@code delay} has elapsed, then the task will
     * never be executed and will immediately be cancelled on the first run.
     *
     * @param run The {@link Runnable} task to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @param until The {@link Supplier} to test when to cancel. When this is
     *              {@code true} the task will be cancelled.
     * @return The {@link BukkitTask task} that was scheduled.
     */
    public static WrappedTask repeatUntil(Runnable run, long delay, long period, BooleanSupplier until) {
        return repeatWhile(run, delay, period, () -> !until.getAsBoolean());
    }

    /**
     * Run a task asynchronously repeatedly until the condition is met
     * at which point it will be cancelled.
     * <p>
     * In behavior towards the condition, this is identical to an inverted
     * {@code while} loop. If the condition is {@code true} to begin with
     * or by the time that {@code delay} has elapsed, then the task will
     * never be executed and will immediately be cancelled on the first run.
     *
     * @param run The {@link Runnable} task to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @param until The {@link Supplier} to test when to cancel.
     *         When this returns {@code true} the task will be cancelled.
     * @return The {@link BukkitTask task} that was scheduled.
     */
    public static WrappedTask repeatAsyncUntil(Runnable run, long delay, long period, BooleanSupplier until) {
        return repeatAsyncWhile(run, delay, period, () -> !until.getAsBoolean());
    }

    /**
     * Run a task repeatedly (every period of ticks) as long as the condition
     * met. Once the condition is no longer met (returns {@code false}) the
     * loop will end.
     * <p>
     * In behavior towards the condition, this is identical to a {@code while}
     * loop. If the condition is {@code false} to begin with or by the time
     * that {@code delay} has elapsed, then the task will never be executed and
     * will immediately be cancelled on the first run.
     *
     * @param run The {@link Runnable} task to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @param condition The {@link Supplier condition} that must be {@code true}
     *                  in order for the task to continue to run.
     * @return The {@link BukkitTask task} that was scheduled.
     */
    public static WrappedTask repeatWhile(Runnable run, long delay, long period, BooleanSupplier condition) {
        Task task = new Task(run, condition);
        WrappedTask bukkitTask = repeat(task, delay, period);
        task.setTask(bukkitTask);
        return bukkitTask;
    }

    /**
     * Run a task asynchronously repeatedly (every period of ticks) as long
     * as the condition met. Once the condition is no longer met (returns
     * {@code false}) the loop will end.
     * <p>
     * In behavior towards the condition, this is identical to a {@code while}
     * loop. If the condition is {@code false} to begin with or by the time
     * that {@code delay} has elapsed, then the task will never be executed and
     * will immediately be cancelled on the first run.
     *
     * @param run The {@link Runnable} task to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @param condition The {@link Supplier condition} that must be {@code true}
     *                  in order for the task to continue to run.
     * @return The {@link BukkitTask task} that was scheduled.
     */
    public static WrappedTask repeatAsyncWhile(Runnable run, long delay, long period, BooleanSupplier condition) {
        Task task = new Task(run, condition);
        WrappedTask bukkitTask = repeatAsync(task, delay, period);
        task.setTask(bukkitTask);
        return bukkitTask;
    }

    /**
     * Run a task repeatedly (every period of ticks) until the specified
     * amount of executions have taken place.
     * <p>
     * This is, conditionally, almost identical to a {@code fori} loop:
     * <pre>
     *     for (int i = 0; i &lt; count; i++) {
     *         // The runnable contents
     *     }
     * </pre>
     *
     * @param consumer The {@link Consumer task} to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @param count The amount of executions to allow before cancelling.
     * @return The {@link BukkitTask task} that was scheduled.
     */
    public static WrappedTask repeatFor(IntConsumer consumer, long delay, long period, int count) {
        Task task = new IncrementTask(consumer, count);
        WrappedTask bukkitTask = repeat(task, delay, period);
        task.setTask(bukkitTask);
        return bukkitTask;
    }

    /**
     * Run a task repeatedly (every period of ticks) until the specified
     * amount of executions have taken place.
     * <p>
     * This is, conditionally, almost identical to a {@code fori} loop:
     * <pre>
     *     for (int i = 0; i &lt; count; i++) {
     *         // The runnable contents
     *     }
     * </pre>
     *
     * @param consumer The {@link Consumer task} to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @param count The amount of executions to allow before cancelling.
     * @return The {@link BukkitTask task} that was scheduled.
     */
    public static WrappedTask repeatAsyncFor(IntConsumer consumer, long delay, long period, int count) {
        Task task = new IncrementTask(consumer, count);
        WrappedTask bukkitTask = repeatAsync(task, delay, period);
        task.setTask(bukkitTask);
        return bukkitTask;
    }

    /**
     * Run a task repeatedly (every period of ticks) until the specified
     * amount of time has elapsed.
     * <p>
     * Timing precision is in milliseconds, however, the task is not guaranteed
     * to run a specific amount of times due to server lag.<pre>1000ms ≈ 20 ticks</pre>
     * If the duration given was 4 seconds and the task is supposed to run every
     * 5 ticks, then it should run about 16 times ({@code 20 / 5 * 4}), however,
     * this is not guaranteed and it can be any amount from 0 to 16 times
     * depending on server lag.
     * <br>
     * This would be guaranteed not to exceed 16 executions, though, since TPS
     * will never be above 20.
     *
     * @param run The {@link Runnable} task to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @param duration The amount of {@link TimeUnit units} to run for.
     * @param unit The {@link TimeUnit} to multiply the duration by.
     * @return The {@link BukkitTask task} that was scheduled.
     */
    public static WrappedTask repeatFor(Runnable run, long delay, long period, long duration, TimeUnit unit) {
        long until = unit.toMillis(duration);
        Task task = new Task(run, () -> System.currentTimeMillis() < until);
        WrappedTask bukkitTask = repeat(task, delay, period);
        task.setTask(bukkitTask);
        return bukkitTask;
    }

    /**
     * Run a task asynchronously repeatedly (every period of ticks) until
     * the specified amount of time has elapsed.
     * <p>
     * Timing precision is in milliseconds, however, the task is not guaranteed
     * to run a specific amount of time due to server lag.<pre>1000ms ≈ 20 ticks</pre>
     * If the duration given was 4 seconds and the task is supposed to run every
     * 5 ticks, then it should run about 16 times ({@code 20 / 5 * 4}), however,
     * this is not guaranteed and it can be any amount from 0 to 16 times
     * depending on server lag.
     * <br>
     * This would be guaranteed not to exceed 16 executions, though, since TPS
     * will never be above 20.
     *
     * @param run The {@link Runnable} task to execute every period.
     * @param delay The delay in ticks before the first run of the task.
     * @param period The period in ticks to wait until running again after each run.
     * @param duration The amount of {@link TimeUnit units} to run for.
     * @param unit The {@link TimeUnit} to multiply the duration by.
     * @return The {@link BukkitTask task} that was scheduled.
     */
    public static WrappedTask repeatAsyncFor(Runnable run, long delay, long period, long duration, TimeUnit unit) {
        long until = unit.toMillis(duration);
        Task task = new Task(run, () -> System.currentTimeMillis() < until);
        WrappedTask bukkitTask = repeatAsync(task, delay, period);
        task.setTask(bukkitTask);
        return bukkitTask;
    }

    private static class Task implements Runnable {

        WrappedTask task;
        boolean cancelled;
        private final Runnable runnable;
        private final BooleanSupplier condition;

        Task(Runnable runnable, BooleanSupplier condition) {
            this.runnable = runnable;
            this.condition = condition;
        }

        /**
         * The {@link BukkitTask} that represents the
         * task that is being run.
         *
         * @param task The task.
         */
        final void setTask(WrappedTask task) {
            this.task = task;
        }

        @Override
        public void run() {

            if (this.cancelled) {

                if (this.task != null) {
                    this.task.cancel();
                }

                return;
            }

            if (this.condition.getAsBoolean()) {
                this.runnable.run();
            } else {

                if (this.task != null) {
                    this.task.cancel();
                } else {
                    this.cancelled = true;
                }
            }
        }
    }

    private static class IncrementTask extends Task implements Runnable {

        private final int maxCount;
        private final IntConsumer consumer;
        private final AtomicInteger count = new AtomicInteger();

        IncrementTask(IntConsumer consumer, int maxCount) {
            super(null, null);
            this.maxCount = maxCount;
            this.consumer = consumer;
        }

        @Override
        public void run() {

            if (this.cancelled) {

                if (this.task != null) {
                    this.task.cancel();
                }

                return;
            }

            int count = this.count.getAndIncrement();
            if (count < this.maxCount) {
                this.consumer.accept(count);
            } else {

                if (this.task != null) {
                    this.task.cancel();
                } else {
                    this.cancelled = true;
                }
            }
        }
    }
}
