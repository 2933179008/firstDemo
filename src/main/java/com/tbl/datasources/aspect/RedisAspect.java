
package com.tbl.datasources.aspect;

/**
 * Redis切面处理类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-17 23:30
 *//*

@Aspect
@Component
public class RedisAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //是否开启redis缓存  true开启   false关闭
    @Value("${renren.redis.open: false}")
    private boolean open;

    @Around("execution(* com.tbl.common.utils.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if(open){
            try{
                result = point.proceed();
            }catch (Exception e){
                logger.error("redis error", e);
                throw new RRException("Redis服务异常");
            }
        }
        return result;
    }
}
*/
