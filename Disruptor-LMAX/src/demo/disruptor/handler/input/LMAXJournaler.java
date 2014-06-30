package demo.disruptor.handler.input;

import com.lmax.disruptor.EventHandler;
import demo.disruptor.event.LMAXEvent;

public class LMAXJournaler implements EventHandler<LMAXEvent> {
  public void onEvent(LMAXEvent event, long sequence, boolean endOfBatch) {
    // simulate a slight delay due to IO
    // // DEBUG
    // System.out.printf("LMAXJournaler: seq: %d endOfBatch: %s\n", sequence, endOfBatch);
    // System.out.flush();
    // // END DEBUG
    try { Thread.sleep(5); } catch (Exception e) {};

    int pos = 1;
    if (event.getReplicatedMessage() != null) {
      pos++;
    }
    if (event.getUnmarshalledMessage() != null) {
      pos++;
    }
    // modify the event in place
    //OK because we know that no other sequencial handler is using this field
    event.setJournalMessage("Journaled. Pos: " + pos);
  }
}
