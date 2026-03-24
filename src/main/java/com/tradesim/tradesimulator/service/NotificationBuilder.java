package com.tradesim.tradesimulator.service;

import com.tradesim.tradesimulator.model.User;
import java.util.Set;

public class NotificationBuilder {

    public static NotificationService build(User user, DashboardAlertStore alertStore) {
        Set<NotificationChannels> channels = user.getChannels();
        NotificationService chain = new ConsoleNotificationService();

        if (channels.contains(NotificationChannels.EMAIL))
            chain = new EmailNotificationDecorator(chain);
        if (channels.contains(NotificationChannels.SMS))
            chain = new SmsNotificationDecorator(chain);
        if (channels.contains(NotificationChannels.DASHBOARD))
            chain = new BadgeDashboardDecorator(chain, alertStore, user.getId());

        return chain;
    }
}