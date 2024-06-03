#!/bin/zsh

rsync -avz -e "ssh -i ~/.ssh/JobScraperKey.pem" ./jobs.service ubuntu@server-host:~/job-scraper/jobs.service
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo cp ~/job-scraper/jobs.service /etc/systemd/system/jobs.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl stop jobs.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl disable jobs.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl start jobs.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl enable jobs.service"
ssh -i ~/.ssh/JobScraperKey.pem ubuntu@server-host "sudo systemctl daemon-reload"

