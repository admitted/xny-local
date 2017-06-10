# 新能源物联网架构

![](http://oov0wb0gl.bkt.clouddn.com/2017-06-09-14970132441903.jpg)


## 展示 
用户使用场景

#### PC端
传统的PC端，使用网页界面。使用了HTML5、jQuery、JavaScript等前端技术。

#### 移动端
新兴的移动端，使用APP、微信小程序等。使用了HTML5、jQuery、JavaScript等前端技术。


## 云服务端
所有的服务均架设在云端服务器上

#### RMI
RMI(Remote Method Invoke)远程方法调用，为用户访问提供接口(HTTP/HTTPS协议)，为展示页面提供后台支持。使用JAVA、Servlet、RMI、SOCKET通讯等技术。

#### DB
DB(Data Base)数据库，数据存储中心，利用触发器、存储过程、函数、事件、视图等数据库技术来满足存储需求。

#### SVR
SVR（Service）整个云端服务的中心，通过SVR 将 RMI 、TPC 和 DB 紧密联系起来。使用 Java 、ActiveMQ 、SOCKET 多线程等技术。

#### TPC
TPC(Transaction Process Center)业务处理中心，中转站。负责业务分发，数据解析，数据汇总，数据存储等工作。使用JAVA、JMS、SOCKET通讯、多线程等技术。

#### RMI连接DB
RMI从数据库获取数据供用户端显示用，根据用户端操作即时更新数据库。利用JDBC技术。

#### RMI连接TPC
用户端的操作指令通过RMI提交给TPC，由TPC分发给对应的设备执行。利用SOCKET通讯。

#### TPC连接DB
设备端上传的数据进行存储。利用JDBC技术

#### TPC连接CPM&DTU
利用TPC/IP协议，进行控制下发、数据上传。CPM走网络数据，DTU走GPRS流量。


## 终端(设备端)
物联网架构中，将设备端架设在实际应用场景，用以部署传感器、控制设备。

#### CPM
CPM(Center Process Machine)中央处理器，及采集、分析、联动、报警、控制为一体的核心物联网设备，架设在应用场景。用户可以直接使用它，也可以通过云端来使用。使用技术：(C++)(内存管理、消息队列、多线程、TCP/IP、sqlite3等）。

#### DTU
DTU (Data Transfer unit)数据传输单元，简化版的采集终端，数据走GPRS流量。在一些简单的应用场景，使用它可以减少硬件成本，以及降低施工难度。

#### CPM&DTU连接设备
物联网现有传输传输协议多数为232&485总线协议，也有少部分TCP/IP协议。根据实际情况，总线协议的设备可以通过CPM&DTU采集数据进行上传；TCP/IP协议设备可以直接连接TPC进行数据上传。

#### 其他设备连接TPC
TCP/IP协议设备可以直接连接TPC进行数据上传。
                        

