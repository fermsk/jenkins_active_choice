import jenkins.model.Jenkins
import hudson.plugins.git.GitSCM
import hudson.plugins.git.BranchSpec

//def JOB_BRANCH = '2.1.2'
def JOB_NAME = 'osinit/aeca-certificate-authority/osinit-mirror%2Faeca%2Fcertificate-authority%2Faeca-ca'
def folder = Jenkins.instance.getItemByFullName("${JOB_NAME}")
def jobs = folder.getAllItems(hudson.model.Job.class)
def builds = []
jobs.each {
    it.getBuilds().each { build ->
        builds += "\"${build}\""
    }
}
def result = [:]
builds.each { job ->
    def matcher = job =~ /.*?\/([^\/]+)\s+#(\d+)/
    if (matcher) {
        def key = matcher[0][1]
        def value = matcher[0][2].toInteger()

        if (result.containsKey(key)) {
            result[key] << value
        } else {
            result[key] = [value]
        }
    }
}
result = result.collectEntries { k, v -> [k, v.sort { a, b -> b <=> a }] }
def branchNames = new ArrayList<String>(result.keySet())
//return result
def getBuildsForBranch = { branchName ->
    return result[branchName] ?: []
}
//def selectedBranch = params.${JOB_BRANCH}
def buildsForSelectedBranch = getBuildsForBranch(JOB_BRANCH)
//return "${buildsForSelectedBranch}"
//return ["$JOB_BRANCH"]