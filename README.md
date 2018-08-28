# Cool-RPC

---

## Describe
这是一个简单、高效可拓展的RPC框架

## Thanks
netty4+

spring

zookeeper

maven

## How to pack
> clone代码后，在代码根目录下执行mvn命令:
~~~~
mvn install:install-file -Dfile=cool-rpc-1.0.0.jar -DgroupId=com.cool -DartifactId=cool-rpc -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true
~~~~

## How to dependency

> 项目pom中添加依赖：

```
<dependency>
    <groupId>com.cool</groupId>
    <artifactId>cool-rpc</artifactId>
    <version>1.0.0</version>
</dependency>
```

## How to use

##### 服务提供端

> 如果你想在cool-rpc上开放一个api，首先api接口需继承com.cool.rpc.annotation.RpcService接口，其次在api接口实现类上添加@CoolService(api接口.class)注解

##### 服务消费端

> 如果你想调用cool-rpc上的开放接口，在调用类中依赖注入CoolProxy，例如：

```
@Resource
CoolProxy coolProxy;
```
> 在方法中使用getInstance()获取api接口代理实例，例如:

```
Object obj = coolProxy.getInstance(Object.class);
```









