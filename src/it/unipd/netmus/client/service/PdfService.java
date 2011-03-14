package it.unipd.netmus.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("pdfService")
public interface PdfService extends RemoteService {
    String exportPDF(String user) throws Exception;
}
