# KanBan Todo

以 **Spring Boot + MySQL** 實作的看板式待辦事項清單，後端提供 RESTful API，前端用原生 HTML / JavaScript 呼叫 API 呈現三欄看板。

## 功能

- 新增任務（標題、說明、優先度）
- 任務分為 **待辦 / 進行中 / 已完成** 三欄，一鍵切換狀態（完成後可重開）
- 依優先度（高 → 中 → 低）排序顯示
- 刪除任務（含確認提示）
- 輸入驗證：標題不可為空，錯誤以 JSON 回傳欄位與訊息
- 查無任務時回傳 `404` 與錯誤訊息

## 技術

| 層 | 使用技術 |
| --- | --- |
| 語言 | Java 17 |
| 框架 | Spring Boot 3.3（Web、Data JPA、Validation） |
| 資料庫 | MySQL（Hibernate 自動建表） |
| 前端 | HTML / CSS / 原生 JavaScript（Fetch API） |
| 建置 | Maven |

## 架構

採用常見的分層架構：

```
Controller  →  Service  →  Repository  →  MySQL
    ↑
   DTO (TaskRequest，含驗證)
```

- **Controller**：`TaskController` 對外提供 REST API；`GlobalExceptionHandler` 以 `@ControllerAdvice` 統一處理驗證錯誤與查無資料
- **Service**：`TaskService` 處理商業邏輯（預設狀態 / 優先度、部分欄位更新），寫入操作加上 `@Transactional`
- **Repository**：`TaskRepository` 繼承 `JpaRepository`
- **Entity**：`Task`，狀態與優先度以 enum 儲存（`TaskStatus`、`TaskPriority`）
- **DTO**：`TaskRequest` 使用 Java `record` 搭配 `@NotBlank` 驗證

## API

Base path：`/api/tasks`

| Method | Path | 說明 | 成功回應 |
| --- | --- | --- | --- |
| `GET` | `/api/tasks` | 取得全部任務 | `200` |
| `GET` | `/api/tasks/{id}` | 取得單一任務 | `200` / `404` |
| `POST` | `/api/tasks` | 新增任務 | `201` |
| `PUT` | `/api/tasks/{id}` | 更新任務 | `200` / `404` |
| `DELETE` | `/api/tasks/{id}` | 刪除任務 | `204` / `404` |

Request body 範例：

```json
{
  "title": "寫 README",
  "description": "整理作品集",
  "status": "TODO",
  "priority": "HIGH"
}
```

- `status`：`TODO` / `IN_PROGRESS` / `DONE`，未填預設 `TODO`
- `priority`：`LOW` / `MEDIUM` / `HIGH`，未填預設 `MEDIUM`

驗證失敗（`400`）：

```json
{ "title": "title 不可為空" }
```

## 執行方式

### 需求

- JDK 17 以上
- Maven
- MySQL

### 步驟

1. 建立資料庫（資料表會由 Hibernate 自動建立）：

   ```sql
   CREATE DATABASE kanban_todo;
   ```

2. 複製設定檔範本並填入自己的 MySQL 帳號密碼：

   ```bash
   cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
   ```

   `application-local.properties` 已列入 `.gitignore`，不會被提交。

3. 啟動：

   ```bash
   mvn spring-boot:run
   ```

4. 開啟瀏覽器前往 <http://localhost:8080>

## 專案結構

```
src/main/
├── java/com/example/kanban/
│   ├── KanbanTodoApplication.java
│   ├── controller/   # REST API、全域例外處理
│   ├── service/      # 商業邏輯
│   ├── repository/   # JPA Repository
│   ├── entity/       # Task、TaskStatus、TaskPriority
│   ├── dto/          # TaskRequest
│   └── exception/    # TaskNotFoundException
└── resources/
    ├── application.properties
    ├── application-local.properties.example
    └── static/index.html   # 看板前端
```
