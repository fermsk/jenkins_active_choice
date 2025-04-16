import jenkins.model.Jenkins
import hudson.plugins.git.GitSCM
import hudson.plugins.git.BranchSpec

//def JOB_BRANCH = '2.1.2'
//def BUILDS = params.BUILDS
def JOB_NAME = 'osinit/aeca-certificate-authority/osinit-mirror%2Faeca%2Fcertificate-authority%2Faeca-ca'
def folder = Jenkins.instance.getItemByFullName("${JOB_NAME}")
def jobs = folder.getAllItems(hudson.model.Job.class)
def builds = []

jobs.each { job ->
    job.getBuilds().each { build ->
        builds += [fullName: "${build}", displayName: build.displayName, number: build.number]
    }
}

def result = [:]
builds.each { buildInfo ->
    def matcher = buildInfo.fullName =~ /.*?\/([^\/]+)\s+#(\d+)/
    if (matcher) {
        def key = matcher[0][1]
        def value = [displayName: buildInfo.displayName.replaceAll('#', ''), number: buildInfo.number]

        if (result.containsKey(key)) {
            result[key] << value
        } else {
            result[key] = [value]
        }
    }
}
result = result.collectEntries { k, v ->
    [k, v.sort { a, b -> b.number <=> a.number }]
}

def getBuildsForBranch = { branchName ->
    return result[branchName] ?: []
}

def buildsForSelectedBranch = getBuildsForBranch(JOB_BRANCH)

def getBuildNumberFromDisplayName = { displayName ->
    def build = buildsForSelectedBranch.find { it.displayName == displayName }
    return build ? build.number : null
}

def chosenDisplayName = '791'
def correspondingBuildNumber = getBuildNumberFromDisplayName(chosenDisplayName)
def buildNumberParam = correspondingBuildNumber
//return BUILDS
return [buildNumberParam]
