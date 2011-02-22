package it.unipd.netmus.server.persistent;

import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;

public class DatastoreUtils {
    
    public static int usersInDatastore() {
        return ODF.get().find().type(UserAccount.class).returnCount().now();
    }
    
    public static int songsInDatastore() {
        return ODF.get().find().type(Song.class).returnCount().now();
    }
    
    public static List<UserAccount> getAllUsersInDatastore() {
        return ODF.get().find().type(UserAccount.class).returnAll().now();
    }
    
    public static void deleteAllUsersFromDatastore() {
        ODF.get().deleteAll(UserAccount.class);
    }
    
    public static List<Song> getAllSongsInDatastore() {
        return ODF.get().find().type(Song.class).returnAll().now();
    }
    
    public static void deleteAllSongsFromDatastore() {
        ODF.get().deleteAll(Song.class);
    }
    
    public static void deleteAllSongsOutFromLibraryFromDatastore() {
        ODF.get().deleteAll(ODF.get().find().type(Song.class).addFilter("numOwners", FilterOperator.EQUAL, 0).returnAll().now());
    }
    
}
