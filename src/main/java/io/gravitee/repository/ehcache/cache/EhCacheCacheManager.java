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

import io.gravitee.repository.cache.model.Cache;
import io.gravitee.repository.cache.api.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author David BRASSELY (david at gravitee.io)
 * @author GraviteeSource Team
 */
@Component
public class EhCacheCacheManager implements CacheManager {

    @Autowired
    private org.springframework.cache.CacheManager cacheManager;

    private ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    @Override
    public Cache getCache(String name) {
        org.springframework.cache.Cache cache = cacheManager.getCache(name);
        return (cache == null) ? null : caches.computeIfAbsent(name,
                s -> new EhCacheDelegate((Ehcache) cache.getNativeCache()));
    }
}
