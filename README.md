# Cool-RPC

---

## Description
这是一个简单、高效、可拓展的RPC框架

[博客讲解](https://segmentfault.com/a/1190000016185800)


## How to pack
> clone代码后，在项目根目录下执行**install.bat**即可:


## How to use

##### 服务提供端

> 如果你想在cool-rpc上开放一个api接口，在api接口实现类上添加@CoolService(api接口.class)注解

##### 服务消费端

> 如果你想调用cool-rpc上的开放接口，需在调用类中依赖注入CoolProxy，例如：

```
@Resource
CoolProxy coolProxy;
```
> 之后在方法中使用getInstance()获取api接口代理实例即可，例如:

```
Object obj = coolProxy.getInstance(Object.class);
```

## How to develop

> clone代码之后，新建属于自己的分支，开发完之后push，等待master合并


### Thanks
netty4+

spring

zookeeper

maven








