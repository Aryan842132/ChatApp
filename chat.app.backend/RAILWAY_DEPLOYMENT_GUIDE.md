# Deploying Chat Application to Railway

This guide explains how to deploy your Spring Boot chat application to Railway.

## Prerequisites

- Railway account (sign up at https://railway.app)
- MongoDB Atlas account or other MongoDB hosting service
- Your application code pushed to a GitHub repository

## Step-by-Step Deployment Guide

### 1. Prepare Your MongoDB Connection

1. Sign up for MongoDB Atlas or use your existing MongoDB service
2. Create a database and get your connection string
3. Ensure your database allows connections from external sources (IP Whitelist)

### 2. Create a New Project on Railway

1. Go to https://railway.app and log in
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Choose your repository containing the chat application

### 3. Configure Environment Variables

Add the following environment variables in the Railway dashboard under "Variables":

- `SPRING_MONGODB_URI`: Your MongoDB connection string
- `JWT_SECRET`: A strong secret key for JWT signing (at least 256 bits)
- `PORT`: Leave as default (Railway sets this automatically)

### 4. Update Your MongoDB Connection String

Replace the placeholder in `railway.json` with your actual MongoDB Atlas connection string:

```json
{
  "env": {
    "SPRING_MONGODB_URI": {
      "value": "mongodb+srv://<username>:<password>@<cluster>.mongodb.net/<database>",
      "required": true
    }
  }
}
```

### 5. Deploy the Application

1. On Railway, go to your project
2. Click "Deploy Now" or wait for auto-deployment
3. Monitor the deployment logs to ensure everything builds correctly

### 6. Configure Your Frontend

Once deployed, your application will be available at a Railway-generated URL. Update your frontend to use this URL for API calls and WebSocket connections.

## Dockerfile Explanation

Our Dockerfile uses a multi-stage build approach:

1. **Builder Stage**: Uses Maven to build the application
2. **Runtime Stage**: Uses a lightweight Java runtime image

The application will listen on the port specified by the `PORT` environment variable, which Railway provides automatically.

## Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_MONGODB_URI` | MongoDB connection string | `mongodb+srv://user:pass@cluster.mongodb.net/db` |
| `JWT_SECRET` | Secret key for JWT signing | `a-very-long-and-secure-secret-key` |
| `PORT` | Port number (provided by Railway) | `8080` |

## Troubleshooting

### Common Issues:

1. **Database Connection Failures**:
   - Verify your MongoDB connection string
   - Check IP whitelist settings in MongoDB Atlas
   - Ensure your database is accessible publicly

2. **Deployment Failures**:
   - Check the build logs in Railway dashboard
   - Verify all dependencies in `pom.xml`
   - Ensure the application starts properly

3. **Environment Variables Missing**:
   - Double-check variable names match exactly
   - Ensure required variables are marked as required in Railway

### Health Checks

The application doesn't include a health check endpoint by default. You can add Spring Boot Actuator for more robust health monitoring.

## Scaling

Railway makes it easy to scale your application:
- Increase instance count in the Railway dashboard
- Monitor resource usage through the dashboard
- Set up autoscaling based on demand

## Monitoring

- View logs directly in the Railway dashboard
- Monitor response times and errors
- Set up alerts for critical issues

## Security Notes

- Store sensitive information (like JWT secrets) as environment variables
- Use strong passwords for database connections
- Regularly rotate secrets
- Consider using Railway's built-in secrets management

## Updating Your Application

1. Make changes to your code locally
2. Commit and push to your GitHub repository
3. Railway will automatically rebuild and deploy the new version
4. Monitor the deployment in the dashboard

Your chat application is now ready for Railway deployment!