package com.example.demo;

import com.example.demo.model.Asset;
import com.example.demo.model.Vendor;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.VendorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class VendorLifeCycleTest_10 {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AssetRepository assetRepository;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @AfterEach
    void cleanUp() {
        assetRepository.deleteAll();
        vendorRepository.deleteAll();
    }

    @Test
    void persistAssetWithDetachedVendor_fails() {
        Vendor vendor = new Vendor();
        vendor.setName("Detached Vendor");
        vendor = vendorRepository.save(vendor);

        Asset asset = new Asset();
        asset.setName("Asset with detached vendor");
        asset.setVendor(vendor);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        assertThrows(Exception.class, () -> {
            em.persist(asset);
            em.getTransaction().commit();
        });

        em.close();
    }

    @Test
    void mergeAssetWithDetachedVendor_succeeds() {
        Vendor vendor = new Vendor();
        vendor.setName("Detached Vendor");
        vendor = vendorRepository.save(vendor);

        Asset asset = new Asset();
        asset.setName("Asset with detached vendor");
        asset.setVendor(vendor);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.merge(asset);
        em.getTransaction().commit();
        em.close();

        assertThat(assetRepository.findAll()).hasSize(1);
    }

    @Test
    void saveAssetWithDetachedVendor_failsUnlessCascade() {
        Vendor vendor = new Vendor();
        vendor.setName("Detached Vendor");
        vendor = vendorRepository.save(vendor);

        Asset asset = new Asset();
        asset.setName("Asset with detached vendor");
        asset.setVendor(vendor);

        assertThrows(Exception.class, () -> assetRepository.save(asset));
    }
}
