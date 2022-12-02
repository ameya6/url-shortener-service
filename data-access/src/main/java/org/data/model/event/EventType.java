package org.data.model.event;

import org.data.model.params.EventLogParams;

public enum EventType {
    URL_CREATE("URL_CREATE"),
    URL_ACCESS("URL_ACCESS");

    EventType(String type){}
}
