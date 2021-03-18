cd ./src/main/webapp && npm run build && cd ../../../
mvn clean install
cd ./target
mkdir run/
cp *.jar run/app.jar