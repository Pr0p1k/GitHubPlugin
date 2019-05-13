import com.intellij.ide.browsers.BrowserLauncherAppless
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.ui.Messages
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Paths

class OpenRepoAction : AnAction("Open repository") {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val path = project?.basePath
        if (path == null)
            Messages.showMessageDialog(
                project,
                "The project is default, so it's directory can't be accessed",
                "Error",
                Messages.getErrorIcon()
            )
        else {
            val gitConfig = Paths.get(path, ".git", "config").toFile()
            val reader = BufferedReader(InputStreamReader(FileInputStream(gitConfig)))
            val urls = arrayListOf<String>()
            reader.lines().forEach {
                if (it.contains("url")) {
                    urls.add(it.substringAfter("url = ")) // TODO get urls after encountering the "[remote" line
                }
            }
            when (urls.size) {
                1 -> {
                    val launcher = BrowserLauncherAppless()
                    launcher.open(urls[0])
                }
                0 -> Messages.showMessageDialog(
                    project,
                    "No remote specified",
                    "Error",
                    Messages.getErrorIcon()
                )
                else -> {
                    // TODO open the list in dialog
                }
            }
        }
    }
}
