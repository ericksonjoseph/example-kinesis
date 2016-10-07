
package com.ericksonjoseph.examplekinesis.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;

/**
 * DBDocument
 *
 * Represents a document that can be stored in our database.
 * Call the toHashMap method to get a map of of all the fields defined in this schema for this class.
 */
public abstract class DBDocument {

    private static final Log LOG = LogFactory.getLog(DBDocument.class);

    abstract public HashMap<String, String> getSchema();

    public HashMap toHashMap() {

        HashMap map = new HashMap();

        Class this_class = this.getClass();
        HashMap<String, String> schema = this.getSchema();

        for (Map.Entry<String, String> field : schema.entrySet()) {

            try {
                Field f = this_class.getDeclaredField(field.getKey());
                map.put(f.getName(), f.get(this));
            } catch (NoSuchFieldException e) {
                // Happens if we defined a field in the schema that does not exist on the class
                LOG.error("We defined a field in the schema that does not exist on the class");
                continue;
            } catch (IllegalAccessException e) {
                LOG.error("Failed to set field on hashMap " + this_class.toString());
                continue;
            }
        }

        return map;
    }
}
