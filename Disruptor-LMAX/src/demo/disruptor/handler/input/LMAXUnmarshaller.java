package demo.disruptor.handler.input;

import com.lmax.disruptor.EventHandler;

import demo.disruptor.event.LMAXEvent;

public class LMAXUnmarshaller implements EventHandler<LMAXEvent> {
  public void onEvent(LMAXEvent event, long sequence, boolean endOfBatch) {
    // // DEBUG
    // System.out.printf("LMAXUnmarshaller: seq: %d endOfBatch: %s\n", sequence, endOfBatch);
    // System.out.flush();
    // // END DEBUG
    int pos = 1;
    if (event.getReplicatedMessage() != null) {
      pos++;
    }
    if (event.getJournalMessage() != null) {
      pos++;
    }
    // modify the event in place
    //	OK because we know that no other sequencial handler is using this field
    event.setUnmarshalledMessage("Unmarshalled. Pos: " + pos);
  }
}
