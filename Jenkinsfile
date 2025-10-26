pipeline {
    agent any
    
    parameters {
        choice(name: 'BRANCH', choices: ['main', 'develop', 'staging', 'feature'], description: 'Select branch to build')
    }
    
    tools {
        maven "maven"
    }
    
    stages {
        stage('Clone') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${params.BRANCH}"]],
                        extensions: [
                            [$class: 'CloneOption', depth: 0, noTags: false, shallow: false],
                            [$class: 'LocalBranch', localBranch: "${params.BRANCH}"]
                        ],
                        userRemoteConfigs: [[
                            url: 'https://github.com/TanmayRao7/test-tomcat-app.git'
                        ]]
                    ])
                    
                    // Capture and display Git info
                    env.GIT_COMMIT_SHORT = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
                    env.GIT_COMMIT_MSG = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%s'").trim()
                    env.GIT_AUTHOR = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%an'").trim()
                    
                    echo "Branch: ${params.BRANCH}"
                    echo "Commit: ${env.GIT_COMMIT_SHORT}"
                    echo "Message: ${env.GIT_COMMIT_MSG}"
                    echo "Author: ${env.GIT_AUTHOR}"
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Update Artifact') {
            steps {
                sh '''
                    ls -lhtr ./target/*.war
                    mv ./target/*.war ./app.war
                '''
            }
        }
        stage('Docker Build') {
            steps {
                sh '''
                    echo "Docker Build is in progress"
                    sleep 2
                    echo "Docker Build completed"
                '''
            }
        }
        stage('Docker Push') {
            steps {
                sh '''
                    echo "Docker Push is in progress"
                    sleep 2
                    echo "Docker Push completed"
                '''
            }
        }
        stage('Deployments') {
            parallel {
                stage('Deploy To Dev') {
                    stages {
                        stage('Approve Dev') {
                            options {
                                timeout(time: 1, unit: 'MINUTES')
                            }
                            steps {
                                script {
                                    try {
                                        input message: 'Deploy to Dev?', ok: 'Deploy', submitter: 'adminm'
                                        env.DEPLOY_DEV = 'true'
                                    } catch (Exception e) {
                                        echo "Dev approval timed out. Skipping Dev deployment."
                                        env.DEPLOY_DEV = 'false'
                                    }
                                }
                            }
                        }
                        stage('Dev Deployment') {
                            when {
                                expression { env.DEPLOY_DEV == 'true' }
                            }
                            steps {
                                sh '''
                                    echo "Dev: Updating Image tag is in progress"
                                    sleep 2
                                    echo "Dev: Updating Image tag is completed"
                                '''
                            }
                        }
                    }
                }
                stage('Deploy To QA') {
                    stages {
                        stage('Approve QA') {
                            options {
                                timeout(time: 1, unit: 'MINUTES')
                            }
                            steps {
                                script {
                                    try {
                                        input message: 'Deploy to QA?', ok: 'Deploy', submitter: 'admin'
                                        env.DEPLOY_QA = 'true'
                                    } catch (Exception e) {
                                        echo "QA approval timed out. Skipping QA deployment."
                                        env.DEPLOY_QA = 'false'
                                    }
                                }
                            }
                        }
                        stage('QA Deployment') {
                            when {
                                expression { env.DEPLOY_QA == 'true' }
                            }
                            steps {
                                sh '''
                                    echo "QA: Updating Image tag is in progress"
                                    sleep 2
                                    echo "QA: Updating Image tag is completed"
                                '''
                            }
                        }
                    }
                }
                stage('Deploy To Prod') {
                    stages {
                        stage('Approve Prod') {
                            options {
                                timeout(time: 1, unit: 'MINUTES')
                            }
                            steps {
                                script {
                                    try {
                                        input message: 'Deploy to Production?', ok: 'Deploy to Prod', submitter: 'admin'
                                        env.DEPLOY_PROD = 'true'
                                    } catch (Exception e) {
                                        echo "Prod approval timed out. Skipping Prod deployment."
                                        env.DEPLOY_PROD = 'false'
                                    }
                                }
                            }
                        }
                        stage('Prod Deployment') {
                            when {
                                expression { env.DEPLOY_PROD == 'true' }
                            }
                            steps {
                                sh '''
                                    echo "Prod: Updating Image tag is in progress"
                                    sleep 2
                                    echo "Prod: Updating Image tag is completed"
                                '''
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
