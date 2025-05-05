package com.example.demo;

import com.example.demo.model.Vendor;
import com.example.demo.repository.VendorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class VendorLifeCycleTest_12 {

    @Autowired
    private VendorRepository vendorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp() {
        vendorRepository.deleteAll();
    }

    @Test
    @Transactional
    void flushDetectsChangesOnManagedEntity() {
        Vendor vendor = new Vendor();
        vendor.setName("Initial Name");
        vendor = vendorRepository.save(vendor);

        Vendor managedVendor = vendorRepository.findById(vendor.getId()).orElseThrow();
        managedVendor.setName("Modified Name");

        entityManager.flush();

        entityManager.clear();
        Vendor refreshed = entityManager.find(Vendor.class, vendor.getId());
        assertThat(refreshed.getName()).isEqualTo("Modified Name");
    }
}
