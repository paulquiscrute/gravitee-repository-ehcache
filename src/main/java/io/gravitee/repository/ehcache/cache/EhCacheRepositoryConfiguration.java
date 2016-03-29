/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.repository.ehcache.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

/**
 * @author David BRASSELY (david at gravitee.io)
 * @author GraviteeSource Team
 */
@Configuration
@ComponentScan
public class EhCacheRepositoryConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheRepositoryConfiguration.class);

    @Bean
    public org.springframework.cache.CacheManager getEhCacheManager() {
        return new org.springframework.cache.ehcache.EhCacheCacheManager(getEhCacheFactory().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean getEhCacheFactory() {
        String graviteeHome = System.getProperty("gravitee.home");
        String ehCacheConfiguration = graviteeHome + File.separator + "config" + File.separator + "ehcache.xml";
        File ehCacheConfigurationFile = new File(ehCacheConfiguration);

        LOGGER.info("Loading EHCache configuration from {}", ehCacheConfiguration);

        if (! ehCacheConfigurationFile.exists()) {
            LOGGER.warn("No configuration file can be found for EHCache");
            throw new IllegalStateException("No configuration file can be found for EHCache");
        }

        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new FileSystemResource(ehCacheConfigurationFile));
        factoryBean.setShared(true);
        return factoryBean;
    }
}
