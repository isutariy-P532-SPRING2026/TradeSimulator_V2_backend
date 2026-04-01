# TradeSimulator v2.0

A paper-trading platform built with Spring Boot that allows analysts to place buy/sell orders, monitor portfolios in real time, and receive multi-channel notifications when orders execute. Built for CSCI-P532 — Object-Oriented Software Design, Spring 2026.

---

## Live Demo

| Item | URL |
|---|---|
| **Frontend (GitHub Pages)** | https://isutariy-p532-spring2026.github.io/TradeSimulator_V2_frontend/ |
| **Backend API (Render)** | https://tradesimulator-v2-backend.onrender.com |
| **Frontend Repository** | https://github.com/isutariy-P532-SPRING2026/TradeSimulator_V2_frontend |
| **Backend Repository** | https://github.com/isutariy-P532-SPRING2026/TradeSimulator_V2_backend |

> **Note:** Render free tier spins down after 15 minutes of inactivity. The first request may take up to 60 seconds to cold-start. This is expected behaviour.

---

## Running Locally (Docker — no Java install required)

```bash
# Build the image
docker build -t tradesimulator .

# Run the container
docker run -p 8080:8080 tradesimulator
```

Then open `http://localhost:8080` in your browser.

---

## Running Locally (Maven)

```bash
./mvnw spring-boot:run
```

Then open `http://localhost:8080`.

---

## Running Tests

```bash
./mvnw test
```

Test reports are written to `target/surefire-reports/`.

---

## Technology Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.x |
| Frontend | HTML + CSS + JavaScript (plain) |
| Build | Maven |
| Container | Docker (multi-stage build) |
| Deployment | Render.com (Docker Web Service) |
| CI/CD | GitHub Actions |
| Testing | JUnit 5 + Mockito |

---

## Features

### Week 1
- Live market feed — 5 stocks (AAPL, GOOG, TSLA, AMZN, MSFT) with prices updated every 5 seconds using a random-walk algorithm (±2%)
- Market orders — execute immediately at current price
- Limit orders — execute automatically when the price threshold is met
- Portfolio tracking — cash balance, holdings, and total portfolio value
- Trade history — full log of executed trades with timestamp, ticker, side, quantity, price, and total
- Console notifications on every order execution

### Week 2
- **Multiple pricing algorithms** — switch between Random Walk, Mean Reversion, and Trend Following at runtime from the UI without restarting the server
- **Multi-channel notifications** — configurable per user: Console (always on), Email (mock), SMS (mock), Dashboard badge counter
- **Multiple users** — three independent analyst accounts (Alice, Bob, Carol), each with their own portfolio, cash balance, order book, trade history, and notification preferences. Switch users via a dropdown in the UI

---

## Design Patterns

| Pattern | Location | Purpose |
|---|---|---|
| **Strategy** | `PriceUpdateStrategy` interface → `RandomWalkStrategy`, `MeanReversionStrategy`, `TrendFollowingStrategy` | Swap pricing algorithms at runtime without modifying `MarketFeed` |
| **Observer** | `PriceObserver` interface → `OrderService` registered with `MarketFeed` | Decouple the price engine from components that react to price changes |
| **Decorator** | `NotificationDecorator` → `EmailNotificationDecorator`, `SmsNotificationDecorator`, `BadgeDashboardDecorator` | Stack notification channels without modifying existing notifiers |
| **Factory** | `OrderFactory.createOrder()` | Single creation point for all order types with validated input |
| **Singleton** | `MarketFeed` (`@Service`) | Exactly one shared market feed instance across the application |

---

## Project Structure

```
src/
├── main/java/com/tradesim/tradesimulator/
│   ├── config/          CorsConfig.java
│   ├── controller/      TradingController.java, StrategyController.java
│   ├── factory/         OrderFactory.java
│   ├── model/           Order, Portfolio, Stock, User, enums
│   └── service/         MarketFeed, OrderService, UserRegistry,
│                        PriceUpdateStrategy implementations,
│                        NotificationService + decorator chain,
│                        DashboardAlertStore, MutableNotificationService
└── test/java/           OrderFactoryTest, OrderServiceTest,
                         RandomWalkStrategyTest, MeanReversionStrategyTest,
                         TrendFollowingStrategyTest,
                         NotificationDecoratorChainTest
```

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/stocks` | Current prices for all stocks |
| GET | `/api/portfolio?userId=alice` | Cash balance, holdings, total value for user |
| GET | `/api/orders?userId=alice` | Pending and executed orders for user |
| POST | `/api/orders?userId=alice` | Place a new market or limit order |
| GET | `/api/strategies` | List available pricing strategies |
| POST | `/api/strategy` | Switch active pricing strategy |
| GET | `/api/notifications?userId=alice` | Get notification channel config for user |
| POST | `/api/notifications?userId=alice` | Update notification channels for user |
| GET | `/api/alerts?userId=alice` | Get unread dashboard alert count |
| GET | `/api/users` | List all users |

---

## CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/ci.yml`) runs on every push to `main` and every pull request:

1. **test** — runs `mvn test` and uploads Surefire report as artifact
2. **build** — runs `mvn package` and `docker build` to verify the Dockerfile (depends on test)
3. **deploy** — calls the Render Deploy Hook via `curl` to trigger a new deployment (runs only on pushes to `main`, depends on build)

The Render Deploy Hook URL is stored as the GitHub Actions secret `RENDER_DEPLOY_HOOK`.

---

## Week 2 — Files Modified 

| Change | Pre-existing files modified | Count |
|---|---|---|
| Change 1 — Multiple Pricing Algorithms | `MarketFeed.java` | **1** |
| Change 2 — Multi-channel Notifications | `NotificationConfig.java` (emptied) | **1** |
| Change 3 — Multiple Users | `TradingController.java`, `OrderService.java` | **2** |
| CI/CD | No changes required | **0** |
| **Total** | | **4** |

All new classes (strategies, decorators, `UserRegistry`, `User`, `DashboardAlertStore`, etc.) were added as new files and do not count against the score.
