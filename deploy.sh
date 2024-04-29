#!/bin/zsh

npm run build
rsync -avz -e "ssh -i ~/.ssh/JobScraperKey.pem" ./dist/ ec2-user@serverweb-host:/var/www/jobs/
