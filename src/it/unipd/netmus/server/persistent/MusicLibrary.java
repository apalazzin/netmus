/**
 * 
 */
package it.unipd.netmus.server.persistent;

import com.vercer.engine.persist.annotation.Parent;

/**
 * @author ValterTexasGroup
 *
 */
public class MusicLibrary {
	
	@Parent private UserAccount owner;
	
	public MusicLibrary(UserAccount owner) {
	    this.owner = owner;
	}
	
    public UserAccount getOwner() {
        return owner;
    }
    public void setOwner(UserAccount owner) {
        this.owner = owner;
    }

}
