
package com.ericksonjoseph.examplekinesis.database;

import com.arangodb.ArangoConfigure;
import com.arangodb.ArangoDriver;
import com.arangodb.ArangoHost;
import com.arangodb.ArangoException;
import com.arangodb.CursorResultSet;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.DocumentCursor;
import com.arangodb.util.MapBuilder;

import com.ericksonjoseph.examplekinesis.queue.QueueInterface;
import com.ericksonjoseph.examplekinesis.model.JobDocument;
import com.ericksonjoseph.examplekinesis.model.Event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;

public class Arango implements DatabaseInterface, QueueInterface {

    // @TODO get from config
    public static final String HOST = "db";

    private static final Log LOG = LogFactory.getLog(Arango.class);

    private ArangoDriver connection;

    public Arango() {
        this.connect();
    }

    private void connect() {

        if (null == this.connection) {

            LOG.info("Connecting to Database");

            // Initialize configure
            ArangoConfigure configure = new ArangoConfigure();
            configure.setArangoHost(new ArangoHost(HOST, 8529));
            configure.init();

            // Create Driver (this instance is thread-safe)
            ArangoDriver arangoDriver = new ArangoDriver(configure);
            arangoDriver.setDefaultDatabase("business");

            this.connection = arangoDriver;
        }
    }

    public int push(String queue, Event data) {

        this.connect();
        
        String entity = "queues";
        String doc = "document(\"" + entity + "/" + queue + "\")";

        String query = "update " + doc + " with { queue: append(" + doc + ".queue, " + data + ") } in " + entity;
        query += " let x = NEW";
        query += " return count(x.queue)";

        try {
            DocumentCursor<String> documentCursor = this.connection.executeDocumentQuery(
                    query, null, this.connection.getDefaultAqlQueryOptions(), String.class);

            for (DocumentEntity<String> documentEntity : documentCursor.asList()) {
                String obj = documentEntity.getEntity();
            }
        } catch (ArangoException e) {
            System.out.println("Could not execute Arango Query");
            System.out.println(e.getMessage());
        }

        // @TODO return new length
        return 0;
    }

    public Event pop(String queue) {

        this.connect();

        String query = "update document(@id) with { queue: shift(document(@id).queue) } in queues let x = OLD return slice(x.queue, 0, 1)[0]";

        Map<String, Object> bindVars = new HashMap();
        bindVars.put("id", "queues/" + queue);

        Event result = null;

        try {
            DocumentCursor<Event> documentCursor = this.connection.executeDocumentQuery(
                    query, bindVars, this.connection.getDefaultAqlQueryOptions(), Event.class);

            for (DocumentEntity<Event> documentEntity : documentCursor.asList()) {
                if (documentEntity == null) {
                    continue;
                }

                Event obj = documentEntity.getEntity();
                result = obj;
            }
        } catch (ArangoException e) {
            System.out.println("Could not execute Arango pop Query");
            System.out.println(e.getMessage());
        }

        return result;
    }

    public boolean saveRecord(String entity, String key, HashMap record) {

        //LOG.info("Saving Document to " + entity);

        this.connect();

        BaseDocument document = hashMapToDocument(record);
        document.setDocumentKey(key);

        try {
            this.connection.createDocument(entity, document);
        } catch (ArangoException e) {
            LOG.error("Could not save document" + e.getMessage());
            return false;
        }

        return true;
    }

    public Map<String, Object> getRecord(String entity, String id) {

        //LOG.info("Reading Document from " + entity);

        this.connect();

        DocumentEntity<BaseDocument> jobDocument = null;

        try {
            jobDocument = this.connection.getDocument(entity, id, BaseDocument.class);
            return jobDocument.getEntity().getProperties();
        } catch (ArangoException e) {
            LOG.info("ArangoException reading document: " + e.getMessage());
            return null;
        }
    }

    public boolean updateDocument(String entity, String id, HashMap updates) {

        //LOG.info("Updating Document from " + entity);

        this.connect();

        BaseDocument document = hashMapToDocument(updates);
        document.setDocumentKey(id);

        try {
            this.connection.updateDocument(entity, id, document);            
        } catch (ArangoException e) {
            System.out.println("Failed to update document. " + e.getMessage());
            return false;
        }

        return true;
    }

    private BaseDocument hashMapToDocument(HashMap record) {

        Set<Map.Entry<String, Object>> set = record.entrySet();

        BaseDocument document = new BaseDocument(); 
        for ( Map.Entry<String, Object> rec : set) {
            document.addAttribute(rec.getKey(), rec.getValue());
        }

        return document;
    }
}
