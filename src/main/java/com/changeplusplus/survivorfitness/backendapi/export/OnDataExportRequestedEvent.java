package com.changeplusplus.survivorfitness.backendapi.export;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class OnDataExportRequestedEvent extends ApplicationEvent {
    private List<String> emailsToSendResultsTo;

    public OnDataExportRequestedEvent(Object source, List<String> emailsToSendResultsTo) {
        super(source);
        this.emailsToSendResultsTo = emailsToSendResultsTo;
    }
}
