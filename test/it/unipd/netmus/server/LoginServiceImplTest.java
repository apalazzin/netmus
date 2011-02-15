
package it.unipd.netmus.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.UserSummaryDTO;

import org.junit.Test;

//public class LoginServiceImplTest {

import org.junit.After;
import org.junit.Before;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class LoginServiceImplTest extends RemoteServiceServlet{
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    private static ObjectDatastore datastore;
    @Before
    public void setUp() {
        helper.setUp();
        datastore = new AnnotationObjectDatastore();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    
    
    @Test
    public void testInsertRegistration() {
		LoginDTO login = new LoginDTO("pippo","culo");
		doInsert(login);
		QueryResultIterator<UserAccount> res = datastore.find().type(UserAccount.class).returnResultsNow();
		UserAccount aux = res.next();
        assertEquals("pippo",aux.getUser());
   }
    
    public void doInsert(LoginDTO l){
		UserAccount userAccount = new UserAccount(l.getUser(),l.getPassword());
		datastore.store().instance(userAccount).ensureUniqueKey().returnKeyNow();
    }
        
    @Test
    public void testVerifyLogin(){
    	UserAccount usr = new UserAccount("pippo","poppa");
		datastore.store().instance(usr).ensureUniqueKey().returnKeyNow();
    	LoginDTO login = new LoginDTO("pippo","poppa");
    	assertEquals(true,doVerify(login));
    }

    public static UserAccount doFindUser(String user) {
        Iterator<UserAccount> found = datastore.find().type(UserAccount.class).returnResultsNow();
        if (found.hasNext())
      	  return found.next();
        else return null;
     }
    
    public boolean doVerify(LoginDTO login){
		UserAccount userAccount = doFindUser(login.getUser());
		if (userAccount == null) {
			return false;
		}
		else {
			if (login.getPassword() == userAccount.getPassword()) {
				return true;
			}
			else return false;
		}
    }
    
    @Test 
    public void testGetLoggedInUserDTO(){
    	UserSummaryDTO aux = doGetLoggedInUserDTO();
        assertEquals("pippo",aux.getNickName());    	
    }
    
	public UserSummaryDTO doGetLoggedInUserDTO() {
	    //http session stub
	    class HttpSessionStub{
			public Object getAttribute(String arg0) {
				return "pippo";
			}
	    };
	    // LoginHelper stub
	    class LoginHelperStub{
	    	public UserAccount getLoggedInUser(HttpSessionStub s){
	    		if(s!=null)
	    		  return new UserAccount((String)s.getAttribute("x"),"poppa");
	    		return null;
	    	}	    	
	    };

		UserSummaryDTO userDTO;
	    HttpSessionStub session = new HttpSessionStub();
		LoginHelperStub stub = new LoginHelperStub();
	    UserAccount u = stub.getLoggedInUser(session);
	    if (u == null)
	      return null;
	    userDTO = u.toUserSummaryDTO();
	    return userDTO;
	}
	
	@Test
	public void testGetAllUsers(){
    	UserAccount usr = new UserAccount("pippo","a");
		datastore.store().instance(usr).ensureUniqueKey().returnKeyNow();
		UserAccount usr1 = new UserAccount("pipp","pa");
		datastore.store().instance(usr1).ensureUniqueKey().returnKeyNow();
    	UserAccount usr2 = new UserAccount("pio","ppa");
		datastore.store().instance(usr2).ensureUniqueKey().returnKeyNow();
		ArrayList<UserSummaryDTO> expected = new ArrayList<UserSummaryDTO>();
		expected.add(usr.toUserSummaryDTO());
		expected.add(usr1.toUserSummaryDTO());
		expected.add(usr2.toUserSummaryDTO());
		ArrayList<UserSummaryDTO> tester = doGetAllUsers();
		for(int i=0;i<tester.size();i++){
			assertEquals(expected.get(i).getNickName(),tester.get(tester.size()-i-1).getNickName());
		}
		
	}
	
	public ArrayList<UserSummaryDTO> doGetAllUsers() {
		Iterator<UserAccount> allUsers = datastore.find().type(UserAccount.class).returnResultsNow();
		ArrayList<UserAccount> allUsersList = new ArrayList<UserAccount>();
		while (allUsers.hasNext() == true)
			allUsersList.add(allUsers.next());
		ArrayList<UserSummaryDTO> allUsersDTO = new ArrayList<UserSummaryDTO>();
		for (UserAccount tmp:allUsersList)
			allUsersDTO.add(tmp.toUserSummaryDTO());
		return allUsersDTO;
	}
}

