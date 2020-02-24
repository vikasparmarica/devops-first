az group deployment create \
  --name ACIDeployment \
  --resource-group aa96dazwergr001 \
  --template-file template.json \
  --parameters parameters.json