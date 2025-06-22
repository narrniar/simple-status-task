# 🚀 Quick Start Guide

Get the Task Management API running in 5 minutes!

## Prerequisites
- Docker and Docker Compose installed
- Git installed

## Step 1: Clone and Navigate
```bash
git clone <repository-url>
cd simple-status-task
```

## Step 2: Start the Application
```bash
docker-compose up --build
```

## Step 3: Test the API
Open your browser or use curl:

### Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "My First Task",
    "description": "This is a test task",
    "status": "PENDING"
  }'
```

### Get All Tasks
```bash
curl http://localhost:8080/api/tasks/1
```

### View API Documentation
Open: http://localhost:8080/api/swagger-ui.html

## 🎉 You're Done!

The application is now running with:
- ✅ API at http://localhost:8080/api
- ✅ PostgreSQL database
- ✅ Swagger documentation
- ✅ Health check at http://localhost:8080/api/actuator/health

## Need Help?
- Check the full [README.md](README.md) for detailed documentation
- View logs: `docker-compose logs -f app`
- Stop the application: `docker-compose down` 