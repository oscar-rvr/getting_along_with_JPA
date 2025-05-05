package com.example.demo;

import com.example.demo.model.Vendor;
import com.example.demo.repository.VendorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class VendorLifeCycleTest_11 {

    @Autowired
    private VendorRepository vendorRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @AfterEach
    void cleanUp() {
        vendorRepository.deleteAll();
    }

    @Test
    void updateManagedEntity_afterFlush_changesPersist() {
        Vendor vendor = new Vendor();
        vendor.setName("Original Name");
        vendor = vendorRepository.save(vendor);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Vendor managedVendor = em.find(Vendor.class, vendor.getId());
        managedVendor.setName("Updated Name");

        em.flush();
        em.getTransaction().commit();
        em.close();

        Optional<Vendor> updated = vendorRepository.findById(vendor.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Updated Name");
    }
}
