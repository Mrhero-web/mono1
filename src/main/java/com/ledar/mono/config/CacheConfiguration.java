package com.ledar.mono.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.ledar.mono.domain.User.class.getName());
            createCache(cm, com.ledar.mono.domain.Role.class.getName());
            createCache(cm, com.ledar.mono.domain.UserRole.class.getName());
            createCache(cm, com.ledar.mono.domain.Menu.class.getName());
            createCache(cm, com.ledar.mono.domain.Api.class.getName());
            createCache(cm, com.ledar.mono.domain.SysRoleApi.class.getName());
            createCache(cm, com.ledar.mono.domain.SysRoleMenu.class.getName());
            createCache(cm, com.ledar.mono.domain.SysRoleDataScope.class.getName());
            createCache(cm, com.ledar.mono.domain.SysUserDataScope.class.getName());
            createCache(cm, com.ledar.mono.domain.Staff.class.getName());
            createCache(cm, com.ledar.mono.domain.Patient.class.getName());
            createCache(cm, com.ledar.mono.domain.Group.class.getName());
//            createCache(cm, com.ledar.mono.domain.InMedicalAdvice.class.getName());
//            createCache(cm, com.ledar.mono.domain.OutMedicalAdvice.class.getName());
//            createCache(cm, com.ledar.mono.domain.TreatmentProgram.class.getName());
//            createCache(cm, com.ledar.mono.domain.EItem1.class.getName());
//            createCache(cm, com.ledar.mono.domain.EItem2.class.getName());
//            createCache(cm, com.ledar.mono.domain.EItemResult.class.getName());
//            createCache(cm, com.ledar.mono.domain.EForm.class.getName());
//            createCache(cm, com.ledar.mono.domain.ScheduleRecordNow.class.getName());
//            createCache(cm, com.ledar.mono.domain.ScheduleRecord.class.getName());
//            createCache(cm, com.ledar.mono.domain.ScheduleRecordHistory.class.getName());
//            createCache(cm, com.ledar.mono.domain.ScheduleRecordDetailsNow.class.getName());
//            createCache(cm, com.ledar.mono.domain.ScheduleRecordDetails.class.getName());
//            createCache(cm, com.ledar.mono.domain.ScheduleRecordDetailsHistory.class.getName());
            createCache(cm, com.ledar.mono.domain.Department.class.getName());
//            createCache(cm, com.ledar.mono.domain.Therapist.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
