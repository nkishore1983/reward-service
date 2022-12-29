pipeline{
    agent any
    stages{
        stage('Compile Stage'){
            steps{
                withMaven(maven: 'maven-for-jenkins'){
                    echo "compiling version ${NEW_VERSION}"
                    sh 'mvn clean compile'
                }
            }
        }
        stage('Testing Stage'){
            steps{
                withMaven(maven: 'maven-for-jenkins'){
                    sh 'mvn test'
                }
            }
        }
        stage('Deployment Stage'){
            steps{
                withMaven(maven: 'maven-for-jenkins'){
                    sh 'mvn deploy'
                }
            }
        }
    }
    post{
        always{
            echo 'All stages are completed'
        }
        failure{
            echo 'Failure while deploying the pipeline'
        }
        success{
            echo 'Deployment successful'
        }
    }
}