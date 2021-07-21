package Helper;

public class Helper_Selectors {
    // URLs
    public static final String HUDL_LOGIN_URL = "https://www.hudl.com/login";

    //Alert selectors
    public static final String LoginAlert_Selector = "body .super-wrap form.login-container .login-error.fade-in-expand p";
    public static final String NeedHelp_InvalidEmailAlert_Selector = "body .super-wrap .reset-container .reset-info .reset-error.fade-in-expand p";
    public static final String LoginOrg_InvalidEmailAlert_Selector = "body .super-wrap .login-container .login-error-code";

    //Miscellaneous UI element selectors
    public static final String PageTitle_Selector = "head title";
    public static final String Username_Selector = "//*[@id='email']";
    public static final String ForgetEmail_Selector = "//*[@id='forgot-email']";
    public static final String Password_Selector = "//*[@id='password']";
    public static final String LoginBtn_Selector = "//*[@id='logIn']";
    public static final String BackBtn_Selector = "body .super-wrap .back-to-hudl";
    public static final String SignUpBtn_Selector = "body .super-wrap .sign-up-trial a";
    public static final String NeedHelpBtn_Selector = "//*[@id='forgot-password-link']";
    public static final String LoginOrgBtn_Selector = "//*[@id='logInWithOrganization']";
    public static final String LoginOrg_LoginButton_Selector = "//*[@id='app']/section/div//form//button";
    public static final String UserAccount_DropdownList = "//*[@id='ssr-webnav']/div/div/nav/div/div/div/div/span[text()[contains(.,'Coach Waraich')]]";
    public static final String UserAccount_LogOut_Selector = "//*[@id='ssr-webnav']/div/div/nav/div/div/div/div/a/span[text()[contains(.,'Log Out')]]";
    public static final String BadCreds_NeedHelp_Selector = "body .super-wrap .login-container .login-error.fade-in-expand div p a";
    public static final String NeedHelp_CheckEmail_Selector = "body .super-wrap .reset-sent-container .reset-info";
    public static final String LoginOrg_HeadingSelector = "#app section form h2";
    public static final String ResetPassHeading_Selector = "body .super-wrap .reset-container .reset-info h1";

    //Text selectors
    public static final String LoginScreen_InvalidCredsAlert = "We didn't recognize that email and/or password. Need help?";
    public static final String LoginScreen_Title_Text = "Log In - Hudl";
    public static final String AccountHomeScreen_Title_Text = "Home - Hudl";
    public static final String HomePage_Title_Text = "Hudl: We Help Teams and Athletes Win";
    public static final String SingUpPage_Title_Text = "Sign up for Hudl";
    public static final String NeedHelpPage_HeadingText = "Login Help";
    public static final String LoginOrgBtn_HeadingText = "Log into Hudl with your Organization";
    public static final String LoginOrgBtn_InvalidEmailAlert = "This account can't log in with an organization yet. Please log in using your email and password.";
    public static final String NeedHelpPage_InvalidEmailFormatAlert = "That isn't a valid email address. Make sure to use the email@domain.com format.";
    public static final String NeedHelp_CheckEmailAlert = "\n" +
            "      Check Your Email\n" +
            "      Click the link in the email to reset your password.\n" +
            "      If you don't see the email, check your junk or spam folders.\n" +
            "    ";
}

