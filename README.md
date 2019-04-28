# miaosha
### 技术栈：
  后端：SpringBoot,MyBatis <p>
  数据库：Mysql <p>
  负载均衡: nginx <p>
  缓存：redis <p>
  前端：thymeleaf,js <p>
  压测：Jmeter <p>
  构建工具：maven <p>
  第三方：阿里巴巴SMS短信，driud连接池及监控
    
    

#### 页面优化：
1.  页面缓存+URL缓存+对象缓存
2.  页面静态化，前后端分离（通过ajax实现接口数据传递）
3.  静态资源优化
    - JS/CSS压缩，减少流量  （使用压缩版）
    - 多个JS./CSS组合，减少连接数     
    - 配置超长时间的本地缓存
    - 静态资源CDN部署
      
#### 解决超卖问题：
      1.  数据库加唯一索引（用户id与商品id）：防止用户重复购买
      2.  SQL加库存数量判断：防止库存变成负数
      
#### 秒杀操作优化核心：减少数据库访问
      1.  系统初始化，把商品库存数量加载到Redis
      2.  收到请求，Redis预减库存，库存不足，直接返回，否则进入3
      3.  请求入队，，立刻返回排队中
      4.  请求出队，生成订单，减少库存
      5.  客户端轮询，是否秒杀成功
      注：内置本地判断库存 设置标记，减少对redis的访问
      
