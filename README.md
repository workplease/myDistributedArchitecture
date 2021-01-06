# myDistributedArchitecture
自定义分布式框架

## myDistributedArchitecture 项目说明

### 1. 实现 RPC 框架：

1）RPC 框架原理

RPC（Remote Procedure Call，远程调用）一般来实现部署在不同机器上的系统之间的方法调用，使得程序能够像访问本地系统资源一样，通过网络传输去访问远端系统资源，RPC 框架实现的架构原理都是类似的。

其中包含几个重要角色：

- Client Code：客户端调用方代码实现，负责发起 RPC 调用，为调用方用户提供使用 API
- Serialization/Deserialization：负责对 RPC 调用通过网络传输的内容进行序列与反序列化，不同的 RPC 框架有不同的实现机制，主要分为文本与二进制两大类。文本类别的序列化机制主要有 XML 与 JSON 两种格式，二进制的序列化机制常见的是 Java 原生的序列化机制
- Stub Proxy：可以看作是一种代理对象，屏蔽 RPC 调用过程中复杂的网络处理逻辑，使得 RPC 调用透明化，能够保持与本地调用一样的代码风格
- Transport：作为 PRC 框架底层的通信传输模块，一般通过 Socket 在客户端与服务端之间传递请求与应答消息
- Server Code：服务端服务业务逻辑具体的实现

2）常见的 RPC 框架：

- Java RMI（Remote Method Invocation）是一种基于 Java 的远程方法调用技术，是 Java 特有的一种 RPC 实现，它能够使部署在不同主机上的 Java 对象之间进行透明的通信与方法调用，特性如下：
  - 支持真正的面向对象的多态性，而完全支持面向对象也是 RMI 相对于其他 PRC 框架的优势之一
  - Java 语言独有，不支持其他语言，能够完美地支持 Java 语言独有的特性
  - 使用了 Java 原生的序列化机制，所有序列化对象必须实现 java.io.Serializable 接口
  - 底层通信基于 BIO（同步阻塞 I/O）实现的 Socket 实现
  - 因 Java 院士序列化机制与 BIO 通信机制本身存在性能问题，导致 RMI 的性能较差，对性能要求高的使用场景不推荐该方案
- Apache CXF 是一个开源的 WebService RPC 框架，是两个著名的开源项目 ObjectWeb Celtix 和 Codehaus XFire 合并后的产物。Apache CXF 是一个范围广泛，功能齐全的集合，特点如下：
  - 支持 Web Services 标准，包括 SOAP（SOAP1.1 和 SOAP1.2）规范，WSI Basic Profile、WSDL、WS-Addressing、WS-Policy、WS-ReliableMessageing、WS-Security 等
  - 支持 JSR 相关规范和标准，包括 JAX-WS（Java API for XML-Based Web Services2.0）、JAX-RS（The Java API for RESTful Web Services）、SAAJ（SOAP with Attachments API for Java）
  - 支持多种传输协议和协议绑定、数据绑定
    - 协议绑定：SOAP、REST/HTTP、XML。
    - 数据绑定：JAXB 2.X、Aegis、Apache XMLBeans 等。
- Apache Axis2 是 Axis 的后续版本，是新一代的 SOAP 引擎，是 CXF 之外另一个分成流行的 Web Services/SOAP/WSDL 实现，且同时存在 Java 语言与 C 语言两种实现，特点如下：
  - 高性能：Axis2 具有自己的轻量级对象模型 AXIOM，且采用 StAX（Streaming API for XML）技术，具有更优秀的性能表现，比 Axis.x 的内存消耗更低
  - 热部署：Axis2 配备了在系统启动和运行时部署 Web 服务和处理程序的功能，换句话说，可以将新服务添加到系统，而不必关闭服务器。只需将所需的 Web 服务归档存储在存储库的 services 目录中，部署模型将自动部署该服务并使其可供使用
  - 同步服务支持：Axis2 支持使用非阻塞客户端和传输的异步，以及 Web 服务和异步 Web 服务调用
  - WSDL 支持：Axis2 支持 Web 服务描述语言版本 1.1 和 2.0，它允许轻松构建存根以访问远程服务，还可以自动导出来自 Axis2 的已部署服务的机器可读描述
- Apache Thirft 是跨越不同的平台和语言，协助构建可伸缩的分布式系统的一种 RPC 实现，它的特点是具备广泛的语言支持以及高性能。Apache Thirft 作为在多语言并存的异构系统之间的 RPC 调用方案是一个非常不错的选择，当前也可以作为同构系统之间的 RPC 方案，有着非常明显的性能优势。原因在于，Thrift 是采用二进制编码协议、使用 TCP/IP 传输协议的一种 RPC 实现，而 XML-RPC/JSON=RPC/SOAP 与 WSDL 协议栈采用文本协议，WSDL 的实现 WebService 采用 HTTP 作为传输协议。对于网络数据传输，TCP/IP 协议的性能要高于 HTTP 协议，不仅因为 HTTP 协议是应用层协议，HTTP 协议传输内容除应用数据本身外，还带有不少描述本次请求上下文的数据（比如响应状态码、Header 信息等）。此外，HTTP 协议一般使用文本协议对传输内容进行编码，相对于一般采用二进制编码协议的 TCP/IP 协议码流要大。
- gRPC 是一个高性能、开源和通用的 RPC 框架，面向移动和 HTTP/2 设计。
  - 其基于 HTTP/2 标准设计，带来诸如双向流、流控、头部压缩、单 TCP 连接上的多复用请求等特性。这些特性使得其在移动设备上表现更好，更省电和节省空间占用。
  - 在 gPRC 里客户端可以像调用本地对象一样调用另一台不同机器上服务端应用的方法，能够更容易地创建分布式应用和服务。
  - gRPC 客户端和服务端可以在多种环境中运行和交互——从 Google 内部的服务器到你自己的 PC，并且可以用任何 gRPC 支持的语言来编写。
- Apache HttpComponents 分为 HttpClient 与 HttpCore 两个模块，其中 HttpClient 提供了可以直接使用的面向用户方法，为 HttpCore 提供了较低层次的 HttpAPI，可以用来定制个性化的客户端与服务端 HTTP 服务。

3）实现自己的 RPC 框架：

通过使用 Java Socket 来实现一个简单的 RPC 框架。

整个调用流程有四个角色：

- Service API：定义对外服务的接口规范。
- Consumer Proxy：Service API 接口的代理类，内部逻辑通过 Socket 与服务的提供者进行通信，包括写入调用参数与获取调用返回的结果对象，通过代理使通信及获取返回结果等复杂逻辑接口调用方透明。
- Provider Reflect：服务的提供方，通过接收 Consumer Proxy 通过 Socket 写入的参数，定位到具体的服务实现，并通过 Java 反射技术实现服务的调用，然后将调用结果写入 Socket，返回到 Comsumer Proxy。
- Service Impl：远程服务的实现类。

相关的类：

- HelloService：服务接口，对应于 Service API 角色。
- HelloServiceImpl：远程服务接口实现，对应于 Service Impl 角色。
- Consumer Proxy：服务消费者代理类，对应于 Consumer Proxy 角色。通过实现服务接口的动态代理对象获得服务接口的动态代理实例 Proxy.newProxyInstance，通过实现 InvocationHandler 接口中的方法 public Object invoke(Objct proxy,Method method,Object[] arguments) 来完成远程 RPC 调用。具体通过 Java 对象输出流 ObjectOutputStream 将调用接口的方法以及参数写入 Socket，发起远程调用。之后通过 Java 对象输入流 ObjectInputStream 从 Socket 中获得返回结果。
- ProviderReflect：服务发布实现类，对应 Provider Reflect 角色。通过 Java 对象输入流 ObjectInputStream 从 Socket 中按照 ConsumerProxy 的写入顺序逐一获取调用方法名称及方法参数，通过 lang3 中的工具方法 MethodUtils.invokeExactMethod 对服务实现类发起反射调用，将调用结果写入 Socket 返回给调用方。
- RpcProviderMain：发布服务，以便接收调用。
- RpConsumerMain：服务的调用方发起调用。

4）RPC 框架与分布式服务框架的区别：

RPC 实现了服务消费者调用方 Client 与服务提供实现方 Server 之间的点对点调用流程，一般来说，包括 stub、通信、数据的序列化与反序列化。调用方与服务提供方一般采用直连的调用方式。

而分布式服务框架，除了包括 RPC 的特性，还包括多台 Server 提供服务的负载均衡策略及实现，服务的注册、发布与引入，以及服务的高可用策略、服务治理等特性。


### 2. 分布式服务框架总体架构与功能：

1）面向服务的体系架构（SOA）：

面向服务的架构（SOA）是伴随着互联网快速发展产生的一种系统架构方法。

面向服务是一种设计范式，用于创建解决方案的逻辑单元，这些逻辑单元可组合、可复用，以支持实现面向服务计算的特定战略目标和收益。

2）分布式服务框架现实需求：

SOA 面向服务的架构体系所需要解决的技术问题中最重要也是最基本的，便是如何实现服务之间的通信。上面实现的自定义 RPC 框架并没有实现服务治理机制。一般来说，服务治理包括服务依赖梳理、负载均衡、服务分组灰度发布、链路监控、服务质量统计、服务自动发现、自动下线与服务注册中心等功能。

可以简单地认为 RPC 框架与服务治理组合起来，就构成了一套完整的分布式服务框架。实现 SOA 架构最重要的便是选择一套合适的分布式服务框架，分布式服务框架是 SOA 能够最终成功落地实现价值交付的技术保障。

RPC 框架实现 SOA 服务化之后服务之间的通信，服务治理实现服务运维，指导服务化更好的快速持续演进。

3）分布式服务框架总体架构及所需的技术概述：

分布式服务框架主要包括服务消费端、服务提供端、服务数据网络传输的序列化与反序列化、服务数据的通信机制、服务注册中心、服务治理这几个组成部分。

分布式服务框架由服务提供端、服务消费端、服务注册中心、此服务治理四部分组成。

![分布式服务框架总体架构](D:\真Java\我的JAVA笔记\项目\图片\分布式服务框架总体架构.png)

- 服务提供端提供服务，将服务提供者信息注册到服务注册中心，服务提供者信息一般包括服务主机地址、端口、服务接口信息等。
- 服务消费端将服务提供者信息从服务注册中心获取到本地缓存，同时将服务消费者信息上报到服务注册中心。
- 服务消费端根据某种软负载策略选择某一个服务提供者发起服务调用，服务调用首先采用某些数据序列化方案，将调用数据序列化为可以在网络传输的字节数组，采用某种 NIO 框架（如 Netty、Mina、Grizzy）完成调用。
- 为了管理大规模的服务依赖关系，需要提供服务治理功能。

后面也是根据该思路去实现对应的功能。

### 3.分布式服务框架序列化与反序列化实现：

1）序列化原理及常用的序列化介绍：

序列化（Serialization）是将对象的状态信息转换为可存储或传输的形式过程。简言之，把对象转换为字节序列的过程称为对象的序列化。

而反序列化（Deserialization）是序列化的逆过程。将字节数组反序列化为对象，把字节序列恢复为对象的过程称为对象的反序列化。

序列化能帮助我们解决如下问题：

- 通过将对象序列化为字节数组，使得不共享内存通过网络连接的系统之间能够进行对象的传输。
- 通过将对象序列化为字节数组，能够将对象永久存储到存储设备。
- 解决远程接口调用 JVM 之间内存无法共享的问题。

2）Java 默认的序列化：

Java 的序列化主要通过对象输出流 java.io.ObjectOutputStream 与对象输入流 java.io.ObjectInputStream 来实现，其中被序列化的类需要实现 java.io.Serializable 接口。

优点：

- Java 语言自带，无须额外引入第三方依赖。
- 与 Java 语言有天然的最好的易用性与亲和性。

缺点：

- 只支持 Java 语言，不支持跨语言。
- Java 默认序列化性能欠佳，序列化后产生的码流过大，对于引用过深的对象序列化易发生内存 OOM 异常。

3）XML 序列化框架介绍：

XML 序列化的优势在于可读性好，利于调试。因为使用标签对来表示数据，导致序列化后码流大，而且效率不高。适用于对性能要求不高，且 QPS 较低的企业级内部系统之间的数据交换的场景，又因 XML 具有语言无关性，可用于异构系统之间的数据交换协议。

XML 序列化/反序列化有多种实现方式，本次只实现 XStream 和 Java 自带的 XML 序列化/反序列化两种方法。

4）JSON 序列化框架介绍：

JSON 是一种轻量级的数据交换格式。相比 XML，JSON 码流更小，而且保留了 XML 可读性好的优势。

JSON 序列化常用的开源工具有以下几个：Jackson、fastjson 和 GSON。

相比较而言，Jackson 与 fastjson 比 GSON 的性能要好。Jackson、GSON 相对 fastjson 稳定性更好，fastjson 对序列化对象本身有一些额外的要求，比如序列化对象的属性必须实现 get/set 方法才能完成对该属性的序列化。fastjson 的优势在于非常易用的 API 操作及高性能。

5）Hessian 序列化框架介绍：

Hessian 是一个支持跨语言传输的二进制序列化协议，相对于 Java 默认的序列化机制，Hessian 具有更好的性能与易用性，而且支持多种不同的语言。

6）protobuf 序列化框架介绍：

protobuf 是一种数据交换的格式，它独立于语言，独立于平台。protobuf 是一个纯粹的展示层协议，可以和各种传输层协议一起使用，它本身的文档也非常完善。

protobuf 具有广泛的用户基础，空间开销小及高解析性能是其亮点，非常适合于公司内部对性能要求高的 RPC 调用。由于其解析性能高，序列化后数据量相对少，也适合应用层对象的持久化场景。

它的主要问题在于需要编写 .proto IDL 文件，使用起来工作量稍大，且需要额外学习 proto IDL 特有的语法，增加了额外的学习成本。

7）protostuff 序列化框架介绍：

protostuff 基于 protobuf，其中，protostuff-runtime 实现了无须编译对 Java Bean 进行 protobuf 序列化/反序列化的能力。

对于仅使用 Java 语言，且无须跨语言的使用场景，protostuff 继承了 protobuf 的高性能的同时免去了编写 .proto 文件的麻烦。

8）Thrift 序列化框架介绍：

与 protobuf 类似，使用 Thrift 之前，需要编写以 .thrift 结尾的 IDL 文件，再使用 Thrift 提供的编译器生成对应的代码。对 Java 而言，所有生成的 Java Bean 都继承了类 org.apache.thrift.TBase。

9）Avro 序列化框架介绍：

Avro 提供的功能类似于其他编组系统，如 Thrift、protobuf 等，而 Avro 的主要不同之处在于以下几点。

- 动态类型：Avro  无须生成代码。数据总是伴以模式定义，这样就可以在不生成代码、静态数据类型的情况下对数据进行所有处理，有利于构建通用的数据处理系统和语言。
- 无标记数据：由于在读取数据时有模式定义，这就大大减少了数据编辑所需的类型信息，从而减少序列化空间开销。
- 不用手动分配的字段 ID：当数据模式发生变化，处理数据时总是同时提供新旧模式，差异就可以用字段名来做符号化的分析。

Avro 具有如下特性：

- 丰富的数据结构类型
- 快速可压缩的二进制数据形式
- 存储持久数据的文件容器
- 远程过程调用 RPC
- 简单的动态语言结合功能，Avro 和动态语言结合后，读写数据文件和使用 RPC 协议都不需要生成代码，而代码生成作为一种可以选的优化只值得在静态类型语言中实现。

10）JBoss Marshalling 序列化框架介绍：

JBoss Marshalling 是一个 Java 对象序列化包，兼容 Java 原生的序列化机制，对 Java 原生序列化机制作类优化，使其在性能上有很大提升。在保持跟 java.io.Serializable 接口兼容的同时增加了一些可调的参数和附加特性，这些参数和附加的特性，可通过工厂类进行配置，对原生 Java 序列化是一个很好的代替。

11）序列化框架的选型：

- 对于公司间的系统调用，性能要求在 100ms 以上的服务，基于 XML 的 SOAP 协议是一个值得考虑的方案。
- 基于 Web  Browser 的 Ajax，以及 Mobile App 与服务端之间的通信，JSON 协议是首选。对于性能要求不高，或者以动态类型语言为主，或者传输数据载荷很小的运用场景，JSON 也是一个不错的选择。
- 对于调试环境比较恶劣的场景，采用 JSON 或 XML 能够极大地提高调式效率，降低系统开发成本。
- 对性能和简洁性有极高要求的场景，Hessian、protobuf、Thrift、Avro 之间具有一定的竞争关系。其中 Hessian 是在性能和稳定性同时考虑下最优的序列化协议。
- 对于 T 级别的数据的持久化应用场景，protobuf 和 Avro 是首要选择。如果持久化后的数据存储在 Hadoop 子项目里，Avro 会是更好的选择。
- 由于 Avro 的设计理念偏向于动态类型语言，对于以动态语言为主的应用场景，Avro 是更好的选择。
- 对于持久层非 Hadoop 项目，以静态类型语言为主的应用场景，protobuf 会更符合静态类型语言工程师的开发习惯。
- 对需要提高一个完整的 RPC 解决方案，Thrift 是一个好的选择。
- 对序列化之后需要至此不同的传输层协议，或者需要跨防火墙访问的高性能场景，protobuf 可以优先考虑。

12）实现自己的序列化工具引擎：

依据之前的实现，整合为通用的序列化工具引擎，可以通过输入不同配置随意选择使用哪一种序列化方案。

序列化引擎 SerializerEngine 通过 static 块，在累加载的时候，将9类序列化算法注册到本地缓存 serializerMap 中，提供了相应的序列化与反序列化的通用处理方法。

- 序列化方法 public static <T\> byte[] serialize(T obj,String serializeType)，该方法传入参数为待序列化对象，以及序列化方式。
- 反序列化方法 public static <T\> T deserialize(byte[] data,Class<T\> clazz,String serializeType)，该方法传入参数为字节数组、反序列结果对象类型，以及序列化方式。

通过 SerializerEngine 引擎，可以通过传入参数 serialzeType 的方式灵活选择具体的序列化/反序列化方案，做到序列化/反序列化方案的可配置化。

13）相关的类：

- ISerializer：序列化/反序列化通用接口

- AvroSerializer：Avro 实现序列化/反序列化
- DefaultJavaSerializer：Java 默认序列化
- HessianSerializer：Hessian 实现序列化/反序列化
- JSON2Serializer：使用 fastjson 实现 JSON 序列化/反序列化
- JSONSerializer：使用 Jackson 实现 JSON 序列化/反序列化
- MarshallingSerializer：JBoss Marshalling 实现序列化/反序列化
- ProtocolBufferSerializer：protobuf 实现序列化/反序列化
- ProtoStuffSerializer：protostuff 实现序列化/反序列化
- ThriftSerializer：Thrift 实现序列化/反序列化
- XML2Serializer：Java 自带方式实现 XML 序列化/反序列化
- XmlSerializer：XStream 实现 XML 序列化/反序列化
- FDateJsonDeserializer：对日期类进行反序列化
- FDateJsonSerializer：对日期类进行序列化
- SerializeType：序列化类型枚举
- SerializerEngine：通用的序列化工具引擎

### 4.实现分布式服务框架服务的发布与引入：

1）FactoryBean 原理：

鉴于 Spring 在 Java 企业级开发中的地位，我们希望分布式服务框架能够与 Spring 无缝地集成，远程服务的发布与引入与本地服务的发布与引入对于开发人员的编程界面能够保持一致。

实现原理是 Spring 通过反射机制利用 Bean 的 class 实例化 Bean，在某些情况下，实例化 Bean 过程如果涉及复杂的业务逻辑，通过 XML 配置或者注解的方式实例化这样一个对象很难实现或者实现起来不够灵活。这个时候，通过 org.springframework.beans.factory.FactoryBean 接口，采用编码的方式来实例化一个 Bean，将实例化 Bean 相关的复杂业务逻辑通过编码在方法 org.springframework.beans.factory.FactoryBean 的 getObject 来实现。

2）Spring 框架对于已有 RPC 框架集成的支持：

Spring 对常见的 RPC 框架提供了相应的集成支持，统一标准化了 RPC 服务发布与引入编程模型，简化了 RPC 服务及调用的开发流程。目前 Spring 主要支持如下的远程调用技术：

- Remote Method Invocation（RMI）：通过类 RmiProxyFactoryBean 与类 RmiServiceExporter 分别完成 RMI 服务的引入与发布。
- HTTP Invoker：Spring 提供的一种远程调用技术，使用 Java 内置的序列化算法，以及通过 HTTP 协议进行数据的传输。相应的支持类是 HttpInvokerProxyFactoryBean 和 HttpInvokerServiceExporter。与 RMI 的相同点在于数据序列化算法相同，都是采用 Java 内置的序列化算法。不同点在于 RMI 传输协议为 TCP/IP 协议，而 HTTPInvoker 为 HTTP 协议。HTTP 协议传输性能低于 TCP/IP 协议，但是不会被防火墙拦截。
- Hessian：Hessian 是一个轻量级的 Web 服务实现工具，它采用的是二进制协议，因此很适合发送二进制数据。它的一个基本原理就是把远程服务对象以二进制的方式进行接收和发送。Spring 通过 HessianProxyFactoryBean 和 HessianServiceExporter 来完成基于 Hessian 序列化协议的服务的引入与发布。
- JAX-WS：Spring 通过 JAX-WS 提供对 WS 服务的支持。类似地提供了相应的支持类 JaxWsProxyFactoryBean 与 SimpleJaxWsServiceExporter 来完成 WS 服务的引入与发布。
- JMS：相应的支持类为 JmsInvokerProxyFactoryBean 与 JmsInvokerServiceExpoter。

3）基于 RmiProxyFactoryBean 实现 RMI 与 Spring 的集成：

Spring 通过 org.springframework.remoting.rmi.RmiProxyFactoryBean 引入 RMI 服务，通过 org.springframework.remoting.rmi.RmiServiceExporter 暴露 RMI 服务，屏蔽掉了 RMI 开发的复杂性，对比 RMI 原生开发，不用关心服务端 Skeleton 和客户端 Stub 等的处理细节，其远程服务接口与实现类甚至可以不用实现 java.rmi.Remote 接口与继承类 java.rmi.server.UnicastRemoteObject。

4）基于 HttpInvokerProxyFactoryBean 实现 HTTP Invoker 与 Spring 的集成：

HTTP Invoker 作为 Spring 提供的一种新的 RPC 解决方案，其目的是为了填补 RMI 服务与基于 HTTP 服务（如 Hessian 等）之间的空白。因为 RMI 服务容易被防火墙拦截，Hessian 的序列化协议是私有协议。而 HTTP Invoker 采用与 RMI 相同的序列化协议（Java 内置序列化协议），传输采用 HTTP 协议，可以不被防火墙拦截。

5）基于 HessianProxyFactoryBean 实现 Hessian 与 Spring  的集成：

Hessian 是一个轻量级的 remoting onhttp 工具，相比 WebService，Hessian 更简单、快捷。采用的是二进制 RPC 协议，因为采用的是二进制协议，所以它很适合发送二进制数据。

6）实现自定义服务框架与 Spring 的集成思路：

服务的发布与引入通过 Spring 管理，进而远程服务发布 Bean 与远程服务引入 Bean 通过 Spring IOC 容器管理，由 Spring 管理其生命周期。实现了远程服务调用编程界面与本地 Bean 方法调用的一致性，屏蔽了远程服务调用与本地方法调用的差异性。

7）实现远程服务的发布：

远程服务的发布需要如下操作：

- 启动 Netty 服务端
- 启动 Zookeeper 服务端，将服务提供者属性信息注册到服务注册中心

远程服务发布相对应的属性包括如下：

- 服务接口 class（serviceItf）：用于注册在服务注册中心，服务调用端获取后遇换成在本地缓存，用于发起服务调用
- 服务实现 Bean（serviceObject）：用于服务调用
- 服务启动端口（serverPort）：对外发布服务作为 Netty 服务端端口
- 服务端服务超时时间（timeout）：用于控制服务端运行超时时间
- 服务提供者唯一标识（appKey）：唯一标识服务所在应用，作为 Zookeeper 服务注册路径中的子路径，用于该应用所有服务的一个命名空间
- 服务分组组名（groupName）：用于分组灰度发布，比如某个服务 A，通过配置不同的分组组名，可以使得调用端发起的调用只路由到与其配置的分组组名相同的服务提供者机器组上
- 服务提供者权重（weight）：配置该机器对外发布的服务在集群中的权重，用于软负载相关的权重算法实现
- 服务端线程数（workerThreads）：限制服务端该服务运行线程数，用于实现资源的隔离与服务端限流

我们主要做了两件事：

- 调用 NettyServer.singleton().start() 方法来启动 Netty 服务端，将服务对外发布出去，使其能够接受外部其他机器的调用请求
- 将服务信息写入 Zookeeper，保存在服务注册中心，方法 buildProviderServiceInfos() 将服务接口按照方法的粒度拆分，获得服务方法粒度的服务列表 List<ProviderService\>，然后通过调用注册中心的方法 registerCenter4Provider.registerProvider() 完成服务端信息的注册

8）实现远程服务的引入：

引入远程服务需要做的操作如下：

- 通过注册中心，将服务提供者信息获取到本地缓存列表
- 初始化 Netty 连接池
- 获取服务提供者代理对象
- 将服务消费者信息注册到注册中心

远程服务引入相对应的属性包括如下：

- 服务接口 class（targetInterface）：用来匹配从服务注册中心获取到本地缓存的服务提供者，得到匹配服务接口的服务提供者列表，再依据软负载策略选取某一个服务提供者，发起调用
- 超时时间（timeout）：服务调用超时时间，超过所设置的时间之后，调用方不再等待服务方返回结果，直接返回给调用方
- 服务 Bean（serviceObject）：远程服务生成的调用方法本地代理对象，可以看作调用方 Stub
- 负载均衡策略（clusterStrategy）：用于配置服务调用方法软负载策略，一般包括随机策略、加权随机策略、轮询策略、加权轮询策略、源地址 hash 策略等负载算法
- 服务提供者唯一标识（remoteAppKey）：与服务发布配置的 appKey 保持一致
- 服务分组组名（groupName）：与服务发布配置的 groupName 保持一致，用于实现同一个服务分组功能

主要做了四件事情：

- 通过方法 registerCenter4Consumer.initProviderMap() 从服务注册中心获取服务提供者信息到本地缓存
- 通过方法 NettyChannelPoolFactory.channelPoolFactoryInstance().initChannelPoolFactory 根据服务提供者信息初始化 Netty Channel 连接池，通过连接池可以做到 Channel 长连接复用，有利于提高服务调用性能
- 通过方法 RevokerProxyBeanFactory.singleton() 获取 RevokerProxyBeanFactory 服务提供者代理对象
- 通过方法 registerCenter4Consumer.registerInvoker 将服务调用者信息注册到 Zookeeper 服务注册中心，为此服务治理功能做数据准备

9）相关的类：

- ProviderFactoryBean：实现远程服务的发布
- RevokerFactoryBean：实现远程服务的引入
- AreaRemoteServiceNamespaceHandler：为远程服务发布自定义标签处理类
- AresRemoteReferenceNamespaceHandler：解析自定义标签的工具类
- ProviderFactoryBeanDefinitionParser：解析服务发布自定义标签
- RevokerFactoryBeanDefinitionParser：解析远程服务引入的自定义标签类

### 5. 分布式服务框架注册中心：

1）服务注册中心介绍：

分布式服务框架部署在多台不同的机器上，例如服务提供者部署在集群 A，服务调用者部署在集群 B，在服务调用的过程中，集群 A 中的机器需要与集群 B 中的机器进行通信。但这个时候可能存在以下问题：

- 集群 A 的服务调用者如何发现集群 B 中的服务提供者
- 集群 A 的服务调用者如何选择集群 B 中的某一台服务提供者机器发起调用
- 集群 B 中的服务某台提供者机器下线之后，集群 A 中的服务调用者如何感知到这台机器的下线，不再对已下线的机器发起调用
- 集群 B 提供的某个服务如何获知集群 A 中哪些机器正在消费该服务

以上问题可以通过服务注册中心来解决，采用注册中心来实时存储更新服务提供者信息及该服务的实时调用者信息。

- 在服务启动的时候，将服务提供者信息主动上报到服务注册中心进行服务注册
- 服务调用者启动的时候，将服务提供者信息从注册中心下拉到服务调用者机器本地缓存，服务调用者从本地缓存的服务提供者地址列表中，基于某种负载均衡策略选择一台服务提供者发起远程调用
- 服务注册中心能够感知服务提供者集群中某一台机器下线，将该机器服务提供者信息从服务注册中心删除，并主动通知服务调用者集群中的每一台机器，使得服务调用者不再调用该机器

注册中心有以下优点：

- 软负载及透明化服务路由：服务提供者和服务调用者之间相互解耦，服务调用者不需要硬编码服务提供者地址
- 服务动态发现及可伸缩能力：服务提供者机器增减能被服务调用者通过注册中心动态感知，而且通过增减机器可以实现服务的弹性伸缩
- 通过注册中心可以动态地监控服务运行质量及服务依赖，为服务提供服务治理能力

2）ZooKeeper 实现服务注册中心：

基于 SOA 架构的应用中，应用提供者对外服务的同时也会调用外部系统提供的服务。当应用越来越多，服务越来越多，服务之间的依赖越来越复杂，这个时候依靠人工来管理服务之间的依赖和上下线已经变得不可能。这种情况下，我们需要服务注册中心来解决服务自动动态发现、服务自动上下线等问题。

- 服务注册：服务的服务端服务启动的同时，将服务提供者的信息（主机 IP 地址、服务端口、服务接口类路径）组成的 znode 路径写入 ZooKeeper 中，注意写入的叶子节点为临时节点。

- 服务发现：服务的消费方在发起服务调用之前，会先连接到 ZooKeeper，对服务提供者节点路径注册监听器，同时获取服务提供者信息到本地缓存，发起调用的时候，调用者会从服务提供者本地缓存列表中运用某种负载均衡策略选取某一个服务提供者，对该服务提供者发起调用，最终完成了本次服务调用。

- 服务的自动下线：若服务提供者集群中某台机器下线，该机器与注册中心 ZooKeeper 的连接会断掉，因为服务注册写入信息的叶子节点写入的 znode 是临时节点。故当与 ZooKeeper 连接断掉后，该临时节点会被自动删除。同时触发服务消费端对服务提供者路径的监听器，服务消费端收到被删除服务提供者节点信息之后，刷新本地服务提供者信息缓存，从缓存中删除已下线的服务提供者信息。
- 服务的自动扩容上线：服务提供者集群新增机器，会在服务提供者 znode 下新增临时叶子节点，同时触发服务消费端对服务提供者路径的监听器，服务消费端收到新增服务提供者节点信息之后，刷新本地服务提供者信息缓存，将新加入的服务提供者信息加入服务提供者信息本地缓存中。
- 自动收集消费者信息：在服务消费端，通过将服务消费者信息写入 ZooKeeper 临时节点，一旦消费者机器下线，断开与 ZooKeeper 的连接，该临时节点将被自动删除。

3）集成 ZooKeeper 实现自己的服务注册与发现：

首先，先实现两个接口，来给出服务提供方与消费方各自的方法定义，然后定义一个类实现服务中心注册。

服务注册中心服务提供方 IRegisterCenter4Provider：

- 服务注册方法 registerProvider()
- 获取所有服务提供者信息方法 getProviderServiceMap()

服务注册中心服务消费方 IRegisterCenter4Invoker：

- 初始化服务提供者信息本地缓存 initProviderMap(String remoteAppKey, String groupName)
- 消费端获取服务提供者信息 getServiceMetaDataMap4Consume()
- 消费端对消费者信息注册方法 registerInvoker(final InvokerService invoker)

服务注册中心实现：

- 从 properies 文件获取 ZooKeeper 配置信息，依次为主机地址列表 ZK_SERVICE、会话超时时间 ZK_SESSION_TIME_OUT、连接超时时间 ZK_CONNECTION_TIME_OUT
- 方法 registerProvider() 完成服务提供者信息注册服务中心功能，核心逻辑如下：
  - 加同步锁 synchronized(RegisterCenter.class)，防止重复注册
  - 将服务提供者列表 serviceMetaData 转换为 providerServiceMap，其中，Key 服务提供者接口，value 为服务提供者服务方法列表
  - 连接 ZkClient，将服务提供者信息作为 ZooKeeper 子节点写入 ZooKeeper，需要注意的是子节点 ip|port 作为临时节点写入 ZooKeeper，原因是 ZooKeeper 会监听服务端的存活，一旦服务端下线，该临时节点会被自动删除，同时推送给服务消费端，从而达到该服务提供者信息自动下线的目的
  - 将节点注册路径注册监听器，一旦节点有变化，将自动更新本地服务提供者缓存信息 providerServiceMap
- 方法 initProviderMap(String remoteAppKey, String groupName) 完成初始化服务调用方本地缓存 serviceMetaDataMap4Consume 的功能
- 方法 registerInvoker(InvokerService invoker) 完成服务调用方注册服务中心的功能，实现思路与之前服务提供方类似

### 6. 分布式服务框架底层通信实现：

1）Linux 下实现的 I/O 模型：

先理清阻塞、非阻塞、同步、异步这4个概念：

- 阻塞：调用方发起调用请求，在没有返回结果之前，调用方线程被挂起，处于一直等待状态
- 非阻塞：非阻塞和阻塞的概念相对应，调用方发起调用请求，当前线程不会等待挂起，而会立刻返回，后续可以通过轮询等手段来获取调用结果状态
- 同步：在发出一个功能调用时，在没有得到结果之前，该调用就不返回
- 异步：异步的概念和同步相对，当一个异步过程调用发出之后，调用者不会立刻得到结果，通过回调等措施来处理这个调用

5种 I/O 模型：

- 阻塞 I/O 模型：默认情况下，所有的文件操作都是阻塞的，在进程空间中调用 revfrom，其系统调用直到数据包到达且被复制到应用进程的缓冲区中或者发生错误才返回，在此期间会一直等待，进程在从调用 recvfrom 开始到它返回的整段时间内都是被阻塞的
- 非阻塞 I/O 模型：进程把一个套接口设置成非阻塞是在通知内核。当所请求的 I/O 操作不能满足要求的时候，不把本进程投入睡眠，而是返回一个错误。也就是说当数据没有到达时并不等待，而是以一个错误返回
- I/O 复用模型：Linux 提供了 select/poll，进程通过将一个或多个 fd 传递给 select 或 poll 系统调用，阻塞在 select；这样 select/poll 可以帮我们侦测许多 fd 是否就绪。但是 select/poll 是顺序扫描 fd 是否就绪的，而且支持的 fd 数量有限。Linux 还以提供了一个 epoll 系统调用，epoll 基于事件驱动方式，而不是顺序扫描，当有 fd 就绪时，立即回调函数 rollback。
- 信号驱动异步 I/O 模型：首先开启套接口信号驱动 I/O 功能，并通过系统调用 sigaction 安装一个信号处理函数（此系统调用立即返回，进程继续工作，它是非阻塞的）。当数据报准备好被读时，就为该进程生成一个 SIGIO 信号。随即可以在信号处理程序中调用 recvfrom 来读数据报，并通知主循环数据已准备好被处理。也可以通知主循环，让它来读数据报
- 异步 I/O 模型：告知内核启动某个操作，并让内核在整个操作完成后（包括将数据从内核复制到用户自己的缓冲区）通知我们。这种情况与信号驱动模型的主要区别是信号驱动 I/O 由内核通知我们何时可以启动一个 I/O 操作；异步 I/O 模型由内核通知我们 I/O 操作何时完成


2）Java Classic I/O（Blocking I/O）网络通信实现：

在网络上，数据按照有限大小的数据报（datagram）进行传输，每个数据包分为两部分：header 首部和 payload 有效载荷。首部包含数据报目的地址与端口、来源地址与端口、检测数据是否被破坏的校验和用于保证可靠传输的各种其他管理信息。因为数据报大小有限，需要将数据分解成多个包，然后在目的地重新组合，在传输过程中，可能有数据丢失或者损坏，这个时候需要数据包重传。还有一种可能是数据包乱序到达，需要进行重排序。

Java Socket 实现分为客户端 Socket 与服务端 ServerSocket：

- 客户端 Socket：
  - 创建 Socket 对象，使用创建的 Socket 连接主机
  - 建立连接后，从 Socket 得到输入流与输出流，Socket 是全双工通道，可以使用这两个流与服务器之间相互发送数据
- 服务端 ServerSocket：
  - 绑定一个特定的端口创建 ServerSocket 对象
  - 使用 ServerSocket 的 accept() 方法监听这个端口的请求连接，accept() 会一直阻塞直到通过某个请求连接与客户端建立连接，此时 accept() 将返回客户端与服务端的连接的 Socket 对象
  - 通过 Socket 对象的 getInputStream() 与 getOutputStream() 方法获得与客户端通信的输入流与输出流，进行通信交互
  - 完成交互后关闭连接

3）Java I/O 复用网络通信实现：

NIO 实现 I/O 复用模型整个编程模型较为复杂，只讨论其中的几个核心概念，了解其优越性。

- Buffer 缓冲区：在 Classic I/O 库中，数据直接面向 Stream 写入或者读取，而在 NIO 库中，数据读取与写入面向的是 Buffer 对象，这种差异使性能得到了巨大提升。缓冲区实质上是一个数组，java.nio 库中提供了 Buffer 抽象类，基于该抽象类，实现了一系列 Java 基本类型的 Buffer 子类
- Channel 通道：Classic I/O 中的 Stream 是单向的，通过 OutputStream 实现输出流，InputStream 实现输入流。而 NIO 中的 Channel 是一个全双工通道，可以通过 Channel 实现同时读取与写入。与缓冲区不同，通道 API 主要由接口指定，不同的操作系统上通道实现会有很大差异
- Selector 选择器：Channel 在 Selector 上注册，Selector 通过不断轮询注册在其上的 Channel，能够感知到 Channel 可读或者可写事件。通过这种机制，可以使用一个或者少数几个线程管理大量的网络连接。用较少的线程处理大量的网络连接有很大好处，可以减少线程间的切换开销，而且线程本身也需要占用系统资源

4）Netty：

Netty 是著名的 NIO 开源框架，提供异步的、事件驱动的网络应用程序框架和工具，可以快速开发高性能、高可靠性的网络服务器和客户端程序。Netty 简化了 NIO 繁杂的 API 调用。

5）粘包/半包现象：

假设客户端分别发送了两个数据包 D1 和 D2 给服务端，由于服务端一次读取到的字节数是不确定的，故可能存在以下4种情况。

- 服务端分两次读取到了两个独立的数据包，分别是 D1 和 D2 ，没有粘包和拆包；
- 服务端一次接收到了两个数据包，D1 和 D2 粘合在一起，被称为 TCP 粘包；
- 服务端分两次读取到了两个数据包，第一次读取到了完整的 D1 包和 D2 包的部分内容，第二次读取到了 D2 包的剩余内容，这被称为 TCP 拆包；
- 服务端分两次读取到了两个数据包，第一次读取到了 D1 包的部分内容 D1_1，第二次读取到了 D1 包的剩余内容 D1_2 和 D2 包的整包。

出现原因：

- 应用程序写入的数据大于套接字缓冲区大小，这将会导致半包现象
- 应用程序写入数据小于套接字缓冲区大小，网卡将应用多次写入的数据发送到网络上，这将会发生粘包现象
- 当 TCP 报文长度减去 TCP 头部长度大于 MSS（最大报文长度）的时候将发生半包
- 修改方法未能及时读取套接字缓冲区数据，将发生粘包

6）Netty 粘包/半包问题解决：

解决粘包/半包问题的本质是能够区分完整的业务应用数据边界，能够按照边界完整地接受 Netty 传输的数据。

- 字符串类型的消息的编解码：

  - DelimiterBasedFrameDecoder+StringDecoder 利用特殊分隔符作为消息的结束标志
  - LineBasedFrameDecoder+StringDecoder 组合以换行符作为消息的结束标志
  - FixedLengthFrameDecoder+StringDecoder 按照固定长度获取信息

- Netty 内置的完整的编码解码方案：

  - 通过解码组合 ProtobufDecoder/ProtobufVarint32FrameDecoderProtobuf 及编码组合 ProtobufEncoder/ProtobufVarint32LengthFieldPrepender 提供对 protobuf 的编解码支持
  - 通过 ObjectDecoder 与 ObjectEncoder 提供对 Java 内置序列化编解码支持
  - 通过 MarshallingDecoder 与 MarshallingEncoder 提供了 Marshalling 序列化编解码支持，还有其他的，比如 JSON、XML、Base64、HTTP、Memcache 等编解码方案的支持

- Netty 自定义编解码器开发：

  原理是使用 int 数据类型来记录整个消息的字节数组长度，将该 int 数据作为消息的消息头一起传输，在服务端接收消息数据的时候，先接收4个字节的 int 数据类型数据，这个数据即为整个消息字节数组的长度，再接收剩余字节，直到接收的字节数组长度等于最先接收的 int 数据类型数据大小。

  - 使用 LengthFieldPrepender 与 LengthFieldBasedFrameDecoder
  - 构建自定义编解码器

7）使用 Netty 构建服务框架底层通信：

- 构建分布式服务框架 Netty 服务端：
  - Netty 服务端接收客户端发起的请求字节数组，然后通过解码 NettyDecoderHandler 将字节数组解码为对应的 Java 请求对象
  - 在实现服务端利用反射发起调用本地实现 NettyServerInvokerHandler 中，为了控制服务端服务能力，使用了 java.util.concurrent.Semaphore 做了流控处理
- 构建分布式服务框架服务调用端 Netty 客户端：
  - 选择合适的序列化协议，解决 Netty 传输过程中出现的半包/粘包问题
  - 发挥长连接的优势，对 Netty 的 Channel 通道进行复用：为使得 Channel 能够复用，编写了一个 Channel 连接池工厂类，针对每一个服务提供者地址，预先生成了一个保存 Channel 的阻塞队列
  - Netty 是异步框架，客户端发起服务调用后同步等待获取调用结果：客户端发起请求之后，不会同步等待结果返回，需要自己实现同步等待机制。具体实现思路为，为每次请求新建一个阻塞队列，返回结果的时候，存入该阻塞队列，若在超时时间内返回结果值，则调用端将该返回结果从阻塞队列中取出返回给调用方，否则超时，返回 null

- 过程步骤如下：
  - 获取服务提供者列表，提供某种软负载算法选择某一个服务提供者
  - 根据服务提供者信息从 Netty 连接池中获取对应的 Channel 连接
  - 将服务请求数据对象通过某种序列化协议编码成字节数组，通过通道 Channel 发送到服务端
  - 同步等待服务端返回调用结果，最终完成一次服务调用

8）相关的类：

- NettyServer：Netty 服务端
- NettyServerInvokeHandler：处理服务端的逻辑
- NettyDecoderHandler：编码器
- NettyEncoderHandler：解码器
- NettyChannelPoolFactory：Channel 连接池工程类
- AresResponseWrapper：Netty 异步调用返回结果的包装类
- RevokerResponseHolder：保存及操作返回结果的数据容器类
- NettyClientInvokeHandler：获取 Netty 异步调用返回的结果
- RevokerServiceCallable：Netty 请求发起调用线程


### 7. 分布式服务框架软负载实现：

1）软负载的实现原理：

负载均衡的目的是将请求按照某种策略分布到多台机器上，使得系统能够实现横向扩展，是应用实现可伸缩性的关键技术，也是系统能够应对大流量的核心技术之一，分布式服务框架中实现负载均衡是通过软件算法来实现的。

在分布式服务框架中，负载均衡是在服务消费端实现的，其实现原理是：

- 服务消费端在应用启动之初从服务注册中心获取服务提供者列表，缓存到服务调用端本地缓存
- 服务消费端发起服务调用之前，先通过某种策略或者算法从服务提供者列表本地缓存中选择本次调用的目标机器，再发起服务调用，从而完成负载均衡的功能

2）负载均衡常用算法：

- 软负载随机算法：获取服务列表大小范围内的随机数，将该随机数作为列表索引，从服务提供列表中获取服务提供者
- 软负载加权随机算法：首先根据加权数放大服务提供者列表，比如服务提供者 A 加权数为3，放大后变成 A，A，A，存放在新的服务提供者列表，然后对新的服务提供者列表应用随机算法
- 软负载轮询算法：依次按顺序获取服务提供者列表中的数据，并使用计数器记录使用过的数据索引，若数据索引引到最后一个数据，则计数器归零，重新开始新的循环
- 软负载加权轮询算法：首先根据加权数放大服务提供者列表，再在放大后的服务提供者基础上使用轮询算法获取服务提供者
- 软负载源地址 hash 算法：使用调用方 IP 地址的 hash 值，将服务列表大小取模后的值作为服务列表索引，根据该索引取值

3）实现自定义软负载机制：

定义一个软负载策略引擎类，使用门面模式，对外暴露统一简单的 API 界面，根据不同的策略配置来选取不同的策略服务。

4）软负载在分布式服务框架中的应用：

之前的底层通信需要用到软负载算法，进行更新后，整个远程通信调用的逻辑如下：

- 从服务注册中心获取服务提供者列表
- 实现软负载，根据服务调用端配置的软负载均衡策略策略 clusterStrategy，作为方法 ClusterEngine.queryClusterStrategy() 的参数，获取到算法具体实现 clusterStrategyService，调用方法 clusterStrategyService.select() 即可获得某个服务提供者
- 组装服务调用请求 AresRequest 对象
- 异步提交调用请求
- 通过阻塞队列机制同步等待请求返回结果

5）相关的类：

- ClusterStrategy：负载均衡策略算法接口
- RandomClusterStrategyImpl：软负载随机算法的实现
- WeightRandomClusterStrategyImpl：软负载加权随机算法实现
- PollingClusterStrategyImpl：软负载轮询算法实现
- WeightPollingClusterStrategyImpl：软负载加权轮询算法实现
- HashClusterStrategyImpl：软负载源地址 hash 算法实现
- ClusterEngine：自定义软负载机制策略引擎
- ClusterStrategyEnum：软负载策略类别
- RevokerProxyBeanFactory：远程服务在服务调用方的动态代理类实现

### 8. 分布式服务框架服务治理：

1）服务治理介绍：

在大规模服务化之前，系统应用之间的交互可能只是简单地通过 WebService、RMI 等 RPC 框架来实现，通过手工配置调用端服务地址进行调用，通过 F5 等硬件进行负载均衡。但是随着业务的不断演进，服务个数越来越多的时候，这种做法就陷入了瓶颈，因为服务数多本身就是问题，量变引起质变，服务数多了，会导致很多问题。

服务治理包括：

- 软负载
- 服务质量监控与服务指标数据采集
- 记录负责人
- 服务分组路由
- 服务依赖关系分析
- 服务降级
- 服务权重调整
- 服务调用链路跟踪

2）分组路由实现：

服务分组是指，在服务提供者集群中，将集群分为两组机器，机器组 A 和机器组 B，在消费者集群中，可以指定某个消费者调用某个集群，比如指定某台机器的请求全部打到机器组 A 上面，这样做的好处是可以实现灰度发布或 AB 测试功能。

服务分组的实现原理：要实现服务分组，可以在 “APP_KEY/服务类路径” 中间再分一层服务组名。加入服务组名之后，注册中心的路径变为 “根目录/APP_KEY/服务组名/服务类路径/（服务提供者或者消费者类型）/（IP、端口等信息数据）”。注册中心加入服务组名路径之后，指定消费某个服务组的消费端将该服务组下的服务提供者列表获取到本地缓存，消费端服务调用的时候，将按照指定的软负载算法从本地缓存中选取一个服务调用者发起调用。

3）简单服务依赖关系分析实现：

服务提供者信息与对应的服务消费者信息在注册中心已经存在了，所需要做的，就是提供获取服务提供者信息与消费者信息列表的接口方法，从注册中心查找对应的信息。

4）服务调用链路跟踪实现：

对每一次调用使用一个唯一标识服务之间的调用串联起来，有助于排查线上问题。

实现原理：在服务调用发起方生成标识本次调用的唯一 ID，传递到服务提供方，然后将该 ID 使用 ThreadLocal 保存起来，在应用的业务代码里面使用拦截器统一从 ThreadLocal 中获取出来。

5）相关的类：

- RegisterCenter：注册中心的实现类
- IRegisterCenter4Governance：服务治理接口
