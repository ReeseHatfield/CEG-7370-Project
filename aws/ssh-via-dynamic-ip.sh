#!/bin/bash

KEY_PATH="$HOME/.ssh/dist-comp-aws.pem"

USER="ec2-user"

read -p "Public EC2 instance IP: " PUBLIC_IP

if [ -z "$PUBLIC_IP" ]; then
    echo "No IP entered. Exiting."
    exit 1
fi

ssh -i "$KEY_PATH" "$USER@$PUBLIC_IP"
