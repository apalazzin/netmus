package it.unipd.netmus.client.ui;

import it.unipd.netmus.client.ui.LoginView.Presenter.LoginType;
import it.unipd.netmus.shared.exception.LoginException;
import it.unipd.netmus.shared.exception.RegistrationException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Nome: LoginViewImpl.java 
 * Autore: VT.G 
 * Licenza: GNU GPL v3 
 * Data Creazione: 16 Febbraio 2011
 */
public class LoginViewImpl extends Composite implements LoginView {

    interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl> {
    }
    private static LoginViewImplUiBinder uiBinder = GWT
            .create(LoginViewImplUiBinder.class);

    MyConstants myConstants = GWT.create(MyConstants.class);

    private Presenter listener;
    private LoginType type;

    @UiField
    Button login;
    @UiField
    Label account;
    @UiField
    Label register;
    @UiField
    Label error;

    @UiField
    Label wlcmT;
    @UiField
    Label wlcm1;
    @UiField
    Label wlcm2;
    @UiField
    Label wlcm3;
    @UiField
    Label wlcm4;

    @UiField
    Image flag_ita;
    @UiField
    Image flag_eng;

    @UiField
    HTMLPanel container;

    @UiField
    TextBox user;
    @UiField
    PasswordTextBox password;
    @UiField
    PasswordTextBox c_password;
    @UiField
    RadioButton check_google;
    @UiField
    RadioButton check_netmus;

    public LoginViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        // localizzazione
        login.setText(myConstants.loginLabel());
        register.setText(myConstants.registerSwitchLabel());
        account.setText(myConstants.accountNetmus());

        wlcmT.setText(myConstants.wlcmT());
        wlcm1.setText(myConstants.wlcm1());
        wlcm2.setText(myConstants.wlcm2());
        wlcm3.setText(myConstants.wlcm3());
        wlcm4.setText(myConstants.wlcm4());

        check_netmus.setValue(true);

        Timer timerMain = new Timer() {
            @Override
            public void run() {
                container.getElement().getStyle().setProperty("opacity", "1");
            }
        };
        timerMain.schedule(200);

    }

    @Override
    public void setError(String error) {
        this.error.setText(error);
    }

    @Override
    public void setLayout() {

        c_password.setText("");

        DOM.getElementById("registrazione").getStyle()
                .setProperty("opacity", "0");
        DOM.getElementById("registrazione").getStyle()
                .setProperty("height", "0");

        register.getElement().getStyle().setProperty("opacity", "0");

        register.setText(myConstants.registerSwitchLabel());
        register.getElement().getStyle().setProperty("opacity", "1");

        login.getElement().getStyle().setProperty("opacity", "0");

        login.setText(myConstants.loginLabel());
        login.getElement().getStyle().setProperty("opacity", "1");
        check_google.setValue(false);
        check_netmus.setValue(true);
        check_google.setEnabled(true);

    }

    @Override
    public void setLoginType(LoginType loginType) {
        this.type = loginType;
    }

    @Override
    public void setPassword(String password) {
        this.password.setText(password);
    }

    @Override
    public void setPresenter(Presenter listener) {
        this.listener = listener;
    }

    @Override
    public void setUser(String user) {
        this.user.setText(user);
    }

    @UiHandler("flag_eng")
    void handleClickFlagEng(ClickEvent e) {

        listener.changeLanguage("en");
    }

    @UiHandler("flag_ita")
    void handleClickFlagIta(ClickEvent e) {

        listener.changeLanguage("it");
    }

    @UiHandler("check_google")
    void handleClickGoogle(ClickEvent e) {

        Timer timerLoginLabel = new Timer() {
            @Override
            public void run() {
                login.setText(myConstants.accountGoogleLogin());
            }
        };
        timerLoginLabel.schedule(400);

        login.setWidth("424px");
        account.setText(myConstants.accountGoogle());
        type = LoginType.GOOGLELOGIN;

        if (check_netmus.getValue()) {

            DOM.getElementById("labels").getStyle().setOpacity(0);
            check_netmus.setValue(false);
            user.setEnabled(false);
            user.getElement().getStyle().setOpacity(0);
            password.setEnabled(false);
            password.getElement().getStyle().setOpacity(0);

        }

    }

    @UiHandler("login")
    void handleClickLogin(ClickEvent e) {
        try {
            if (type != LoginType.NETMUSREGISTRATION) {
                if (type == LoginType.GOOGLELOGIN)
                    listener.sendGoogleLogin(user.getText(), password.getText());
                else
                    listener.sendLogin(user.getText(), password.getText());
            } else
                listener.sendRegistration(user.getText(), password.getText(),
                        c_password.getText());
        } catch (LoginException e1) {
            e1.printStackTrace();
        } catch (RegistrationException e2) {
            e2.printStackTrace();
        }
    }

    @UiHandler("check_netmus")
    void handleClickNetmus(ClickEvent e) {

        login.setText("Login");
        login.setWidth("80px");

        account.setText(myConstants.accountNetmus());
        type = LoginType.NETMUSLOGIN;

        if (check_google.getValue()) {

            Timer timerLoginLabel = new Timer() {
                @Override
                public void run() {
                    DOM.getElementById("labels").getStyle().setOpacity(1);
                    check_google.setValue(false);
                    user.setEnabled(true);
                    user.getElement().getStyle().setOpacity(1);
                    password.setEnabled(true);
                    password.getElement().getStyle().setOpacity(1);
                }
            };
            timerLoginLabel.schedule(400);

        }

    }

    @UiHandler("register")
    void handleClickRegister(ClickEvent e) {

        if (type != LoginType.NETMUSREGISTRATION) {

            type = LoginType.NETMUSREGISTRATION;

            login.setText("Login");
            login.setWidth("80px");

            Timer timerLoginGLabel = new Timer() {
                @Override
                public void run() {
                    DOM.getElementById("labels").getStyle().setOpacity(1);
                    check_google.setValue(false);
                    user.setEnabled(true);
                    user.getElement().getStyle().setOpacity(1);
                    password.setEnabled(true);
                    password.getElement().getStyle().setOpacity(1);
                }

            };

            timerLoginGLabel.schedule(400);

            DOM.getElementById("password_check").setInnerHTML(
                    myConstants.passwordCheck());

            DOM.getElementById("registrazione").getStyle()
                    .setProperty("opacity", "1");
            DOM.getElementById("registrazione").getStyle()
                    .setProperty("height", "40px");

            register.getElement().getStyle().setProperty("opacity", "0");
            Timer timerRegisterLabel = new Timer() {
                @Override
                public void run() {
                    register.setText(myConstants.loginSwitchLabel());
                    register.getElement().getStyle()
                            .setProperty("opacity", "1");
                }
            };
            timerRegisterLabel.schedule(200);

            login.getElement().getStyle().setProperty("opacity", "0");
            Timer timerLoginLabel = new Timer() {
                @Override
                public void run() {
                    login.setText(myConstants.registerLabel());
                    login.getElement().getStyle().setProperty("opacity", "1");
                }
            };
            timerLoginLabel.schedule(200);

            account.setText(myConstants.accountNetmus());
            check_google.setValue(false);
            check_netmus.setValue(true);
            check_google.setEnabled(false);
            user.setEnabled(true);
            password.setEnabled(true);

        } else {

            type = LoginType.NETMUSLOGIN;

            DOM.getElementById("registrazione").getStyle()
                    .setProperty("opacity", "0");
            DOM.getElementById("registrazione").getStyle()
                    .setProperty("height", "0");

            register.getElement().getStyle().setProperty("opacity", "0");
            Timer timerRegisterLabel = new Timer() {
                @Override
                public void run() {
                    register.setText(myConstants.registerSwitchLabel());
                    register.getElement().getStyle()
                            .setProperty("opacity", "1");
                }
            };
            timerRegisterLabel.schedule(200);

            login.getElement().getStyle().setProperty("opacity", "0");
            Timer timerLoginLabel = new Timer() {
                @Override
                public void run() {
                    login.setText(myConstants.loginLabel());
                    login.getElement().getStyle().setProperty("opacity", "1");
                }
            };
            timerLoginLabel.schedule(200);

            check_google.setValue(false);
            check_netmus.setValue(true);
            check_google.setEnabled(true);

        }
    }

    @UiHandler(value = { "user", "password", "c_password" })
    void handlePressEnterPassword(KeyPressEvent e) {
        if (e.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            try {
                if (type != LoginType.NETMUSREGISTRATION) {
                    if (type == LoginType.GOOGLELOGIN)
                        listener.sendGoogleLogin(user.getText(),
                                password.getText());
                    else
                        listener.sendLogin(user.getText(), password.getText());
                } else
                    listener.sendRegistration(user.getText(),
                            password.getText(), c_password.getText());
            } catch (LoginException e1) {
                e1.printStackTrace();
            } catch (RegistrationException e2) {
                e2.printStackTrace();
            }
        }
    }

}
