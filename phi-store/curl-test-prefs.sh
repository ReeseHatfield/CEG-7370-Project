#!/bin/bash

PREFS="I only like vegan food"

curl -X POST http://localhost:3001/rec \
  -H "Content-Type: application/json" \
  -d "{\"preferences\": \"$PREFS\"}"
