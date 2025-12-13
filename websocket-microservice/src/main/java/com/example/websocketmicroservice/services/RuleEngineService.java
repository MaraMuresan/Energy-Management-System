package com.example.websocketmicroservice.services;

import org.springframework.stereotype.Service;

@Service
public class RuleEngineService {

    public String matchRule(String message) {
        if (message == null) {
            return null;
        }

        String msg = message.toLowerCase();

        if (msg.contains("hello")) {
            return "Hello! How can I help you today?";
        }
        if (msg.contains("device not visible") || msg.contains("missing device")) {
            return "If your device is not visible, ensure it is assigned to your account.";
        }
        if (msg.contains("maximum consumption") || msg.contains("limit")) {
            return "Each device has a maximum consumption limit defined by the administrator.";
        }
        if (msg.contains("overconsumption") || msg.contains("alert")) {
            return "Overconsumption alerts appear when a device exceeds its configured energy limit.";
        }
        if (msg.contains("history") || msg.contains("consumption history")) {
            return "You can view your consumption history by pressing the View Historical Energy Consumption button.";
        }
        if (msg.contains("timezone")) {
            return "The system uses server time zone for all timestamps.";
        }
        if (msg.contains("support")) {
            return "You are now chatting with the automated support assistant!";
        }
        if (msg.contains("delete account") || msg.contains("remove account")) {
            return "Account deletion requests must be personally sent to the system administrator.";
        }
        if (msg.contains("maintenance") || msg.contains("system is down")) {
            return "System maintenance is scheduled by administrators. During maintenance windows, some features may be temporarily unavailable.";
        }
        if (msg.contains("change my username") || msg.contains("update account")) {
            return "Account details such as username or address can be updated by the administrator upon request.";
        }

        return null;
    }
}
