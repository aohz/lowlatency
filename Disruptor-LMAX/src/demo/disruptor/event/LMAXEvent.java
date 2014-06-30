package demo.disruptor.event;

import com.lmax.disruptor.EventFactory;

public class LMAXEvent {

  // EventFactory
  public static final EventFactory<LMAXEvent> FACTORY = new LMAXEventFactory();

  //Fields to be set during processing
  //each handler will set its field
  //these values will be available for business logic.  
  private String journalMessage;
  private String replicatedMessage;
  private String unmarshalledMessage;
  
  private long id_pushedOnto1 = -1;
  private long id_pulledOff1  = -1;
  private long id_pushedOnto2 = -1;

  private int marshalledCount;

  /**
   * Copy contents from one Event into this one
   */
  public void copy(final LMAXEvent other) {
    this.id_pushedOnto1 = other.id_pushedOnto1;
    this.id_pulledOff1  = other.id_pulledOff1;
    this.id_pushedOnto2 = other.id_pushedOnto2;

    this.journalMessage      = other.journalMessage;
    this.replicatedMessage   = other.replicatedMessage;
    this.unmarshalledMessage = other.unmarshalledMessage;

    this.marshalledCount = other.marshalledCount;    
  }


  /* ---[ GETTERS and SETTERS ]--- */
  public long getPushedOnto1Id() {
    return id_pushedOnto1;
  }

  public void setPushedOnto1Id(long id_pushedOnto1) {
    this.id_pushedOnto1 = id_pushedOnto1;
  }

  public long getPulledOff1Id() {
    return id_pulledOff1;
  }

  public void setPulledOff1Id(long id_pulledOff1) {
    this.id_pulledOff1 = id_pulledOff1;
  }

  public long getPushedOnto2Id() {
    return id_pushedOnto2;
  }

  public void setPushedOnto2Id(long id_pushedOnto2) {
    this.id_pushedOnto2 = id_pushedOnto2;
  }

  public String getJournalMessage() {
    return journalMessage;
  }

  public void setJournalMessage(String journalMessage) {
    this.journalMessage = journalMessage;
  }

  public String getReplicatedMessage() {
    return replicatedMessage;
  }

  public void setReplicatedMessage(String replicatedMessage) {
    this.replicatedMessage = replicatedMessage;
  }

  public String getUnmarshalledMessage() {
    return unmarshalledMessage;
  }

  public void setUnmarshalledMessage(String unmarshalledMessage) {
    this.unmarshalledMessage = unmarshalledMessage;
  }

  public int getMarshalledCount() {
    return marshalledCount;
  }

  public void incrementMarshallCount() {	
    this.marshalledCount++;
  }  

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("LMAXEvent:");
    sb.append("\n  PushedOn1Id      : ").append(id_pushedOnto1);
    sb.append("\n  PulledOff1Id     : ").append(id_pulledOff1);
    sb.append("\n  PushedOn2Id      : ").append(id_pushedOnto2);
    sb.append("\n  JournalMsg       : ").append(journalMessage);
    sb.append("\n  ReplicatedMsg    : ").append(replicatedMessage);
    sb.append("\n  UnmarshalledMesg : ").append(unmarshalledMessage);
    sb.append("\n  Was re-marshalled: ").append(marshalledCount > 1);
    return sb.toString();
  }


  /* ---[ FACTORY ]--- */

  public static class LMAXEventFactory implements EventFactory<LMAXEvent> {
    public LMAXEvent newInstance() {
      return new LMAXEvent();
    }
  }

}
