pipeline {
    agent any

    parameters {
        choice(
            name: 'OS',
            choices: ['linux', 'darwin', 'windows'],
            description: 'Target operating system'
        )
        choice(
            name: 'ARCH',
            choices: ['amd64', 'arm64'],
            description: 'Target architecture'
        )
    }

    environment {
        REGISTRY = "ghcr.io/inezhinskiy"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Lint') {
            steps {
                sh 'make lint'
            }
        }

        stage('Test') {
            steps {
                sh 'make test'
            }
        }

        stage('Build binary') {
            steps {
                sh "make build TARGETOS=${params.OS} TARGETARCH=${params.ARCH}"
            }
        }

        stage('Build & Push image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'ghcr-credentials', usernameVariable: 'GHCR_USER', passwordVariable: 'GHCR_TOKEN')]) {
                    sh '''
                        echo "$GHCR_TOKEN" | docker login ghcr.io -u "$GHCR_USER" --password-stdin
                    '''
                    sh "make image push REGISTRY=${REGISTRY} TARGETOS=${params.OS} TARGETARCH=${params.ARCH}"
                }
            }
        }
    }
}