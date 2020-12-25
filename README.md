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

