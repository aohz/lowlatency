package demo.disruptor.handler.input;

import com.lmax.disruptor.EventHandler;

import demo.disruptor.event.LMAXEvent;

public class LMAXReplicator implements EventHandler<LMAXEvent> {
  public void onEvent(LMAXEvent event, long sequence, boolean endOfBatch) {
    // // DEBUG
    // System.out.printf("LMAXReplicator: seq: %d endOfBatch: %s\n", sequence, endOfBatch);
    // System.out.flush();
    // // END DEBUG

    // simulate a slight delay due to IO
    try { Thread.sleep(5); } catch (Exception e) {};

    int pos = 1;
    if (event.getUnmarshalledMessage() != null) {
      pos++;
    }
    if (event.getJournalMessage() != null) {
      pos++;
    }
    // modify the event in place
    //OK because we know that no other sequencial handler is using this field
    event.setReplicatedMessage("Replicated. Pos: " + pos);
  }
}
