Unique-Web
====

[![BSD License](http://img.shields.io/hexpm/l/plug.svg)](https://github.com/biezhi/unique-web)
[![Build Status](http://img.shields.io/travis/joyent/node/v0.6.svg)](https://github.com/biezhi/unique-web)

![mahua](http://i2.tietuku.com/630edadd7481f675.png)
##Unique-Web是什么?
 * 一款轻量级的javaweb开发框架

 * 帮助更多java开发者快速完成功能

##帮助文档
这是旧版本的，新版还在编写中。PDF下载：[unique-web-guide.pdf](http://imdoc.qiniudn.com/unique-web-guide.pdf)

##特性概览？

+ 基础的webmvc
    + 支持`rest`风格的url访问
    + 支持注解式的配置
+ 一个基础的`ioc`容器和实现

+ 扩展的ORM插件（DAO底层基于`dbutils`，完成大部分数据库操作）
+ 基于模板引擎的视图渲染支持
+ `redis`缓存支持, 只需一个配置即可不加代码进行缓存
+ `mongo`支持
+ 多层拦截器的设计
+ 任务调度支持
+ 邮件发送支持
+ 多彩验证码支持
+ 图片处理支持
+ 文件上传支持
+ 通用工具类库

##待开发的功能
+ 声明式事务管理
+ i18n国际化

##问题反馈
在使用过程中如果您有遇到问题可以通过以下方式和我沟通

+ Email(biezhi.me@gmail.com)
+ QQ: 921293209
+ weibo: (http://weibo.com/u/5238733773)
+ oschina: http://my.oschina.net/biezhi

##加入开源队伍
在兴趣的驱动下,写一些`免费`的东西，有欣喜，也还有汗水，如果你也有这份对技术的追求可以抽空一起交流，加入我们的行列，后期还有blog、sns这样的开源项目有待开展。

##关于作者

```java
  public static void main(String[] args) {
    	Map<String, Object> author = new HashMap<String, Object>();
		author.put("nickName", "王爵");
		author.put("blog", "http://blog.biezhi.me");
  }
```


  [1]: https://github.com/biezhi/unique-web/blob/master/en.md