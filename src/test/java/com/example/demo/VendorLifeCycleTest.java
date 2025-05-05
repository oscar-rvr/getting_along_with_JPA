package com.example.demo;

import com.example.demo.model.Vendor;
import com.example.demo.repository.VendorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class VendorLifeCycleTest {
    @Autowired
    private VendorRepository vendorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp(){
        vendorRepository.deleteAll();
    }

    @Test
    void givenVendorWithoutId_whenSavedWithRepository_thenInserted(){
        Vendor vendor = new Vendor();
        vendor.setName("Repo Vendor");

        Vendor saved =vendorRepository.save(vendor);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Repo Vendor", saved.getName());
    }
}
