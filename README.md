# Git Commands
## Pulling changes from main branch on GitHub
1. Fetch the latest changes
   ```bash
   git fetch origin main
   ```
2. Switch to the Local Main Branch
   ```bash
   git checkout main
   ```
3. Pull the Latest Changes into the Local Main Branch
   ```bash
   git pull origin main
   ```
4. Switch Back to your Branch
   ```bash
   git checkout <branch-name>
   ```
5. Merge the latest changes from the main branch into your branch
   ```bash
   git merge main
   ```
## Pushing changes to your branch on GitHub
1. Make sure you are on YOUR branch!
   ```bash
   git checkout <your-branch>
   ```
2. Stage your changes
   ```bash
   git add .
   ```
3. Commit your changes
   ```bash
   git commit -m "Enter-Message-Here"
   ```
4. Push to YOUR branch
   ```bash
   git push origin <Your-Branch>
   ```
