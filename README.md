# kbot

## Link to telegram bot 

    https://t.me/inezhinskiy_bot

## Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/kbot.git
cd kbot
```

2. Set up your Telegram Bot Token:
```bash
export TELE_TOKEN="your_telegram_bot_token"
```

3. Build the application:
```bash
make build
```

4. Run the application:
```bash
./kbot start
```

5. Push image 
```bash
make push
```

## Usage 

    /start hello


## CI/CD Pipeline

```mermaid
flowchart TD
    A[Developer] -->|git push to develop| B[GitHub Repository]
    B --> C[GitHub Actions: CI job]
    C -->|go build, go test| D[Docker build]
    D -->|docker push| E[ghcr.io/inezhinskiy/kbot]
    C -->|on success| F[GitHub Actions: CD job]
    F -->|yq: update image.tag| G[helm/values.yaml]
    G -->|git commit and push| B
    B -.->|watches develop branch| H[ArgoCD]
    H -->|auto-sync| I[Kubernetes Cluster]
    E -.->|image pull| I
    I --> J[kbot Pod running]
```