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
public class VendorLifeCycleTest_3 {
    @Autowired
    private VendorRepository vendorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp() {
        vendorRepository.deleteAll();
    }

    @Test
    void saveWithRepository() {
        Vendor vendor = new Vendor();
        vendor.setName("Repo Vendor");

        Vendor saved = vendorRepository.save(vendor);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Repo Vendor", saved.getName());
    }

    @Test
    void persistWithEntityManager() {
        Vendor vendor = new Vendor();
        vendor.setName("Persist Vendor");

        entityManager.persist(vendor);
        entityManager.flush();

        Assertions.assertNotNull(vendor.getId());
        Assertions.assertEquals("Persist Vendor", vendor.getName());
    }

    @Test
    void mergeWithEntityManager() {
        Vendor vendor = new Vendor();
        vendor.setName("Merge Vendor");

        Vendor merged = entityManager.merge(vendor);
        entityManager.flush();

        Assertions.assertNotNull(merged.getId());
        Assertions.assertEquals("Merge Vendor", merged.getName());
    }
}
