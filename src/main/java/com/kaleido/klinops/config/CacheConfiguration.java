package com.kaleido.klinops.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.kaleido.klinops.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.kaleido.klinops.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.kaleido.klinops.domain.User.class.getName());
            createCache(cm, com.kaleido.klinops.domain.Authority.class.getName());
            createCache(cm, com.kaleido.klinops.domain.User.class.getName() + ".authorities");
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName());
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName() + ".endPoints");
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName() + ".studiedProducts");
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName() + ".mileStones");
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName() + ".bioAnalyses");
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName() + ".dataAnalyses");
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName() + ".shipments");
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName() + ".studySamples");
            createCache(cm, com.kaleido.klinops.domain.ClinicalStudy.class.getName() + ".investigators");
            createCache(cm, com.kaleido.klinops.domain.StudyEndPoint.class.getName());
            createCache(cm, com.kaleido.klinops.domain.Site.class.getName());
            createCache(cm, com.kaleido.klinops.domain.Site.class.getName() + ".investigators");
            createCache(cm, com.kaleido.klinops.domain.PrincipalInvestigator.class.getName());
            createCache(cm, com.kaleido.klinops.domain.PrincipalInvestigator.class.getName() + ".studies");
            createCache(cm, com.kaleido.klinops.domain.StudyMilestone.class.getName());
            createCache(cm, com.kaleido.klinops.domain.StudyProduct.class.getName());
            createCache(cm, com.kaleido.klinops.domain.StudySample.class.getName());
            createCache(cm, com.kaleido.klinops.domain.Shipment.class.getName());
            createCache(cm, com.kaleido.klinops.domain.Shipment.class.getName() + ".components");
            createCache(cm, com.kaleido.klinops.domain.Laboratory.class.getName());
            createCache(cm, com.kaleido.klinops.domain.BioAnalysis.class.getName());
            createCache(cm, com.kaleido.klinops.domain.BioAnalysis.class.getName() + ".dataAnalyses");
            createCache(cm, com.kaleido.klinops.domain.DataAnalysis.class.getName());
            createCache(cm, com.kaleido.klinops.domain.DataAnalysis.class.getName() + ".bioAnalyses");
            createCache(cm, com.kaleido.klinops.domain.TrialMasterFile.class.getName());
            createCache(cm, com.kaleido.klinops.domain.ShipmentComponent.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
