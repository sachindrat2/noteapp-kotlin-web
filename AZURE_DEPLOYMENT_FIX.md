# Azure Web App Deployment Guide

## Current Issue
Azure is serving the nginx welcome page because it's using an old Docker image without the proper index.html file.

## Solution: Update Docker Image Version

### Step 1: In Azure Portal
1. Go to https://portal.azure.com
2. Navigate to your Web App: `kotlin-web-dev-cke4b2fnb2gvgfbc`
3. Go to **Deployment Center**
4. Update the **Full Image Name and Tag** to: `sachindra785/kotlin-web:v1.1`
5. Click **Save**

### Step 2: Restart the App
1. In your Web App overview, click **Restart**
2. Wait 2-3 minutes for the new image to be pulled and started

### Step 3: Verify
Visit: https://kotlin-web-dev-cke4b2fnb2gvgfbc.canadacentral-01.azurewebsites.net/

## Alternative: Force Redeploy via CLI
```bash
az webapp restart --name kotlin-web-dev-cke4b2fnb2gvgfbc --resource-group [your-resource-group]
```

## What's Fixed
- ✅ Added index.html to Docker image
- ✅ Proper Kotlin/JS app loading
- ✅ Modern Material 3 login/register screens
- ✅ Nginx with SPA routing support

The new image `sachindra785/kotlin-web:v1.1` contains your complete Kotlin Compose web app!