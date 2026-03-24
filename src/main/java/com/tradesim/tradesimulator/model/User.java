package com.tradesim.tradesimulator.model;

import com.tradesim.tradesimulator.service.NotificationChannels;
import java.util.HashSet;
import java.util.Set;

public class User {

    private final String id;
    private final String name;
    private Set<NotificationChannels> channels;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.channels = new HashSet<>(Set.of(NotificationChannels.CONSOLE));
    }

    public String getId()   { return id; }
    public String getName() { return name; }
    public Set<NotificationChannels> getChannels() { return channels; }
    public void setChannels(Set<NotificationChannels> channels) { this.channels = channels; }
}