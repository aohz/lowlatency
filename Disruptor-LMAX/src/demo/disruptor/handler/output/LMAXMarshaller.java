package demo.disruptor.handler.output;

import com.lmax.disruptor.EventHandler;

import demo.disruptor.event.LMAXEvent;

public class LMAXMarshaller implements EventHandler<LMAXEvent> {
  public void onEvent(LMAXEvent event, long sequence, boolean endOfBatch) {
    // modify the event in place
	//OK because we know that no other sequencial handler is using this field
    event.incrementMarshallCount();
  }
}
