#!/bin/bash
# deploy.sh — Pull latest image from GHCR and deploy locally
# Usage: ./deploy.sh <github-username>

set -e

GITHUB_USER=${1:?"Usage: ./deploy.sh <github-username>"}
IMAGE="ghcr.io/${GITHUB_USER}/task-manager:latest"

echo "========================================="
echo " Task Manager — Local Deployment"
echo "========================================="

echo "[1/4] Logging in to GitHub Container Registry..."
echo "     (You will be prompted for a Personal Access Token)"
docker login ghcr.io -u "$GITHUB_USER"

echo "[2/4] Pulling latest image: $IMAGE"
docker pull "$IMAGE"

echo "[3/4] Stopping any existing container..."
docker compose down 2>/dev/null || true

echo "[4/4] Starting application..."
export IMAGE_TAG="$IMAGE"
docker compose up -d

echo ""
echo "Waiting for application to start..."
sleep 10

# Health check
if curl -sf http://localhost:8080/actuator/health > /dev/null; then
    echo "Application is running at http://localhost:8080"
    echo "API docs:  http://localhost:8080/api/tasks"
    echo "Health:    http://localhost:8080/actuator/health"
    echo "H2 Console: http://localhost:8080/h2-console"
else
    echo "WARNING: Health check failed. Check logs with: docker compose logs"
fi
