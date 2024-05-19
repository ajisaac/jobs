#!/bin/zsh

rsync -avz ./job-scraper.service aaron@job-scraper.homelab.io:~/job-scraper.service
ssh aaron@job-scraper.homelab.io "sudo cp ~/job-scraper.service /etc/systemd/system/job-scraper.service"
ssh aaron@job-scraper.homelab.io "sudo systemctl stop job-scraper.service"
ssh aaron@job-scraper.homelab.io "sudo systemctl disable job-scraper.service"
ssh aaron@job-scraper.homelab.io "sudo systemctl start job-scraper.service"
ssh aaron@job-scraper.homelab.io "sudo systemctl enable job-scraper.service"
ssh aaron@job-scraper.homelab.io "sudo systemctl daemon-reload"

