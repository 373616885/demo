package com.example.disruptor.demo.producer;

import com.example.disruptor.demo.model.NotifyEvent;
import com.lmax.disruptor.EventTranslatorVararg;
import com.lmax.disruptor.RingBuffer;

/**
 * @author qinjp
 * @date 2019-07-05
 **/
public class EventProducer {

    private final static EventTranslatorVararg<NotifyEvent> translator = new EventTranslatorVararg<NotifyEvent>() {
        @Override
        public void translateTo(NotifyEvent notifyEvent, long seq, Object... objs) {
            notifyEvent.setSeq(seq);
            notifyEvent.setMessage(String.valueOf(objs[0]));
        }
    };

    private final RingBuffer<NotifyEvent> ringBuffer;

    public EventProducer(RingBuffer<NotifyEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void publish(String msg){
        this.ringBuffer.publishEvent(translator, msg);
    }
}
