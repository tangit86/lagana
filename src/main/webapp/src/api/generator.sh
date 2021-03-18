wget http://localhost:3333/v3/api-docs api-schema.json
openapi-generator-cli generate -i api-schema.json -g openapi-yaml -o ./gen
openapi-generator-cli generate -i ./gen/openapi/openapi.yaml -g typescript-axios -o ./gen
sed -i 's/delete localVarUrlObj.search;//' ./gen/api.ts