package com.infinispan.demo.model;

/**
 * Represents a CRUD op for a given map entry
 */
public class CRUDOperation {

    private TYPE type;

    private Integer key;
    private DemoMapEntry mapEntry;
    public CRUDOperation(TYPE type, Integer key) {
        this.type = type;
        this.key = key;
    }

    public CRUDOperation(TYPE type, Integer key, DemoMapEntry mapEntry) {
        this.type = type;
        this.key = key;
        this.mapEntry = mapEntry;
    }

    public String toJSON() {

        StringBuffer buf = new StringBuffer();

        buf.append("{ \"type\": \"" + type.name() + "\"");

        if (key != null) {
            buf.append(", \"key\": \"" + key + "\"");
        }
        if (mapEntry != null) {
            buf.append(", \"entry\": " + mapEntry.toJSON());
        }
        buf.append("}\n");

        return buf.toString();
    }

    public enum TYPE {
        CREATE,
        DELETE,
        UPDATE
    }
}
