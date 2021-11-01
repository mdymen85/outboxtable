# outboxtable

sudo docker run --name outboxtable-db -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mdymen_pass -d mysql:latest

sudo docker exec -i outboxtable-db sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < src/main/resources/data.sql

