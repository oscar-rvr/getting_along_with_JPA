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
public class VendorLifeCycleTest_5 {

    @Autowired
    private VendorRepository vendorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp() {
        vendorRepository.deleteAll();
    }

    @Test
    void saveTwiceWithSameId_repositoryMerges() {
        Vendor vendor1 = new Vendor();
        vendor1.setId(300L);
        vendor1.setName("Original");
        vendorRepository.save(vendor1);

        Vendor vendor2 = new Vendor();
        vendor2.setId(300L);
        vendor2.setName("Updated via save");

        vendorRepository.save(vendor2);

        Vendor result = vendorRepository.findById(300L).orElseThrow();
        Assertions.assertEquals("Updated via save", result.getName());
    }

    @Test
    void persistTwiceWithSameId_fails() {
        Vendor vendor1 = new Vendor();
        vendor1.setId(300L);
        vendor1.setName("Original");
        entityManager.persist(vendor1);
        entityManager.flush();

        Vendor vendor2 = new Vendor();
        vendor2.setId(300L);
        vendor2.setName("Duplicate");

        Assertions.assertThrows(Exception.class, () -> {
            entityManager.persist(vendor2);
            entityManager.flush();
        });
    }

    @Test
    void mergeTwiceWithSameId_updatesEntity() {
        Vendor vendor1 = new Vendor();
        vendor1.setId(300L);
        vendor1.setName("Original");
        entityManager.persist(vendor1);
        entityManager.flush();

        Vendor vendor2 = new Vendor();
        vendor2.setId(300L);
        vendor2.setName("Updated via merge");

        entityManager.merge(vendor2);
        entityManager.flush();

        Vendor result = vendorRepository.findById(300L).orElseThrow();
        Assertions.assertEquals("Updated via merge", result.getName());
    }
}
