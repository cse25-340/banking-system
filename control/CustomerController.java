package bank.control;

import bank.dao.CustomerDAO;
import bank.entity.Customer;
import bank.entity.User;

/** Registers new customers and finds existing ones. */
public class CustomerController {

    private final BankData data = BankData.getInstance();
    private final CustomerDAO customerDAO = new CustomerDAO();

    /** Registers a customer record only (no login). */
    public Customer registerCustomer(String firstName, String surname, String address, String phone) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        if (phone == null || !phone.trim().matches("[0-9+ ]{7,15}")) {
            throw new IllegalArgumentException("Enter a valid phone number (digits only, 7 to 15 characters)");
        }
        Customer customer = new Customer(data.nextCustomerId(), firstName, surname, address, phone);
        data.getCustomers().put(customer.getCustomerId(), customer);
        customerDAO.saveCustomer(customer);
        return customer;
    }

    /** Registers a customer and creates their login (overloaded version used by the screen). */
    public Customer registerCustomer(String firstName, String surname, String address, String phone,
                                     String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        String key = username.trim();
        if (data.getUsers().containsKey(key)) {
            throw new IllegalArgumentException("That username is already taken");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        Customer customer = registerCustomer(firstName, surname, address, phone);
        User user = new User(key, password, "CUSTOMER", customer.getCustomerId());
        data.getUsers().put(key, user);
        customerDAO.saveUser(user, password);
        return customer;
    }

    public Customer findCustomer(String customerId) {
        if (customerId == null) {
            return null;
        }
        Customer cached = data.getCustomers().get(customerId.trim());
        if (cached != null) {
            return cached;
        }
        Customer fromDb = customerDAO.findById(customerId.trim());
        if (fromDb != null) {
            data.getCustomers().put(fromDb.getCustomerId(), fromDb);
        }
        return fromDb;
    }
}