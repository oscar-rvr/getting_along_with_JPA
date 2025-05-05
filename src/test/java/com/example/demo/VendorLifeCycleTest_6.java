package com.example.demo;

import com.example.demo.model.Asset;
import com.example.demo.model.Vendor;
import com.example.demo.repository.VendorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class VendorLifeCycleTest_6 {

    @Autowired
    private VendorRepository vendorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp() {
        vendorRepository.deleteAll();
    }

    @Test
    void saveVendorWithAssets_repositoryCascades() {
        Vendor vendor = new Vendor();
        vendor.setName("Repo Vendor");

        Asset asset1 = new Asset(null, "Asset 1", vendor);
        Asset asset2 = new Asset(null, "Asset 2", vendor);

        vendor.setAssets(List.of(asset1, asset2));

        Vendor saved = vendorRepository.save(vendor);
        vendorRepository.flush();

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(2, saved.getAssets().size());
    }

    @Test
    void persistVendorWithAssets_cascades() {
        Vendor vendor = new Vendor();
        vendor.setName("Persist Vendor");

        Asset asset1 = new Asset(null, "Asset 1", vendor);
        Asset asset2 = new Asset(null, "Asset 2", vendor);

        vendor.setAssets(List.of(asset1, asset2));

        entityManager.persist(vendor);
        entityManager.flush();

        Assertions.assertNotNull(vendor.getId());
        Assertions.assertEquals(2, vendor.getAssets().size());
    }

    @Test
    void mergeVendorWithAssets_cascades() {
        Vendor vendor = new Vendor();
        vendor.setName("Merge Vendor");

        Asset asset1 = new Asset(null, "Asset 1", vendor);
        Asset asset2 = new Asset(null, "Asset 2", vendor);

        vendor.setAssets(List.of(asset1, asset2));

        Vendor merged = entityManager.merge(vendor);
        entityManager.flush();

        Assertions.assertNotNull(merged.getId());
        Assertions.assertEquals(2, merged.getAssets().size());
    }
}
