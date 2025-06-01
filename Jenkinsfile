pipeline {
    agent any

    stages {
        stage('Disk Info') {
            steps {
                echo 'Состояние дисков:'
                sh 'df -h'
            }
        }

        stage('Top Memory Process') {
            steps {
                echo 'Процесс, занимающий наибольший объем памяти:'
                sh '''
                    ps -eo pid,comm,%mem --sort=-%mem | head -n 2
                '''
            }
        }
    }
}