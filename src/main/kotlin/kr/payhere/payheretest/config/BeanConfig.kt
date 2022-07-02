package kr.payhere.payheretest.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration::class)
class BeanConfig (
    private val redisConnectionFactory: RedisConnectionFactory,
): CachingConfigurerSupport() {

    private val serializer = JdkSerializationRedisSerializer(this.javaClass.classLoader)

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        return redisTemplate
    }

    @Bean
    override fun cacheManager(): CacheManager? {
        val cacheManager = SimpleCacheManager()
        val cacheConfigurationMap = HashMap<String, RedisCacheConfiguration>()
        buildRedisCacheConfig(cacheConfigurationMap, REDIS_30_MIN, (60 * 30).toLong())
        buildRedisCacheConfig(cacheConfigurationMap, REDIS_60_MIN, (60 * 60).toLong())

        val redisCacheManager = RedisCacheManager.builder(redisConnectionFactory)
            .withInitialCacheConfigurations(cacheConfigurationMap)
            .build()
        val caches = LinkedList(
            Arrays.asList(
            buildLocalMemoryCache(LOCAL_30_MIN, (60 * 30).toLong()),
            buildLocalMemoryCache(LOCAL_60_MIN, (60 * 60).toLong())
        ))
        redisCacheManager.initializeCaches()
        redisCacheManager.cacheNames.stream().forEach { name -> caches.add(redisCacheManager.getCache(name)) }
        cacheManager.setCaches(caches)
        cacheManager.initializeCaches()

        return TransactionAwareCacheManagerProxy(cacheManager)
    }

    private fun buildLocalMemoryCache(cacheName: String, second: Long): Cache {
        return CaffeineCache(cacheName, Caffeine.newBuilder()
            .expireAfterWrite(second, TimeUnit.SECONDS)
            .build())
    }

    private fun buildRedisCacheConfig(map: MutableMap<String, RedisCacheConfiguration>, cacheName: String, second: Long): RedisCacheConfiguration {
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            .entryTtl(Duration.ofSeconds(second))
        map[cacheName] = redisCacheConfiguration
        return redisCacheConfiguration
    }

    companion object {
        const val REDIS_30_MIN = "REDIS_30_MIN"
        const val REDIS_60_MIN = "REDIS_60_MIN"

        const val LOCAL_30_MIN = "CACHE_30_MIN"
        const val LOCAL_60_MIN = "CACHE_60_MIN"
    }
}