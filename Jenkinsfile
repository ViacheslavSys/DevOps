pipeline {
    agent any

    stages {
        stage('Check System') {
            steps {
                script {
                    def scriptText = readFile 'checkSystem.groovy'
                    evaluate(scriptText)
                }
            }
        }
    }
}
