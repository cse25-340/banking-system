package bank.entity;


public class Employer {

    private final String companyName;
    private final String companyAddress;

    public Employer(String companyName, String companyAddress) {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name is required");
        }
        if (companyAddress == null || companyAddress.isBlank()) {
            throw new IllegalArgumentException("Company address is required");
        }
        this.companyName = companyName.trim();
        this.companyAddress = companyAddress.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    @Override
    public String toString() {
        return companyName + ", " + companyAddress;
    }
}
