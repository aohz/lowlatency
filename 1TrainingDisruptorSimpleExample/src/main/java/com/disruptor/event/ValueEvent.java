package com.disruptor.event;

import com.lmax.disruptor.EventFactory;
import com.model.Value;

public class ValueEvent {

	private Value value;

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public static final EventFactory<ValueEvent> VALUE_EVENT_FACTORY = new EventFactory<ValueEvent>() {

		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};

}
