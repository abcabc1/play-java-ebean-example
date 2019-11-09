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

