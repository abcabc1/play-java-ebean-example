MOVED TO https://github.com/playframework/play-samples

example UI范例

iplay 电商平台
  account 系统账户
  
  customer 用户
    User 客户
    Store 商家
    Address 地址
     
  sale 销售订单(promotion[Activity] = Merchandise[Tag] + User[Tag] + Rule)
    OrderMain 主订单
    OrderDetail 订单明细
    OrderPackage 订单包裹
    OrderPackage 订单包裹明细
    promotion 促销(规则按照类别/级别等分类)
      Activity 促销活动
      ActivityMj 满减
      ActivityMz 满赠
      ActivityMs 秒杀
      ActivityQ  券  
  merchandise 商品
    Goods 标品
    Merchandise 商品
    Pack 套餐
    MerchandisePack 套餐商品
    
words 学习平台


graphql

	Trait	Language	Blocking	Features
1.	play.api.cache.redis.CacheApi	Scala	blocking	advanced
2.	play.api.cache.redis.CacheAsyncApi	Scala	non-blocking	advanced
3.	play.cache.redis.AsyncCacheApi	Java	non-blocking	advanced
4.	play.api.cache.SyncCacheApi	Scala	blocking	basic
5.	play.api.cache.AsyncCacheApi	Scala	non-blocking	basic
6.	play.cache.SyncCacheApi	Java	blocking	basic
7.	play.cache.AsyncCacheApi	Java	non-blocking	basic
