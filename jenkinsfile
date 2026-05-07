pipeline {
    agent any
    
    tools {
        // Ensure this name matches exactly what you set in "Global Tool Configuration"
        maven 'maven-3.9.9' 
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Harshitsharma201/Selenium-Test-Framework.git'
            }
        }
        
        stage('Build & Test') {
            steps {
                // 'install' already includes 'test'. Using -DskipTests=false ensures they run.
                bat 'mvn clean install'
            }
        }
        
        stage('Reports') {
            steps {
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkName: true,
                    keepAll: true,
                    reportDir: 'src/test/resources/ExtentReport',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Spark Report'
                ])
            }
        }
    }

    // The post block MUST be inside the pipeline { }
    post {
        always {
            // Added **/target/surefire-reports/*.xml (fixed the double 'tt' typo in your version)
            archiveArtifacts artifacts: 'src/test/resources/ExtentReport/*.html', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }
        
        success {
            emailext (
                subject: "SUCCESS: Build # ${env.BUILD_NUMBER} - ${env.JOB_NAME}",
                body: """<p>Automation build passed successfully!</p>
                         <p>View the report here: ${env.JOB_URL}Extent_20Spark_20Report/</p>""",
                to: 'devnulldevil@gmail.com',
                attachmentsPattern: 'src/test/resources/ExtentReport/ExtentReport.html'
            )
        }
        
        failure {
            emailext (
                subject: "FAILURE: Build # ${env.BUILD_NUMBER} - ${env.JOB_NAME}",
                body: """<p>The automation suite failed.</p>
                         <p>Check the console logs here: ${env.BUILD_URL}console</p>""",
                to: 'devnulldevil@gmail.com',
                attachmentsPattern: 'src/test/resources/ExtentReport/ExtentReport.html'
            )
        }
    }
} // Final closing bracket for pipeline