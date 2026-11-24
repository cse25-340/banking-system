package bank.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {

    private final String customerId;
    private final String firstName;
    private final String surname;
    private String address;
    private String phone;
    private final List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String firstName, String surname, String address, String phone) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer id is required");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException("Surname is required");
        }
        this.customerId = customerId.trim();
        this.firstName = firstName.trim();
        this.surname = surname.trim();
        this.address = address;
        this.phone = phone;
    }

    /** Adds an account to this customer. The account must belong to this customer. */
    public void addAccount(Account account) {
        if (account == null || account.getCustomer() != this) {
            throw new IllegalArgumentException("Account does not belong to this customer");
        }
        for (Account existing : accounts) {
            if (existing.getAccountNumber().equals(account.getAccountNumber())) {
                throw new IllegalArgumentException("Account already added");
            }
        }
        accounts.add(account);
    }

    /** @return a read-only view of this customer's accounts */
    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public String getFullName() {
        return firstName + " " + surname;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return customerId + " - " + getFullName();
    }
}
