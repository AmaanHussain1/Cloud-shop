pipeline {
    // This tells Jenkins it can run on any available server agent
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                // For local test, it just grabs the workspace context
                checkout scm
            }
        }

        stage('Build Product Service') {
            steps {
                // The 'dir' command changes the directory just like 'cd' in a terminal
                dir('product-service') {
                    // 'sh' executes a shell command on the Linux agent
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Build Order Service') {
            steps {
                dir('order-service') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
    }

    post {
        // This block runs after all stages finish (pass or fail)
        always {
            echo 'Pipeline has finished executing.'
        }
        success {
            echo 'SUCCESS! All microservices compiled perfectly.'
        }
        failure {
            echo 'FAILURE! Check the build logs.'
        }
    }
}