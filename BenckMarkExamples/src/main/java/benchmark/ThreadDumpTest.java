package benchmark;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 3, timeUnit = TimeUnit.SECONDS)
@Threads(2)
@State(Scope.Thread)
public class ThreadDumpTest {
	private static long SLEEP = 10;

	boolean lockedMonitors = false;
	boolean lockedSynchronizers = false;

	double val = Math.PI;

	@Benchmark
	public double testSine() {
		return Math.sin(val);
	}

	@Group("jmh")
	@GroupThreads(1)
	@Benchmark
	public ThreadInfo[] testJmhWay() throws InterruptedException {
		Thread.sleep(SLEEP);
		return ManagementFactory.getThreadMXBean().dumpAllThreads(
				lockedMonitors, lockedSynchronizers);
	}

	@Group("jmh")
	@GroupThreads(1)
	@Benchmark
	public double sine1() {
		return Math.sin(val);
	}

	@Group("jdk")
	@GroupThreads(1)
	@Benchmark
	public Map<Thread, StackTraceElement[]> testCoreWay()
			throws InterruptedException {
		Thread.sleep(SLEEP);
		return Thread.getAllStackTraces();
	}

	@Group("jdk")
	@GroupThreads(1)
	@Benchmark
	public double sine2() {
		return Math.sin(val);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + FalseCacheSharingBenchmark.class.getSimpleName() + ".*").addProfiler(StackProfiler.class)
				.threads(Runtime.getRuntime().availableProcessors()).build();

		new Runner(opt).run();
	}
}