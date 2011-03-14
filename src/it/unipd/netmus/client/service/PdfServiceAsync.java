package it.unipd.netmus.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PdfServiceAsync {
    void exportPDF(String user, AsyncCallback<String> callback);
}
