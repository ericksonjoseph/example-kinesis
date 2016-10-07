
package com.ericksonjoseph.examplekinesis.database;

import java.util.Map;
import java.util.HashMap;

public interface DatabaseInterface {

    public Map<String, Object> getRecord(String entity, String id);

    public boolean saveRecord(String entity, String id, HashMap record);

    public boolean updateDocument(String entity, String id, HashMap document);

}
