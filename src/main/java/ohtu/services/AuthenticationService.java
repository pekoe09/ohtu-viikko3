package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ohtu.data_access.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationService {

    private UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if(validateUser(user, username, password))
                return true;
        }
        return false;
    }
    
    private boolean validateUser(User user, String username, String password) {
        if (user.getUsername().equals(username)) {
            if (user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null || invalid(username, password)) {
            return false;
        }

        userDao.add(new User(username, password));
        return true;
    }

    private boolean invalid(String username, String password) {
        Pattern unamePattern = Pattern.compile("[a-z]{3,}");
        boolean unameOk =  unamePattern.matcher(username).matches();
        if(!unameOk) return true;        
        Pattern pwordPattern = Pattern.compile("(?=.*[\\d\\p{Punct}]).{8,}");
        boolean pwordOk = pwordPattern.matcher(password).matches();
        if(!pwordOk) return true;        
        return false;
    }
}
