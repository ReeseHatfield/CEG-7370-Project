#!/bin/bash

PREFS="I only like vegan food"

curl -X POST http://localhost:3002/rec \
  -H "Content-Type: application/json" \
  -d "{\"preferences\": \"$PREFS\"}"
