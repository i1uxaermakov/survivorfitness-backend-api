package com.changeplusplus.survivorfitness.backendapi.export;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataExportRequestListener implements ApplicationListener<OnDataExportRequestedEvent> {
    @Override
    public void onApplicationEvent(OnDataExportRequestedEvent event) {
        doExportData(event.getEmailsToSendResultsTo());
    }

    private void doExportData(List<String> emailsToNotify) {

    }
}
