#!/bin/bash
# Check if there are 2 parameters. If yes, take these as neo4j username and password
if [ $# -eq 2 ]; then
  echo "Neo4j username and password taken from given parameters."
  USERNAME=$1
  PASSWORD=$2
else
  echo "Neo4j username and password parsed from neo4j-access secret."
  SECRET=$(kubectl -n persistence get secrets neo4j-access -o yaml)
  HASHED_USERNAME=$(echo "$SECRET" | awk -F'[[:blank:],]' '/username/ {print $4}')
  HASHED_PASSWORD=$(echo "$SECRET" | awk -F'[[:blank:],]' '/password/ {print $4}')
  USERNAME=$(echo "$HASHED_USERNAME" | base64 -D)
  PASSWORD=$(echo "$HASHED_PASSWORD" | base64 -D)
fi

IMPORT_FILE=queries/neo4j-import-queries.cypher
echo "Start import from $IMPORT_FILE"
kubectl -n persistence exec --stdin --tty neo4j-neo4j-core-0 \
  -- bash -c "cat $IMPORT_FILE | bin/cypher-shell -u $USERNAME -p $PASSWORD --format plain"
echo "Finished import"
