# Architecture Workflow

MotionVolt CarCare now follows a hexagonal architecture.

1. Web request enters an inbound adapter in `adapter.in.web`.
2. The controller calls an application input port in `application.port.in`.
3. The application service coordinates business flow in `application.service`.
4. The service depends only on output ports in `application.port.out`.
5. Persistence and external OAuth implementations live in outbound adapters.
6. Domain data is carried by model classes in `domain.model`.

This keeps Spring MVC, JDBC, and Kakao OAuth outside the core service logic.
