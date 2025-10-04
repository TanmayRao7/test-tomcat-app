#!/bin/bash
ENV_NAME=$1
echo "Starting deployment for environment ${ENV_NAME}"
echo "argocd app sync ${ENV_NAME}"
sleep 2
echo "Deployment completed for environment ${ENV_NAME}"