package com.example.demo;

import com.example.demo.model.Asset;
import com.example.demo.model.Vendor;
import com.example.demo.repository.AssetRepository;
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
public class VendorLifeCycleTest_7 {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AssetRepository assetRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanUp() {
        vendorRepository.deleteAll();
        assetRepository.deleteAll();
    }

    @Test
    void saveVendorWithExistingAssets_repositoryUpdatesAssets() {
        Asset a1 = assetRepository.save(new Asset(null, "Repo Asset 1", null));
        Asset a2 = assetRepository.save(new Asset(null, "Repo Asset 2", null));

        Vendor vendor = new Vendor();
        vendor.setName("Repo Vendor");
        a1.setVendor(vendor);
        a2.setVendor(vendor);
        vendor.setAssets(List.of(a1, a2));

        Vendor saved = vendorRepository.save(vendor);
        vendorRepository.flush();

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(2, saved.getAssets().size());
    }

    @Test
    void persistVendorWithExistingAssets_updatesAssets() {
        Asset a1 = new Asset(null, "Persist Asset 1", null);
        Asset a2 = new Asset(null, "Persist Asset 2", null);
        entityManager.persist(a1);
        entityManager.persist(a2);
        entityManager.flush();

        Vendor vendor = new Vendor();
        vendor.setName("Persist Vendor");
        a1.setVendor(vendor);
        a2.setVendor(vendor);
        vendor.setAssets(List.of(a1, a2));

        entityManager.persist(vendor);
        entityManager.flush();

        Assertions.assertNotNull(vendor.getId());
        Assertions.assertEquals(2, vendor.getAssets().size());
    }

    @Test
    void mergeVendorWithExistingAssets_updatesRelation() {
        Asset a1 = new Asset(null, "Merge Asset 1", null);
        Asset a2 = new Asset(null, "Merge Asset 2", null);
        entityManager.persist(a1);
        entityManager.persist(a2);
        entityManager.flush();

        Vendor vendor = new Vendor();
        vendor.setName("Merge Vendor");
        a1.setVendor(vendor);
        a2.setVendor(vendor);
        vendor.setAssets(List.of(a1, a2));

        Vendor merged = entityManager.merge(vendor);
        entityManager.flush();

        Assertions.assertNotNull(merged.getId());
        Assertions.assertEquals(2, merged.getAssets().size());
    }
}
