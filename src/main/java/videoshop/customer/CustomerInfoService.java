package videoshop.customer;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service("customerInfoService")
public class CustomerInfoService {

    private final CustomerRepository customerRepository;
    private final UserAccountManagement userAccountManagement;

    public CustomerInfoService(CustomerRepository customerRepository, UserAccountManagement userAccountManagement) {
        this.customerRepository = customerRepository;
        this.userAccountManagement = userAccountManagement;
    }

    public Optional<LocalDate> getCurrentCustomerBirthDate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        return userAccountManagement.findByUsername(authentication.getName())
                .flatMap(customerRepository::findByUserAccount)
                .map(Customer::getBirthDate);
    }
}