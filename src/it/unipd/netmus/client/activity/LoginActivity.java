package it.unipd.netmus.client.activity;

import it.unipd.netmus.client.ClientFactory;
import it.unipd.netmus.client.place.LoginPlace;
import it.unipd.netmus.client.place.ProfilePlace;
import it.unipd.netmus.client.service.LoginService;
import it.unipd.netmus.client.service.LoginServiceAsync;
import it.unipd.netmus.client.ui.LoginView;
import it.unipd.netmus.client.ui.MyConstants;
import it.unipd.netmus.shared.FieldVerifier;
import it.unipd.netmus.shared.LoginDTO;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import java.util.Date;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * Nome: LoginActivity.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 15 Febbraio 2011
 */
public class LoginActivity extends AbstractActivity implements
        LoginView.Presenter {
    // Used to obtain views, eventBus, placeController
    // Alternatively, could be injected via GIN
    private ClientFactory client_factory;
    private String user;
    private String password;
    private String error;
    private LoginType login_type;

    private LoginServiceAsync login_service_svc = GWT
            .create(LoginService.class);
    MyConstants my_constants = GWT.create(MyConstants.class);

    public LoginActivity(LoginPlace place, ClientFactory clientFactory) {
        this.user = place.getLoginName();
        this.password = place.getPassword();
        this.error = place.getError();
        this.login_type = place.getLoginType();
        this.client_factory = clientFactory;
    }

    @Override
    public void changeLanguage(String locale) {

        String actual = Window.Location.getHref();

        if (locale.equals("it")) {
            if (!actual.contains("locale=it")) {
                String param = "locale=it";
                if (actual.contains("?")) {
                    param = "&" + param;
                    if (actual.contains("#"))
                        actual = actual.replace("#", param + "#");
                    else
                        actual = actual + param;
                } else {
                    param = "?" + param;
                    if (actual.contains(".html"))
                        actual = actual.replace(".html", ".html" + param);
                    else if (actual.contains("/")) {
                        int i = actual.lastIndexOf("/");
                        actual = actual.substring(0, i + 1) + param
                                + actual.substring(i + 1);
                    }
                }
                Window.Location.assign(actual);
            }
        } else {
            if (actual.contains("locale=it")) {
                String param = "locale=it";
                if (actual.contains("&locale=it"))
                    param = "&locale=it";
                else {
                    if (actual.contains("?locale=it&"))
                        param = "locale=it&";
                    else if (actual.contains("?locale=it"))
                        param = "?locale=it";
                }
                Window.Location.assign(actual.replace(param, ""));
            }
        }

    }

    /**
     * Permette di spostarsi in un place differente anche relativo ad un'altra
     * view.Ad esempio per aprire la pagina di ProfileView una volta verificato
     * il login. Verrà quindi richiamato sempre al termine dei metodi sendLogin
     * e sendRegistration.
     */
    @Override
    public void goTo(Place place) {
        client_factory.getPlaceController().goTo(place);
    }

    /**
     * Permette di effettuare un reindirizzamento al servlet dedicato
     * all'autenticazione Google.
     */
    @Override
    public void sendGoogleLogin(String user, String password)
            throws LoginException {
        Window.Location.assign("/logingoogle");
    }

    /**
     * Invia al server il login inserito dall'utente dopo averne controllato la
     * validità (e-mail valida, password sufficientemente lunga).
     */
    @Override
    public void sendLogin(String user, String password) throws LoginException {
        final String username = user;
        final String pass = password;
        LoginDTO login = new LoginDTO(user, password);

        // Make the call to send login info.
        login_service_svc.startLogin(login, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof LoginException) {
                    goTo(new LoginPlace(username, pass, my_constants
                            .infoLoginIncorrect(), LoginType.NETMUSLOGIN));
                } else {
                    goTo(new LoginPlace(username, pass, my_constants
                            .databaseErrorGeneric(), LoginType.NETMUSLOGIN));
                }
            }

            @Override
            public void onSuccess(String session_id) {
                // create the cookie for this session
                final long DURATION = 1000 * 60 * 60 * 24;
                Date expires = new Date(System.currentTimeMillis() + DURATION);
                Cookies.setCookie("user", username, expires);
                Cookies.setCookie("sid", session_id, expires);

                goTo(new ProfilePlace(username));
            }
        });
    }

    /**
     * Invia al server i dati di registrazione inseriti dall'utente dopo averne
     * controllato la correttezza (e-mail valida, password sufficientemente
     * lunga).
     */
    @Override
    public void sendRegistration(String user, String password,
            String confirmPassword) throws RegistrationException {
        final String username = user;
        final String pass = password;
        LoginDTO login = new LoginDTO(user, password);

        if (!FieldVerifier.isValidPassword(password))
            goTo(new LoginPlace(username, pass, my_constants.errorPassword(),
                    LoginType.NETMUSREGISTRATION));
        else if (!FieldVerifier.isValidEmail(username))
            goTo(new LoginPlace(username, pass, my_constants.errorEmail(),
                    LoginType.NETMUSREGISTRATION));
        else if (!password.equals(confirmPassword))
            goTo(new LoginPlace(username, pass, my_constants.errorCPassword(),
                    LoginType.NETMUSREGISTRATION));
        else {

            // Make the call to send login info.
            login_service_svc.insertRegistration(login,
                    new AsyncCallback<LoginDTO>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            goTo(new LoginPlace(username, pass, my_constants
                                    .infoUserUsato(),
                                    LoginType.NETMUSREGISTRATION));
                        }

                        @Override
                        public void onSuccess(LoginDTO result) {

                            // Reimposta la login
                            LoginView loginView = client_factory.getLoginView();
                            loginView.setLayout();

                            try {
                                sendLogin(result.getUser(),
                                        result.getPassword());
                            } catch (LoginException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * Invocato da ActivityManager per avviare una nuova LoginActivity.
     */
    @Override
    public void start(final AcceptsOneWidget container_widget,
            EventBus event_bus) {

        login_service_svc.getLoggedInUser(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof LoginException) {
                    // user not logged yet - show loginView
                    LoginView loginView = client_factory.getLoginView();
                    loginView.setError(error);
                    loginView.setLoginType(login_type);
                    loginView.setPassword(password);
                    loginView.setUser(user);
                    loginView.setPresenter(LoginActivity.this);
                    container_widget.setWidget(loginView.asWidget());

                }
            }

            @Override
            public void onSuccess(String result) {
                goTo(new ProfilePlace(result));
            }
        });
    }
}