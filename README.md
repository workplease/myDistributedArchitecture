# myDistributedArchitecture
自定义分布式框架

## myDistributedArchitecture 项目说明

### 1. 实现 RPC 框架：

1）RPC 框架原理：

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

![自定义 RPC 框架实现原理](D:\真Java\我的JAVA笔记\项目\图片\自定义 RPC 框架实现原理.png)

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
