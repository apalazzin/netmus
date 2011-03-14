package it.unipd.netmus.server;

import java.io.StringWriter;

import it.unipd.netmus.client.service.PdfService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PdfServiceImpl extends RemoteServiceServlet implements PdfService {
    private static final long serialVersionUID = 5506566782918627250L;

	public String exportPDF(String user) throws Exception{
	
		//generate the document
		StringWriter writer = new StringWriter();
		writer.append('c');
		String newDocId = "<error>";
		
		try{
		//create the doc on google docs
			String title = "test doc for " + user;
			String content = writer.toString();
			newDocId = GdataManager.manager().createNewDocument(title, content).getResourceId();
		} catch (Exception e) {
			e.printStackTrace();
			return "error interfacing with Google Docs, see your logs";
		}

		
		//return the id
		return "I've just created a document with resource id " +
				"<a target=\"_blank\" href=\"./pdf?id=" + newDocId +"\">" + newDocId + "</a>";
	}
  }
