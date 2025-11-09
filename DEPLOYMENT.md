# Deployment Guide for Render

This guide will help you deploy your Java Shopping Cart application on Render.

## Prerequisites

1. A GitHub account
2. A Render account (sign up at https://render.com)
3. Your project pushed to a GitHub repository

## Deployment Steps

### 1. Push Your Code to GitHub

If you haven't already, push your project to GitHub:

```bash
git init
git add .
git commit -m "Initial commit - Ready for Render deployment"
git branch -M main
git remote add origin <your-github-repo-url>
git push -u origin main
```

### 2. Deploy on Render

#### Option A: Using render.yaml (Recommended)

1. Go to https://dashboard.render.com
2. Click "New +" and select "Blueprint"
3. Connect your GitHub repository
4. Render will automatically detect the `render.yaml` file
5. Click "Apply" to deploy

#### Option B: Manual Setup

1. Go to https://dashboard.render.com
2. Click "New +" and select "Web Service"
3. Connect your GitHub repository
4. Configure the service:
   - **Name**: online-shopping-cart (or your preferred name)
   - **Environment**: Java
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -cp "target/classes:target/dependency/*" com.shoppingcart.Application`
   - **Plan**: Free (or choose a paid plan)
5. Click "Create Web Service"

### 3. Environment Variables

Render automatically sets the `PORT` environment variable, which your application now reads. No additional configuration needed!

### 4. Wait for Deployment

Render will:
1. Install Java 11
2. Install Maven
3. Run the build command
4. Start your application

The deployment typically takes 5-10 minutes for the first build.

### 5. Access Your Application

Once deployed, Render will provide you with a URL like:
`https://online-shopping-cart.onrender.com`

## Important Notes

- **Free Plan Limitations**: 
  - Services on the free plan spin down after 15 minutes of inactivity
  - First request after spin-down may take 30-60 seconds
  - Consider upgrading to a paid plan for production use

- **File Persistence**: 
  - The `orders.xml` file is stored in the application directory
  - On Render's free plan, files may be reset on redeploy
  - Consider using a database (PostgreSQL) for production

- **Port Configuration**: 
  - The application automatically reads the `PORT` environment variable
  - No manual configuration needed

## Troubleshooting

### Build Fails
- Check the build logs in Render dashboard
- Ensure all dependencies are correctly specified in `pom.xml`
- Verify Java version is set to 11

### Application Won't Start
- Check the logs for error messages
- Verify the start command is correct
- Ensure the main class path is correct: `com.shoppingcart.Application`

### Port Issues
- The application now automatically uses the PORT environment variable
- If you see port binding errors, check the Application.java file

## Next Steps

1. **Add a Database**: Consider migrating from XML to PostgreSQL for better persistence
2. **Configure Email**: Update EmailService.java with your SMTP credentials
3. **Add Environment Variables**: Store sensitive data (like email passwords) as Render environment variables
4. **Set up Custom Domain**: Configure a custom domain in Render dashboard

## Support

For Render-specific issues, check:
- Render Documentation: https://render.com/docs
- Render Community: https://community.render.com

