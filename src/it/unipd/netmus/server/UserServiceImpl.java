package it.unipd.netmus.server;

import it.unipd.netmus.client.service.UserService;
import it.unipd.netmus.server.persistent.UserAccount;
import it.unipd.netmus.server.utils.BCrypt;
import it.unipd.netmus.shared.UserCompleteDTO;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Nome: UserServiceImpl.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 13 Febbraio 2011
 */
@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements
        UserService {

    /**
     * Cancella irreversibilmente l’utente e tutte le sue informazioni dal
     * Datastore e conseguentemente lo reindirizza alla pagina iniziale di
     * login. Le canzoni che facevano parte del catalogo non vengono cancellate.
     */
    @Override
    public boolean deleteProfile(String user) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Salva nell’ UserAccount del Datastore i dati presenti nell’
     * UserCompleteDTO dato in input sovrascrivendo le informazioni precedenti.
     * E previsto qui anche il cambio della password.
     */
    @Override
    public boolean editProfile(String user, UserCompleteDTO new_info_user) {

        try {
            UserAccount current_user = UserAccount.load(user);

            if (!new_info_user.getNewPassword().equals("")) {
                String passwordHash = BCrypt.hashpw(
                        new_info_user.getNewPassword(), BCrypt.gensalt());
                current_user.setPassword(passwordHash);
            }

            current_user.setNickName(new_info_user.getNickName());
            current_user.setFirstName(new_info_user.getFirstName());
            current_user.setLastName(new_info_user.getLastName());
            current_user.setGender(new_info_user.getGender());
            current_user.setNationality(new_info_user.getNationality());
            current_user.setAboutMe(new_info_user.getAboutMe());
            return true;

        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Cerca nel Datastore i nomi degli utenti il cui catalogo ha proprietà
     * simili a quello dato in input. I criteri di somiglianza sono dati
     * dall’artista più ricorrente ed il genere più ascoltato.
     */
    @Override
    public List<String> findRelatedUsers(String user) {
        UserAccount user_account = UserAccount.load(user);
        return user_account.findRelatedUsers();
    }

    /**
     * Trova nel Datastore l’utente a cui corrisponde l’username dato in input e
     * ne ritorna le informazioni incapsulate in un DTO. Gli utenti di cui viene
     * richiesto il caricamento devono essere presenti nel Datastore.
     */
    @Override
    public UserCompleteDTO loadProfile(String user) {
        return UserAccount.load(user).toUserCompleteDTO();
    }

}
