
package com.ericksonjoseph.examplekinesis.processor;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;

/**
 * Used to create new stock trade record processors.
 *
 */
public class EventRecordProcessorFactory implements IRecordProcessorFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public IRecordProcessor createProcessor() {
        return new EventRecordProcessor();
    }

}
