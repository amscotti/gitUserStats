import com.mitchtalmadge.asciidata.table.ASCIITable
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters

import java.text.SimpleDateFormat
import java.util.concurrent.Callable

class UserCommit {
    String email
    Integer commits
    Date firstCommit
    Date lastCommit
}

@Command(name = "User Commit", mixinStandardHelpOptions = true, version = "User Commit 1.0",
        description = "Reads the commit logs from a Git Repository and displays based on users interactions")
class App implements Callable<Integer> {
    @Parameters(index = "0", paramLabel = "REPOSITORY", description = "the repository path")
    File repository

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd")

    private List<UserCommit> getCommits() {
        def existingRepo = new FileRepositoryBuilder()
                .setGitDir(new File(repository, ".git"))
                .build()

        Git git = new Git(existingRepo)
        Iterable<RevCommit> commits = git.log().all().call()

        commits
                .groupBy { it.authorIdent.emailAddress }
                .collect { email, commitList ->
                    List<Date> dates = commitList.collect { it.authorIdent.when }.sort()
                    new UserCommit(
                            email: email,
                            commits: commitList.size(),
                            firstCommit: dates.first(),
                            lastCommit: dates.last())
                }
                .sort { it.firstCommit }
    }

    private String[][] formatCommits(List<UserCommit> commits) {
        commits
                .collect {
                    [it.email, it.commits, DATE_FORMAT.format(it.firstCommit), DATE_FORMAT.format(it.lastCommit)] as String[]
                } as String[][]
    }

    private void printStatus(List<UserCommit> commits) {
        int totalCommits = commits.collect { it.commits }.sum(0) as int
        String firstCommit = DATE_FORMAT.format(commits.first().firstCommit)
        String lastCommit = DATE_FORMAT.format(commits.last().lastCommit)

        println("${commits.size()} users with ${totalCommits} commits, from ${firstCommit} to ${lastCommit}.")
    }

    static private void printTable(String[][] data) {
        String[] headers = ["USER", "COMMITS", "FIRST COMMIT", "LAST COMMIT"]
        println(ASCIITable.fromData(headers, data).toString())
    }

    static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args)
        System.exit(exitCode)
    }

    @Override
    Integer call() throws Exception {
        if (!repository.exists() || !repository.isDirectory() || !new File(repository, ".git").exists()) {
            println("Unable to find Git Repository at the path ${repository.toString()}")
            return -1
        }

        def commits = getCommits()
        printStatus(commits)

        String[][] formattedCommits = formatCommits(commits)
        printTable(formattedCommits)
        return 0
    }
}