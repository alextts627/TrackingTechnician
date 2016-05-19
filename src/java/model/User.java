package model;
public class User 
{
    private String userName;
    private History userHistory;
    
    public User(String email)
    {
        userName = email;
        userHistory = new History();
    }
    
    public User()
    {
        userName = "";
        userHistory = new History();
    }
    
    public boolean signIn(String email, String pass)
    {
        //compare passed user and password to user and password in DB
        //if true, pass true, else pass false
        boolean validUser = DBAccess.isValidUser(email,pass);
        if(validUser)
        {
            userName = email;
            userHistory = DBAccess.getHistory(userName);
        }
        return validUser;
    }
    
    public boolean isLoggedIn()
    {
        if (userName.equals(""))
            return false;
        else 
            return true;
    }
    
    public void signOut()
    {
        userHistory = null;
        userName = "";
        /*
         * In the servlet, we need to call session.Invalidate();
         * And everything should be fine, assuming that matt's initialization
         * on the index.jsp that generates a new User object
         * if one does not exist respects the new sessionID.
         */
    }
    
    public boolean createAccount(String email, String pass1, String pass2)
    {
        if(!pass1.equals(pass2))
            return false;
        else
        {
            userHistory = new History();
            boolean success = DBAccess.addUser(email, pass1); // this line is just in to remove the error mark
            if(success)
                this.userName = email; //"log in" the user
            return success;        
        }
    }
    
    public History getHistory()
    {
        return userHistory;
    }
    
    public String getUserName()
    {
        return userName;
    }
}
