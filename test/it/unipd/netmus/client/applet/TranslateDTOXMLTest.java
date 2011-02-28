package it.unipd.netmus.client.applet;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

import it.unipd.netmus.shared.*;

public class TranslateDTOXMLTest extends GWTTestCase {
	
	private static String getXML(){
		return "<?xml version=\"1.0\" encoding=\"ISO-8859-15\" standalone=\"no\"?><Library><Song File=\"canzoni/AIN0120-06 - Beautiful Soul - Mccartney  Jesse.mp3\"/><Song File=\"canzoni/Ascolta il tuo cuore - L Pausini.mp3\"><LeadArtist>Laura Pausini</LeadArtist><SongGenre>Italiane</SongGenre><SongTitle>Ascolta il tuo cuore</SongTitle></Song><Song File=\"canzoni/Cercami   Renato Zero.mp3\"><LeadArtist>Renato Zero</LeadArtist><SongGenre>Italiane</SongGenre><SongTitle>Cercami (Renato Zero)</SongTitle></Song><Song File=\"canzoni/Ronan keating  when you say nothing at all.mp3\"><SongTitle>Ronan keating  when you say nothing at all</SongTitle></Song><Song File=\"4 Minutes (Album Version).mp3\"><LeadArtist>Madonna feat. Justin Timberlake  Timberland</LeadArtist><SongTitle>4 Minutes (Album Version)</SongTitle></Song><Song File=\"--- Madonna feat. Justin Timberlake  Timberland - 4 Minutes (Album Version).mp3\"><LeadArtist>Madonna feat. Justin Timberlake  Timberland</LeadArtist><SongTitle>4 Minutes (Album Version)</SongTitle></Song></Library>";
	}
	
	private static List<SongDTO> getDTOs(){
		List<SongDTO> list = new ArrayList<SongDTO>();
		list.add(new SongDTO("canzoni/AIN0120-06 - Beautiful Soul - Mccartney  Jesse.mp3"));
		list.add(new SongDTO("Laura Pausini","Ascolta il tuo cuore","","","","Italiane","","canzoni/Ascolta il tuo cuore - L Pausini.mp3"));
		list.add(new SongDTO("Renato Zero","Cercami (Renato Zero)","","","","Italiane","","canzoni/Cercami   Renato Zero.mp3"));
		list.add(new SongDTO("","Ronan keating  when you say nothing at all","","","","","","canzoni/Ronan keating  when you say nothing at all.mp3"));
		list.add(new SongDTO("Madonna feat. Justin Timberlake  Timberland","4 Minutes (Album Version)","","","","","","4 Minutes (Album Version).mp3"));
		list.add(new SongDTO("Madonna feat. Justin Timberlake  Timberland","4 Minutes (Album Version)","","","","","","--- Madonna feat. Justin Timberlake  Timberland - 4 Minutes (Album Version).mp3"));
		return list;
	}
	
	private static boolean compare(SongDTO a, SongDTO b){
		if (! a.getAlbum().equals(b.getAlbum())) return false;
		if (! a.getAuthor().equals(b.getAuthor())) return false;
		if (! a.getComposer().equals(b.getComposer())) return false;
		if (! a.getFile().equals(b.getFile())) return false;
		if (! a.getGenre().equals(b.getGenre())) return false;
		if (! a.getTitle().equals(b.getTitle())) return false;
		if (! a.getTrackNumber().equals(b.getTrackNumber())) return false;
		if (! a.getYear().equals(b.getYear())) return false;
		return true;
	}
	
	@Test
	public void testXMLToDTO() {
		TranslateDTOXML a = new TranslateDTOXML();
		List<SongDTO> result = a.XMLToDTO(getXML());
		List<SongDTO> expected = getDTOs();
		if (result.size() != expected.size())
			fail("WTF! Dimensione diversa.");
		for (int i=0; i<result.size(); i++){
			if (!compare(result.get(i),expected.get(i))) fail("WTF! Non coincidono.");
		}
		//assertTrue(true);
	}

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return "com.google.gwt.xml.XML";
		//return "it.unipd.netmus.client.applet.TranslateDTOXML";
	}

}
