pipeline{
    agent any
    environment{
        NEW_VERSION = '1.3.0'
    }
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
            when{
                expression{
                    BRANCH_NAME == 'dev'
                }
            }
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