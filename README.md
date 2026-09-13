# SS07 HW05: Gateway Filters và Loan Service gọi đa service bằng FeignClient

## Mục tiêu

Bài này xây dựng nghiệp vụ đăng ký khoản vay cho FinBank. `loan-service` gọi đồng bộ sang `customer-service` và `account-service` bằng OpenFeign, sau đó tạo hồ sơ vay trạng thái `PENDING`. `api-gateway` có Global Filter để log request/response, đo thời gian xử lý và thêm header `X-Response-Time`.

## Module

```text
discovery-server    : Eureka Server, port 8761
api-gateway         : Gateway, port 8222
account-service     : Quản lý tài khoản, port 8081
transaction-service : Xử lý chuyển tiền, port 8082
customer-service    : Quản lý khách hàng, port 8083
loan-service        : Đăng ký khoản vay, port 8084
```

## API Account Service

```text
GET /api/accounts/{accountNumber}
GET /api/accounts/{accountNumber}/balance
GET /api/accounts/customer/{customerId}
PUT /api/accounts/{accountNumber}/debit
PUT /api/accounts/{accountNumber}/credit
```

Ví dụ body cho debit/credit:

```json
{
  "amount": 2000000
}
```

## API Customer Service

```text
GET /api/customers/{id}
GET /api/customers/by-account/{accountNumber}
```

## API Loan Service

```text
POST /api/loans/apply
```

Gọi qua Gateway:

```text
POST http://localhost:8222/api/loans/apply
```

Body:

```json
{
  "customerId": 1,
  "amount": 50000000,
  "termMonths": 24,
  "purpose": "Vay mua xe may"
}
```

## Dữ liệu mẫu

Khi `account-service` và `customer-service` khởi động, hệ thống tự tạo:

```text
Customer 1 - Nguyen Van A - co account 1001, 1003 active
Customer 2 - Tran Thi B - co account 1002 active
Customer 3 - Le Khong Co TK - khong co account, dung de test no active account
```

## Thứ tự chạy

```bash
./gradlew :discovery-server:bootRun
./gradlew :account-service:bootRun
./gradlew :customer-service:bootRun
./gradlew :transaction-service:bootRun
./gradlew :loan-service:bootRun
./gradlew :api-gateway:bootRun
```

## Test case Postman

Collection nằm trong thư mục:

```text
postman/FinBank_SS07_HW05.postman_collection.json
```

Các case chính:

- `POST /api/loans/apply` với `customerId = 1`: tạo loan `PENDING`
- `POST /api/loans/apply` với `customerId = 99`: lỗi khách hàng không tồn tại
- `POST /api/loans/apply` với `customerId = 3`: lỗi `no active account`
- Kiểm tra tab Headers trong Postman có `X-Response-Time`
- Kiểm tra console Gateway có log method, path và thời gian xử lý

## FeignClient trong Loan Service

`LoanServiceApplication` bật Feign:

```java
@EnableFeignClients
```

Client gọi Customer Service:

```java
@FeignClient(name = "customer-service")
public interface CustomerServiceClient {
    @GetMapping("/api/customers/{id}")
    CustomerResponse getById(@PathVariable Long id);
}
```

Client gọi Account Service:

```java
@FeignClient(name = "account-service")
public interface AccountServiceClient {
    @GetMapping("/api/accounts/customer/{customerId}")
    List<AccountResponse> getAccountsByCustomerId(@PathVariable Long customerId);
}
```

## Gateway Global Filter

`LoggingFilter` implements `GlobalFilter, Ordered`. Filter ghi log lúc request vào Gateway, dùng `beforeCommit()` để thêm header `X-Response-Time`, sau đó log thời gian xử lý khi response hoàn tất.

Luồng tổng thể:

```text
Client -> API Gateway -> LoggingFilter -> loan-service
loan-service -> CustomerServiceClient -> customer-service
loan-service -> AccountServiceClient -> account-service
```
