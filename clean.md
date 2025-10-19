~~~
myapp/
├── pom.xml
│
├── myapp-core/
│    └── pom.xml
│
├── my-app-place-order/
│    ├── pom.xml
│    ├── platform/
│    │    └── order/
│    │         └── place/
│    │              ├── repository/
│    │              │    └── AbstractOrderRepositoryAdapter.java
│    │              ├── client/
│    │              │    └── AbstractPaymentClientAdapter.java
│    │              └── messaging/
│    │                   └── AbstractEventPublisherAdapter.java
│    └── usecase/
│         └── order/
│              └── place/
│                   ├── Order.java
│                   ├── ID.java
│                   ├── PlaceOrderUseCase.java
│                   ├── mapper/
│                   │    └── PlaceOrderMapper.java
│                   ├── repository/
│                   │    └── OrderRepository.java
│                   ├── client/
│                   │    └── PaymentClient.java
│                   └── messaging/
│                        └── EventPublisher.java
│
├── myapp-app-web/
│    ├── pom.xml
│    ├── config/
│    │    └── BeanConfig.java
│    └── platform/
│         ├── order/
│         │    ├── place/
│         │    │    ├── PlaceOrderController.java
│         │    │    ├── PlaceOrderService.java
│         │    │    ├── repository/
│         │    │    │    └── OrderRepositoryAdapter.java
│         │    │    ├── client/
│         │    │    │    └── PaymentClientAdapter.java
│         │    │    └── messaging/
│         │    │         └── EventPublisherAdapter.java
│         │    ├── cancel
│         │    └── search
│         └── customer
│
├── myapp-app-console/
│    ├── pom.xml
│    └── platform/
│         ├── order/
│         │    ├── place/
│         │    │    ├── repository/
│         │    │    │    └── OrderRepositoryAdapter.java
│         │    │    ├── client/
│         │    │    │    └── PaymentClientAdapter.java
│         │    │    └── messaging/
│         │    │         └── EventPublisherAdapter.java
│         │    ├── cancel
│         │    └── search
│         └── customer
│
└── myapp-architecture-tests/
     ├── pom.xml
     └── ArchitectureTest.java
~~~