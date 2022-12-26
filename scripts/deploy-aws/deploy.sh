#!/bin/bash
echo "> 현재 시간: $(date)" >> /home/ubuntu/action/deploy.log

echo "> 8080 PORT 에서 구동중인 컨테이너 id 확인"
CONTAINER_ID=$(sudo docker container ls -a -f "name=hitch-hiker" -q)

if [ -z $CONTAINER_ID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/action/deploy.log
else
  echo "> docker stop $CONTAINER_ID" >> /home/ubuntu/action/deploy.log
  sudo docker stop $CONTAINER_ID
  echo "> docker stop $CONTAINER_ID" >> /home/ubuntu/action/deploy.log
  sudo docker rm $CONTAINER_ID
  sleep 5
fi

echo "> Docker 이미지 내려받기"    >> /home/ubuntu/action/deploy.log
sudo docker pull llems/hitch-hiker-server:0.1
echo "> Docker 컨테이너 배포"    >> /home/ubuntu/action/deploy.log
sudo docker run --name hitch-hiker -d -e active=prod -p 8080:8080 llems/hitch-hiker-server:0.1
#nohup java -jar $DEPLOY_JAR >> /home/ubuntu/deploy.log 2>/home/ubuntu/action/deploy_err.log &