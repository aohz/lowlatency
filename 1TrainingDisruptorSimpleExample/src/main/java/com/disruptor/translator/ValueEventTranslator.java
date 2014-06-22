package com.disruptor.translator;

import com.disruptor.event.ValueEvent;
import com.lmax.disruptor.EventTranslator;
import com.model.Value;

public class ValueEventTranslator implements EventTranslator<ValueEvent> {

	private Value value;

	public ValueEventTranslator(Value value) {
		this.value = value;
	}

	public void translateTo(ValueEvent event, long sequence) {
		event.setValue(value);
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

}
