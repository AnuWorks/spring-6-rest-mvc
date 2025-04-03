package com.anuworks.spring6restmvc.repo;

import com.anuworks.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepoTest {

    @Autowired
    CustomerRepo customerRepo;


    @Test
    @Transactional
    @Rollback
    void testSaveCustomer() {
        Customer customer = customerRepo.save(Customer.builder()
                        .customerName("Anudeep")
                .build());

        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isNotNull();
    }


}