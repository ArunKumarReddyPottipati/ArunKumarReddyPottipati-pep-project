package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();

    // Method to validate login credentials
    public Account validateLogin(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account; 
        }
        return null;
    }

    // Method to register a new account
    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty()) {
            return null; 
        }
        
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null; 
        }
        
        if (accountDAO.doesUsernameExist(account.getUsername())) {
            return null;
        }
        return accountDAO.createAccount(account);
    }
}
