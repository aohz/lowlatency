package benchmark;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 50, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(4)
@Fork(2)
public class AtomicCounterBenchmarks {

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(
						".*" + AtomicCounterBenchmarks.class.getSimpleName()
								+ ".*")
				.threads(Runtime.getRuntime().availableProcessors()).build();

		new Runner(opt).run();
	}

	@State(Scope.Benchmark)
	public static class DetailsBenchmark {
		Details details = new Details();
	}

	@State(Scope.Benchmark)
	public static class AtomicCounterBenchmark {
		AtomicCounter counter = new AtomicCounter();
	}

	@State(Scope.Benchmark)
	public static class SynchronizedCounterBenchmark {
		SynchronizedCounter counter = new SynchronizedCounter();
	}

	@Benchmark
	@Group("atomicIntegerFieldUpdaterCounter")
	public void benchmarkAtomicIntegerFieldUpdaterCounter_update(
			Blackhole hole, DetailsBenchmark detailsBenchmark)
			throws InterruptedException {
		hole.consume(AtomicIntegerFieldUpdaterCounter
				.getNextValue(detailsBenchmark.details));
		Thread.yield();
		hole.consume(AtomicIntegerFieldUpdaterCounter
				.getPreviousValue(detailsBenchmark.details));
	}

	@Benchmark
	@Group("atomicIntegerFieldUpdaterCounter")
	public void benchmarkAtomicIntegerFieldUpdaterCounter_read(Blackhole hole,
			DetailsBenchmark detailsBenchmark) throws InterruptedException {
		hole.consume(AtomicIntegerFieldUpdaterCounter
				.getValue(detailsBenchmark.details));
		Thread.yield();
	}

	@Benchmark
	@Group("atomicCounter")
	public void benchmarkAtomicCounter_update(Blackhole hole,
			AtomicCounterBenchmark atomicCounterBenchmark)
			throws InterruptedException {
		hole.consume(atomicCounterBenchmark.counter.getNextValue());
		Thread.yield();
		hole.consume(atomicCounterBenchmark.counter.getPreviousValue());
	}

	@Benchmark
	@Group("atomicCounter")
	public void benchmarkAtomicCounter_read(Blackhole hole,
			AtomicCounterBenchmark atomicCounterBenchmark)
			throws InterruptedException {
		hole.consume(atomicCounterBenchmark.counter.getValue());
		Thread.yield();
	}

	@Benchmark
	@Group("synchronizedCounter")
	public void benchmarkSynchronizedCounter_update(Blackhole hole,
			SynchronizedCounterBenchmark synchronizedCounterBenchmark)
			throws InterruptedException {
		hole.consume(synchronizedCounterBenchmark.counter.getNextValue());
		Thread.yield();
		hole.consume(synchronizedCounterBenchmark.counter.getPreviousValue());
	}

	@Benchmark
	@Group("synchronizedCounter")
	public void benchmarkSynchronizedCounter_read(Blackhole hole,
			SynchronizedCounterBenchmark synchronizedCounterBenchmark)
			throws InterruptedException {
		hole.consume(synchronizedCounterBenchmark.counter.getValue());
		Thread.yield();
	}
}

class AtomicIntegerFieldUpdaterCounter {

	private static final AtomicIntegerFieldUpdater<Details> updater = AtomicIntegerFieldUpdater
			.newUpdater(Details.class, "value");

	public static int getNextValue(Details details) {
		return updater.incrementAndGet(details);
	}

	public static int getPreviousValue(Details details) {
		return updater.decrementAndGet(details);
	}

	public static void setValue(Details details, int newValue) {
		updater.set(details, newValue);
	}

	public static int getValue(Details details) {
		return updater.get(details);
	}

}

class Details {
	// if not volatile, an IllegalArgumentException("Must be volatile type") is
	// thrown
	volatile int value;
}

class SynchronizedCounter {

	private int value;

	public synchronized int getValue() {
		return value;
	}

	public synchronized int getNextValue() {
		return ++value;
	}

	public synchronized int getPreviousValue() {
		return --value;
	}

	public synchronized void setValue(int value) {
		this.value = value;
	}

}

class AtomicCounter {

	private final AtomicInteger value = new AtomicInteger(0);

	public int getValue() {
		return value.get();
	}

	public int getNextValue() {
		return value.incrementAndGet();
	}

	public int getPreviousValue() {
		return value.decrementAndGet();
	}

	public void setValue(int value) {
		this.value.set(value);
	}
}