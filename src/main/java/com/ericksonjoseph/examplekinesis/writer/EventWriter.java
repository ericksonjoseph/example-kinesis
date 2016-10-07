
package com.ericksonjoseph.examplekinesis.writer;

import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.ResourceNotFoundException;
import com.ericksonjoseph.examplekinesis.model.Event;
import com.ericksonjoseph.examplekinesis.utils.ConfigurationUtils;
import com.ericksonjoseph.examplekinesis.utils.CredentialUtils;

/**
 * Continuously sends simulated stock trades to Kinesis
 *
 */
public class EventWriter {

    private static final Log LOG = LogFactory.getLog(EventWriter.class);

    private static final long INTERVAL = 5000;

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage: " + EventWriter.class.getSimpleName()
                    + " <stream name> <region>");
            System.exit(1);
        }

        String streamName = args[0];
        String regionName = args[1];
        Region region = RegionUtils.getRegion(regionName);
        if (region == null) {
            System.err.println(regionName + " is not a valid AWS region.");
            System.exit(1);
        }

        AWSCredentials credentials = CredentialUtils.getCredentialsProvider().getCredentials();

        AmazonKinesis kinesisClient = new AmazonKinesisClient(credentials,
                ConfigurationUtils.getClientConfigWithUserAgent());
        kinesisClient.setRegion(region);

        // Validate that the stream exists and is active
        validateStream(kinesisClient, streamName);

        // Repeatedly send stock trades with a 100 milliseconds wait in between
        EventGenerator eventGenerator = new EventGenerator();
        while(true) {
            Event trade = eventGenerator.getRandomTrade();
            sendEvent(trade, kinesisClient, streamName);
            Thread.sleep(INTERVAL);
        }
    }

    /**
     * Checks if the stream exists and is active
     *
     * @param kinesisClient Amazon Kinesis client instance
     * @param streamName Name of stream
     */
    private static void validateStream(AmazonKinesis kinesisClient, String streamName) {
        try {
            DescribeStreamResult result = kinesisClient.describeStream(streamName);
            if(!"ACTIVE".equals(result.getStreamDescription().getStreamStatus())) {
                System.err.println("Stream " + streamName + " is not active. Please wait a few moments and try again.");
                System.exit(1);
            }
        } catch (ResourceNotFoundException e) {
            System.err.println("Stream " + streamName + " does not exist. Please create it in the console.");
            System.err.println(e);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error found while describing the stream " + streamName);
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Uses the Kinesis client to send an event to the given stream.
     *
     * @param event instance representing the event
     * @param kinesisClient Amazon Kinesis client
     * @param streamName Name of stream
     */
    private static void sendEvent(Event event, AmazonKinesis kinesisClient,
            String streamName) {
        byte[] bytes = event.toJsonAsBytes();
        // The bytes could be null if there is an issue with the JSON serialization by the Jackson JSON library.
        if (bytes == null) {
            LOG.warn("Could not get JSON bytes for stock event");
            return;
        }

        LOG.info("Putting event: " + event.toString());
        PutRecordRequest putRecord = new PutRecordRequest();
        putRecord.setStreamName(streamName);
        // We use the ticker symbol as the partition key, as explained in the tutorial.
        putRecord.setPartitionKey(event.getOrigin());
        putRecord.setData(ByteBuffer.wrap(bytes));

        try {
            kinesisClient.putRecord(putRecord);
        } catch (AmazonClientException ex) {
            LOG.warn("Error sending record to Amazon Kinesis.", ex);
        }
    }

}
