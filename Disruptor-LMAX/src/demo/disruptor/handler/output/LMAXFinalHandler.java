package demo.disruptor.handler.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LifecycleAware;

import demo.disruptor.event.LMAXEvent;

public class LMAXFinalHandler implements EventHandler<LMAXEvent>,
		LifecycleAware {
	private final List<String> eventMsgs = new ArrayList<String>();
	private final BufferedWriter fwriter;
	private final CountDownLatch latch;

	public LMAXFinalHandler(final File outFile, final CountDownLatch latch) {
		this.latch = latch;
		try {
			fwriter = new BufferedWriter(new FileWriter(outFile));

		} catch (Exception e) {
			System.err
					.println("ERROR: Unable to open FileWriter in LMAXFinalHandler");
			throw new RuntimeException(e);
		}
	}

	public void onEvent(LMAXEvent event, long sequence, boolean endOfBatch) {
		// modify the event in place
		event.incrementMarshallCount();

		System.out.println(event);
		latch.countDown();
	}

	private String joinMessages() {
		final StringBuilder sb = new StringBuilder();
		for (String s : eventMsgs) {
			sb.append(s).append("\n");
		}
		return sb.toString();
	}

	private void writeToFile(String s) {
		try {
			fwriter.write(s);
			fwriter.newLine();
			fwriter.flush();

		} catch (Exception e) {
			System.out.println("ERROR: unable to write to output file: "
					+ e.toString());
			e.printStackTrace();
		}
	}

	/* ---[ LifecycleAware Methods ]--- */

	public void onStart() {
		System.out.println("LifeCycleAware in FinalHandler: onStart called");
		System.out.flush();
	}

	public void onShutdown() {
		System.out.println("LifeCycleAware in FinalHandler: onShutdown called");
		System.out.flush();
		if (fwriter != null) {
			try {
				fwriter.close();
			} catch (Exception e) {
				System.err
						.println("ERROR: Unable to close FileWriter in LMAXFinalHandler: "
								+ e.toString());
			}
		}
	}
}
