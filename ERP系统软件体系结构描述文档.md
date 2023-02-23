# 小组分工
| 学号 | 完成部分 |
| --- | --- |
| 201250208 | 添加数据层职责，添加数据层接口，修改数据库逻辑层，添加接口格式 |
| 201250209 | 模块职责，层间接口，修改销售模块用例图 |
| 201250210 | 组合视角、销售模块接口规范 |
| 201250203 | 文档框架、引言、产品概述、逻辑视角、库存模块、信息视角 |

## 1.引言
### 1.1编制目的
本报告详细完成对ERP系统（企业资源管理系统）的概要设计，达到指导详细设计和开发的目的，同时实现和测试人员及用户的沟通。
本报告面向开发人员、测试人员及最终用户而编写，是了解系统的导航。
### 1.2词汇表
| 词汇名称 | 词汇含义 | 备注 |
| --- | --- | --- |
| ERP | 企业资源管理系统 | ...... |

### 1.3参考资料
软件开发的技术基础 附录D.3
## 2.产品概述
公司规模扩大，企业业务量、办公场所、员工数都发生增长，为适应新的环境，提高 工作效率和用户满意度，现制作此产品，要求大幅增加销售额  ,提高销售人员的工作效率，能够清晰展现销售人员业绩。
## 3.逻辑视角
ERP系统中，选择了分层体系结构风格，将系统分为3层（展示层、业务逻辑层、数据层）能够很好地示意整个高层抽象。展示层包含GUI页面的实现，业务逻辑包含业务逻辑处理的实现，数据层负责数据的持久化和访问。分层体系结构的逻辑视角和逻辑设计方案如图1和图2所示。
**图1 参照体系结构风格的包图表达逻辑视角**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/26914325/1651499098760-3bcf8e96-2538-4b11-a736-7835f7c0b15f.jpeg)
**图2 软件体系结构逻辑设计方案**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/22123684/1655472776801-e5a73839-e937-4c6c-83ff-8370670800a4.jpeg)
## 4.组合视角
### 4.1开发包图
ERP系统的最终开发包设计如表1所示。
**表1 ERP系统的最终开发包设计**

| 开发包 | 依赖的其他开发包 |
| --- | --- |
| userui | usercontroller, vue类库包, vo,view api |
| saleui | salecontroller,vue类库包, vo,view api |
| inventoryui | inventorycontroller,vue类库包, vo,view api |
| financeui | financecontroller,vue类库包, vo,view api |
| hrui | hrcontroller,vue类库包, vo,view api |
| userservice | userdata |
| usercontroller | userservice |
| salesservice | userservice,inventoryservice,salesdata |
| salescontroller | salesevice |
| inventoryservice | financeservice,inventorydata |
| inventorycontroller | inventoryservice |
| financeservice | financedata,inventoryservice,
salesservice |
| financecontroller | financeservice |
| hrservice | hrdata |
| hrcontroller | hrservice |
| userdata | po,Databaseutibility |
| usermapper | po,userdata |
| salesdata | po,Databaseutibility |
| salesmapper | po,salesdata |
| inventorydata | po,
Databaseutibility |
| inventorymapper | po,inventorydata |
| financedata | po,Databaseutibility |
| financemapper | po,financedata |
| hrdata | po,Databaseutibility |
| hrmapper | hrdata, po |
| vue类库包 |  |
| databaseutility | JDBC,mysql |
| java RMI |  |
| po |  |
| vo |  |
| view api | usercontroller,
salescontroller,
inventorycontroller,
financecontroller,
hrcontroller |

ERP系统前端开发包图如图3所示，后端开发包图如图4所示。

**图3 ERP系统客户端开发包图**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/26916169/1651578647400-0d41f35f-24a1-4d3d-9d5d-26c3ac7e6058.jpeg)

**图4 ERP系统服务器端开发包图**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/26916169/1651578648317-5847d070-c1a4-4173-9fa8-376ddcc6cf13.jpeg)

### 4.2运行时进程
在ERP系统中，会有多个客户端进程和一个服务器端进程，其进程图如图5所示。结合部署图，客户端进程是在客户端机器上运行，服务器端进程是在服务器端机器上运行。
**图5 进程图**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/26916169/1651576790982-9b5404ef-9246-4b83-8600-af0278000136.jpeg)
### 4.3物理部署
![](https://cdn.nlark.com/yuque/0/2022/jpeg/26916169/1651638022695-9b4f06ab-4dc5-4574-9527-d80bab02c31b.jpeg)
**图6 部署图**

## 5.接口视角
### 5.1模块的职责

**图7 客户端模块视图**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/25521821/1651564475744-36da6625-1dc5-4881-a44a-cca758a69213.jpeg)
**图8 服务器端模块视图**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/25521821/1651579736841-ce96fb2f-95dd-4aa9-bcba-2f8ca2539c8e.jpeg)
**表2 客户端各层的职责**

| 层 | 职责 |
| --- | --- |
| 启动模块 | 负责初始化网络通信机制，启动用户界面 |
| 用户界面层 | 基于vue的ERP客户端 |
| 客户端网络模块 | 利用http与服务器端通信 |

** 表3 服务器端各层的职责**

| 层 | 职责 |
| --- | --- |
| 启动模块 | 负责初始化网络通信机制，启动用户界面 |
| 业务逻辑层 | 对客户端传入的的指令运行业务处理逻辑并传回结果数据 |
| 服务器端网络模块 | 利用http与服务器端通信 |
| 数据层 | 负责数据的持久化及数据访问接口 |

	每一层只是使用下方直接接触的层。层与层之间仅仅是通过接口的调用来完成的。层之间调用的接口如表4所示。
**表4 层之间调用的接口**

| 接口 | 服务调用方 | 服务提供方 |
| --- | --- | --- |
| UserService
SalesService
InventoryService
FinanceService
HrService | 客户端展示层 | 服务器端业务逻辑层 |
| UserMapper
SalesMapper
InventoryMapper
FinanceMapper
HrMapper | 服务器端业务逻辑层 | 服务器端数据层 |

	借用销售用例中的客户模块、库存商品部分用例来说明层之间的调用。
**图9 销售用例层之间调用的接口**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/25521821/1651593100644-84abcf70-6351-4bbb-a1ac-4b1d8b3d090d.jpeg)
** 图10 库存用例之间调用的接口**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/25521821/1651593104640-fa5d9df5-b9da-46cd-a0d1-d43fff4d543b.jpeg)
### 5.2库存模块的分解
**图11 库存模块类**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/26914325/1651590667253-521ba8d5-b346-4379-8b00-b087eab9cae5.jpeg)
#### 5.2.1库存模块的职责
**表5 库存模块的职责**

| 模块 | 职责 |
| --- | --- |
| InventoryController | 负责库存界面的所需要的服务  |

#### 5.2.2库存模块的接口规范
**表6 库存模块的提供的接口规范**
##### Inventory模块
提供的服务（供接口）

| 接口 | 语法 | 前置条件 | 后置条件 |
| --- | --- | --- | --- |
| InventoryController.searchItem | selectItem(String keyword) | 非空 | 展示商品搜索结果 |
| InventoryController.searchItem | selectItem(int key) | 非空 | 展示商品搜索结果 |
| InventoryController.check | check() | 非空 | 展示当天的各种商品的名称，型号，库存数量， 库存均价（商品的平均进价），批次批号，生产日期，并且显示行号。 Excel   |
| InventoryController.viewInventory | viewInventory(Date start,Date end) | 非空 | 查看此时间段内的出/入库数量/金额/商品信息/分类信息   |
| InventoryController.alertReceipt | alertReceipt(Receipt receipt) | 非空 | 向仓库管理员发送消息   |
| InventoryController.giftReceipt | giftReceipt(Receipt receipt) | 非空 | 库存信息进行更新 |
| InventoryController.overflowReceipt | overflowReceipt(Receipt receipt) | 非空 | 库存信息进行更新，保持一致 |
| InventoryController.lossReceipt | lossReceipt(Receipt receipt) | 非空 | 库存信息进行更新，保持一致 |
| InventoryController.storageReceipt | storageReceipt(Receipt receipt) | 非空 | 完成库存中信息的更改，并向库存管理人员发送信息 |
| InventoryController.outboundReceipt | outboundReceipt(Receipt receipt) | 非空 | 完成库存中信息的更改，并向库存管理人员发送信息 |
| InventoryController.showCategory | showCategory() | 无 | 显示库存中商品的分类，以树状 |

**表7 库存模块的需要的服务接口**
需要的服务

| 服务名 | 服务 |
| --- | --- |
| InventoryDataService.find(int id) | 根据编号进行查找单一持久化对象 |
| InventoryDataService.insert(CommodityPO po) | 插入单一持久化对象 |
| InventoryDataService.delete(CommodityPO po) | 删除单一持久化对象 |
| InventoryDataService.update(CommodityPO po) | 更新单一持久化对象 |
| DatabaseFactory.getDatabase | 得到数据库的服务的引用 |
| InventoryDataService.insertCategory(CategoryPO po) | 插入单一持久化对象 |
| InventoryDataService.deleteCategory(CategoryPO po) | 删除单一持久化对象 |
| InventoryDataService.updateCategory(CategoryPO po) | 更新单一持久化对象 |



### 5.3销售模块的分解
**图12 销售模块类**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/25521821/1651594795624-2eb9fe1b-b571-4383-8287-048979a9a29d.jpeg)
#### 5.3.1销售模块的职责
**表8 销售模块的职责**

| 模块 | 职责 |
| --- | --- |
| SaleController | 负责销售人员登陆相关 |
| SaleRutrunController | 负责折让相关 |
| GiftSheetController | 负责客户管理相关 |

#### 销售模块接口规范
##### Sale模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358163804-bb57e09e-d7a4-441e-8b85-abc30ba263ab.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358201489-5b46adce-80b4-4aa1-bcd8-ba9aa5dd9a9f.png)

##### SaleRuturn模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358236654-5eb8c0b4-d0a2-4d17-a53d-da142dbd8f64.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358259003-37345591-d8b6-434a-9091-c79009c1f476.png)
##### GiftSheet模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358284992-62076d86-2ad6-4c02-869b-9a988a0284e0.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358296640-1f342bde-51f9-48ed-b41c-a6ff4e76211a.png)
### 5.4员工管理模块
#### 5.4.1员工模块的职责

| 模块 | 职责 |
| --- | --- |
| EmployeeController | 负责员工的注册和增删改查相关 |
| UserController | 负责系统登陆，同时员工注册时会自动创建新账号 |

#### 5.4.2员工模块接口规范
##### Employee模块
供接口

| 接口 | 语法 | 前置条件 | 后置条件 |
| --- | --- | --- | --- |
| EmployeeController.makeEmployeeOrder | public Response makeEmployeeOrder(@RequestBody EmployeeVO employeeVO) | 输入合法 | 将员工信息发送给EmployeeService |
| EmployeeController.showAll | public Response showAll() | 输入合法 |  |
| EmployeeController.createEmployee | public Response createEmployee(@RequestBody EmployeeVO employeeVO) | 输入合法 | 将员工id发送给EmployeeService |
| EmployeeController.deleteById | public Response deleteById(@RequestParam(value = "id") Integer employeeId) | 输入合法 | 将员工信息发送给EmployeeService |
| EmployeeController.updateEmployee | public Response updateEmployee(@RequestBody EmployeeVO employeeVO) | 输入合法 | 将员工信息发送给EmployeeService |

需接口

| 服务名 | 服务 |
| --- | --- |
| EmployeeVO create(EmployeeVO employeeVO); | 新增员工信息 |
| List<EmployeeVO> getAll(); | 获取员工列表 |
| EmployeeVO getOneById(Integer employeeId); | 获取指定员工 |
| void deleteById(Integer employeeId); | 删除指定员工 |
| EmployeeVO update(EmployeeVO employeeVO); | 更新员工 |
| void register(UserVO userVO); | 为员工自动注册系统账户 |

### 5.5折让管理模块
#### 5.5.1折让管理模块职责
| 模块 | 职责 |
| --- | --- |
| VoucherController | 负责代金券，商品组合包、商品赠送相关 |
| DiscountController | 负责折扣相关 |

#### 5.5.2折让模块接口规范
##### discount模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358910372-dd89d9a7-82de-42dc-bfe0-99fc5444620a.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657359754589-192be8ce-f9ed-4644-ac0d-220b5a9ba2bf.png)
##### Voucher模块
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358922951-2a8f0a16-487b-429f-a09a-8baaf7fb6dab.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657358943103-bdf4950c-2970-44d2-b9cf-b9b22f0e5c51.png)
### 5.6工资税收管理模块
#### 5.6.1工资税收管理模块职责
| 模块 | 职责 |
| --- | --- |
| SalarySystemController | 负责薪酬制度相关相关 |
| SalaryController | 负责工资相关 |
| AnnualBonusController | 负责年终奖相关 |

#### 5.6.2工资税收管理模块接口
##### SalarySheet模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657360266365-d76d01d2-9af2-4a0d-a3ad-5dc03f90fae6.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657360344689-b2127b6e-55c0-47d8-80e1-e148f89fde1c.png)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657360360890-ed09d8d1-aa10-48d2-86c3-370e53f87d07.png)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657360371126-25a66508-d8f0-41ea-91c0-7ef40a775670.png)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657360381982-4c1dfab7-dbf5-445b-8db3-d9a44bea673a.png)

##### SalarySystem模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657360296337-ef26b890-f722-4a6b-b6f5-52058833bd58.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657360328442-e1dd6849-ebfb-4c96-9ddf-cbd106a454ad.png)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657360360890-ed09d8d1-aa10-48d2-86c3-370e53f87d07.png)
### 5.7账户转账模块
#### 5.7.1账户转账模块职责
| 模块 | 职责 |
| --- | --- |
| AccountController | 负责公司账户相关 |
| PayableController | 负责付款单相关 |
| ReceiveController | 负责收款单相关 |
| TransferController | 负责转账提醒 |

#### 5.7.2账户转账模块的接口
##### account模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362063034-33648840-7901-4747-b31f-84a4f68b6c65.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362079691-26310ab5-8747-4f1d-93ff-fb04a82c02a1.png)

##### payable模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362136781-e71077b7-1de7-4c33-b537-6b10d0261bc7.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362119156-02e48d25-fb2c-4b93-aac7-7dd65f19b648.png)
##### receive模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362159474-a87c3979-1b89-4980-9c81-d10028c59002.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362176743-68174a85-cc9d-49e2-8c8d-8972e0964651.png)
##### transfer模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362200518-3e36f61e-c40d-45fc-aae7-26977cf660c7.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362213781-d0a5a6dd-ef94-4840-abc0-dcbe166705d4.png)
### 5.8经营数据查看模块
#### 5.8.1经营情况查看模块职责
| 模块 | 职责 |
| --- | --- |
| BusinessController | 负责经营情况查看相关 |
| SheetExportController | 负责数据导出导入相关 |

#### 5.8.2经营情况查看模块的接口
##### Business模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362436206-4cc9f7cc-5453-4e79-ad86-ba9a1766a6bc.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362418501-7965eb82-003e-4bdd-ab4b-bc8bda6df936.png)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362535233-e801c456-dc04-44ad-b70c-b927225ca3a0.png)

##### SheetExport模块
供接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362508691-7c1e9bc1-9ea2-4a7a-8532-40c0074c79a6.png)
需接口
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657362526304-5394b42c-f2fe-48d4-aa90-67f7c39943f0.png)
### 5.9系统管理模块
#### 5.9.1系统管理模块职责
管理用户登陆和系统备份（初期建账功能）

| 模块 | 职责 |
| --- | --- |
| LoginController | 负责销售人员登陆相关 |
| BackupController | 负责初期建账相关 |

##### Login模块
供接口

| 接口 | 语法 | 前置条件 | 后置条件 |
| --- | --- | --- | --- |
| LoginController.getUserInput | getUserInput(String username, String pwd) | 输入合法 | 从LoginService返回登陆状态信息 |
| LoginController.showLoginState | showLoginState(LoginState state) | 无 | 在页面显示登陆状态（成功、失败） |

需接口

| 服务名 | 服务 |
| --- | --- |
| LoginService.checkUserGroup | 检查用户输入信息，若存在，检查用户组，返回登陆状态 |

##### backup模块
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657357759598-bc26e6f0-d2a5-4b22-851c-660e8ac26bc9.png)
![image.png](https://cdn.nlark.com/yuque/0/2022/png/22123684/1657357776955-d936c975-f26f-4a9d-a010-f97e6da34cb9.png)

### 5.10数据层的分解
数据层主要给业务逻辑层提供数据访问服务，包括对于持久化数据的增删改查。由于持久化数据的保存可能存在多种形式：Txt文件、序列化文件、数据库等，所示抽象了数据服务。数据层模块的描述具体如图13所示。
**图13 数据层模块的描述**
![](https://cdn.nlark.com/yuque/0/2022/jpeg/22123684/1651643068953-3f1ae70f-9306-41aa-9c5d-e27825c2bca6.jpeg)
#### 5.4.1数据层模块的职责
数据层模块的职责如表11所示。
模块
**表12 数据层模块**

| 模块 | 职责 |
| --- | --- |
| UserDao | 用户相关 |
|  CommodityDao | 商品相关 |
| InventoryDao | 库存相关 |
| FinanceDao | 金额相关 |
| HRDao | 员工管理相关 |
| ReciptDao | 单据管理相关 |

#### 5.4.2数据层模块的接口规范
数据层模块的接口规范如表12所示。
##### UserDao
| 接口 | 语法 | 前置条件 | 后置条件 |
| --- | --- | --- | --- |
| UserDao.createClient | int createUser(UserPO user) | user非空 | 返回是否创建成功 |
| UserDao.findById | UserPO findById(int Id) | ID非空 | 返回查找到的UserPO对象，若为null则代表无 |
| UserDao.findAll | List<UserPO> findAll() | 无 | 以列表的形式返回所有user |
| UserDao.deleteById | int deleteById(int Id) | ID非空 | 返回是否删除成功 |

#####  CommodityDao
| 接口 | 语法 | 前置条件 | 后置条件 |
| --- | --- | --- | --- |
| CommodityDao.createCommodity | int createCommodity(CommodityPO commodity) | commodity非空 | 返回是否创建成功 |
| CommodityDao.updateById | int updateByIdCommodityPO commodity) | commodity非空 | 返回是否修改成功 |
| CommodityDao.findById | CommodityPO findById(int id) | id非空 | 返回查找到的commodity对象，若为null则代表无 |
| CommodityDao.findAll | List<CommodityPO> findAll() | 无 | 以列表的形式返回所有commodity |
| CommodityDao.deleteById | int deleteById(int Id) | ID非空 | 返回是否删除成功 |

##### InventoryDao
| 接口 | 语法 | 前置条件 | 后置条件 |
| --- | --- | --- | --- |
| InventoryDao.addCommodity | int addCommodity(int id, int num) | id非空，num不为负 | 返回是否添加成功 |
| InventoryDao.subCommodity | int subCommodity(int id,int num) | id非空，num不为负 | 返回是否减少成功 |
| InventoryDao.findAll | Map<int id,int num> findAll() | 无 | 返回货物编号和其库存数量 |

**表13 数据层模块的接口规范**

| 提供的服务（供接口） |  |  |
| --- | --- | --- |
| userDao.createUser | 语法     | int createUser(UserDao user); |
| ------------------ | -------- | ----------------------------- |
|                    | 前置条件 | 业务逻辑层调用传入UserDao对象 |
|                    | 后置条件 | 返回创建用户是否成功          |

## 6.信息视角
### 6.1数据持久化对象
持久化用户对象UserPO的定义如图14所示。
**图14 持久化用户对象的定义**
```java
public class UserPO implement Serializable{
    int id;
    String name;
    String password;
    Userrole role;
    
    public UserPO(int i,String n,String p,UserRole r){
        id = i;
        name = n;
        password = p;
        role = r;
    }
    
    public String getName(){
        return name;
    }
    
    public int getID(){
        return id;
    }
    public String getPassword(){
        return password;
    }
    public UserRole getRole(){
        return role;
    }
}
```
### 6.2Txt持久化格式
Txt数据保持格式以Commodity.txt为例。每行分别对应货号、商品名称、价格、数量。中间用“：”隔开。如下所示： 
123：杯子：10：32
456：桌子：20：22
### 6.3数据库表
数据库中包含 User表、Inventory表、Sales表、Finance表、Employees表、Commodity表、CommodityChange表、Receipt表、SalesRecipt表、Client表、Category表。
其中 Commodity 表的主键 Commodity  ID为Inventory表的外键。
