# 计算机网络作业相关说明
秦锴 2016220302008

一个计算机网络的小作业，完成一次简单http请求的分析

## 目录结构

    .
    ├── Log
    │   ├── error.log
    │   └── log.log
    ├── out
    │   ├── artifacts
    │   │   └── simpleHttpServer_jar
    │   │       ├── dom4j-1.6.1.jar
    │   │       ├── log4j-1.2.17.jar
    │   │       └── simpleHttpServer.jar
    │   └── production
    │       └── simpleHttpServer
    │           ├── 404.html
    │           ├── META-INF
    │           │   └── MANIFEST.MF
    │           ├── UI
    │           │   ├── Entry.class
    │           │   ├── HttpServerUI$1.class
    │           │   ├── HttpServerUI$2.class
    │           │   ├── HttpServerUI$3.class
    │           │   ├── HttpServerUI$4.class    
    │           │   └── HttpServerUI.class
    │           ├── context
    │           │   ├── Context.class
    │           │   ├── Request.class
    │           │   ├── Response.class
    │           │   └── impl
    │           │       ├── HttpContext.class
    │           │       ├── HttpRequest.class
    │           │       └── HttpResponse.class
    │           ├── handler
    │           │   ├── Handler.class
    │           │   ├── HttpHandler.class
    │           │   ├── MapHandler.class
    │           │   ├── ResponseHandler.class
    │           │   ├── abs
    │           │   │   └── AbstractHandler.class
    │           │   └── impl
    │           │       ├── HomeHandler.class
    │           │       └── NotFoundHandler.class
    │           ├── home.html
    │           ├── lib
    │           │   ├── dom4j-1.6.1.jar
    │           │   └── log4j-1.2.17.jar
    │           ├── log4j.properties
    │           ├── server
    │           │   └── Server.class
    │           ├── server.xml
    │           ├── utils
    │           │   ├── LogAppender.class
    │           │   ├── TextAreaLogAppender.class
    │           │   └── XMLUtil.class
    │           └── web.xml
    ├── simpleHttpServer.iml
    └── src
        ├── 404.html
        ├── META-INF
        │   └── MANIFEST.MF
        ├── UI
        │   ├── Entry.java
        │   └── HttpServerUI.java
        ├── context
        │   ├── Context.java
        │   ├── Request.java
        │   ├── Response.java
        │   └── impl
        │       ├── HttpContext.java
        │       ├── HttpRequest.java
        │       └── HttpResponse.java
        ├── handler
        │   ├── Handler.java
        │   ├── HttpHandler.java
        │   ├── MapHandler.java
        │   ├── ResponseHandler.java
        │   ├── abs
        │   │   └── AbstractHandler.java
        │   └── impl
        │       ├── HomeHandler.java
        │       └── NotFoundHandler.java
        ├── home.html
        ├── lib
        │   ├── dom4j-1.6.1.jar
        │   └── log4j-1.2.17.jar
        ├── log4j.properties
        ├── server
        │   └── Server.java
        ├── server.xml
        ├── utils
        │   ├── LogAppender.java
        │   ├── TextAreaLogAppender.java
        │   └── XMLUtil.java
        └── web.xml

## 可执行jar以及使用方法

可执行jar包位于 ./out/artifacts/simpleHttpServer_jar/simpleHttpServer.jar

首先进入项目根目录，即在simpleHttpServer文件夹下

然后使用 java -jar ./out/artifacts/simpleHttpServer.jar 即可运行本服务器

## 源码说明

源码位于 ./src 内

### context交互包

**context**包内为交互相关接口，其中的**impl**文件夹内为相关接口的实现类

对于交互，我设计了一个*Request*接口和一个*Response*接口以及一个**Context**抽象类，这个抽象类用以获取交互信息，以及设置资源等，位于impl包内的即为这些接口和抽象类的实现类。

### handler页面处理包

**handler**包内为页面相关处理接口和类，其中的**abs**文件夹内有**handler**的抽象类，**impl**文件夹内有抽象类的实现类。

其中*MapHandler*用以解析*web.xml*，以完成*uri*到对应*handler*的映射。*httpHandler*负责处理一次*http*请求，通过不同的uri启动不同的handler进行处理。*ResponseHandler*负责处理页面的响应结果。对于相关页面处理的*handler*，我首先写了一个*handler*的接口，然后对于这个接口我写了一个*AbstractHandler*抽象类，相关页面处理的*handler*只需重写抽象类的方法即可

### lib以及lib资源包

**lib**文件夹内为所用到的*jar*包

**res**文件夹内为所用到的图片

### META-INF文件夹

**META-INF**文件夹内为*main class*信息

### server服务器包

**server**文件夹内为服务器类

使用java nio进行监听，完成服务器监听线程

### UI页面显示包

**UI**文件夹内为*main class*以及*GUI*相关类

**HttpServerUi**主要完成了页面的设置，用户动作的监听，以及log4j的输出管道绑定，而**Entry**则是整个程序的入口

### utils辅助类包

**utils**内为辅助类，**LogAppender**为log4j的扩展虚拟类，其将log4j的输出传给管道，然后送到GUI组件中显示，**TextAreaLogAppender**则是LogAppender的具体实现类，完成了将管道的内容添加到相关textarea中并实现了，滚动条自动跟随滚动到最后。至于**XMLUtil**则负责完成对xml文件的解析和更新

### 其他

除此以外，**web.xml**文件用于配置页面的相关*handler*映射，**server.xml**用于配置服务器的相关信息，**log4j.properties**用于配置*log4j*的输出，另外两个*html*文件分别为主页面以及404页面

## 日志

日志共有两个分别为**error.log**以及**log.log**，分别用于存储错误日志和普通日志

## OUT

**out**文件夹内的**production**文件夹内为源码编译出的class文件，**artifacts**文件夹内为项目打包后的*jar*包