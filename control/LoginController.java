package bank.control;

import bank.dao.CustomerDAO;
import bank.entity.Customer;
import bank.entity.User;

/** Checks logins and remembers who is logged in. */
public class LoginController {

    private final BankData data = BankData.getInstance();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private User currentUser;

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        String trimmed = username.trim();
        User user = data.getUsers().get(trimmed);

        if (user == null) {
            user = customerDAO.findUserByUsername(trimmed);
            if (user != null) {
                data.getUsers().put(trimmed, user);
                Customer customer = customerDAO.findById(trimmed);
                if (customer != null) {
                    data.getCustomers().put(trimmed, customer);
                }
            }
        }

        if (user != null && user.verifyPassword(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    /** @return the customer record of the logged-in user, or null for staff users */
    public Customer getCurrentCustomer() {
        if (currentUser == null || currentUser.getCustomerId() == null) {
            return null;
        }
        return data.getCustomers().get(currentUser.getCustomerId());
    }

    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }

    /** @return the name to show on screen, e.g. "Kabo Moyo" or "admin" */
    public String getDisplayName() {
        Customer customer = getCurrentCustomer();
        if (customer != null) {
            return customer.getFullName();
        }
        return currentUser == null ? "" : currentUser.getUsername();
    }
}