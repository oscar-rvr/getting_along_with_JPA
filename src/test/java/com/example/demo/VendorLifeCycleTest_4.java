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
public class VendorLifeCycleTest_4 {

    @Autowired
    private VendorRepository vendorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp() {
        vendorRepository.deleteAll();
    }

    @Test
    void saveWithManualId_repositoryThrows() {
        Vendor vendor = new Vendor();
        vendor.setName("Repo vendor with ID");
        vendor.setId(1L);

        Assertions.assertThrows(Exception.class, () -> {
            vendorRepository.save(vendor);
            vendorRepository.flush();
        });
    }

    @Test
    void persistWithManualId_ignored() {
        Vendor vendor = new Vendor();
        vendor.setId(100L);
        vendor.setName("Persist vendor with ID");

        entityManager.persist(vendor);
        entityManager.flush();

        Assertions.assertNotNull(vendor.getId());
        System.out.println("Generated ID: " + vendor.getId());
    }

    @Test
    void mergeWithManualId_inserted() {
        Vendor vendor = new Vendor();
        vendor.setId(200L);
        vendor.setName("Merged Vendor");

        Vendor merged = entityManager.merge(vendor);
        entityManager.flush();

        Assertions.assertNotNull(merged.getId());
        Assertions.assertEquals("Merged Vendor", merged.getName());
    }
}
