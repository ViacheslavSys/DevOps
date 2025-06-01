// checkSystem.groovy
def log = { msg -> println "[${new Date().format("yyyy-MM-dd HH:mm:ss")}] $msg" }

File.listRoots().each { root ->
    def total = root.totalSpace
    def free = root.freeSpace
    def used = total - free
    log "Раздел: ${root.absolutePath}"
    log "  Всего: ${(total / 1e9).round(2)} GB"
    log "  Использовано: ${(used / 1e9).round(2)} GB"
    log "  Свободно: ${(free / 1e9).round(2)} GB"
}

log "Поиск процесса с максимальным потреблением памяти..."

def isWindows = System.properties['os.name'].toLowerCase().contains('windows')

if (isWindows) {
    def proc = ['cmd', '/c', 'wmic process get Name,ProcessId,WorkingSetSize /format:csv'].execute()
    def lines = proc.text.readLines().findAll { it && !it.startsWith("Node") && it.contains(",") }

    def topProcess = lines
        .collect { line ->
            def parts = line.split(',')
            def name = parts[1]
            def pid = parts[2]
            def mem = parts[3]?.toLong() ?: 0L
            [name: name, pid: pid, mem: mem]
        }
        .max { it.mem }

    if (topProcess) {
        log "Имя: ${topProcess.name}, PID: ${topProcess.pid}, RAM: ${(topProcess.mem / 1024 / 1024).round(2)} MB"
    }

} else {
    def proc = ['bash', '-c', 'ps -eo pid,comm,rss --sort=-rss | head -n 2'].execute()
    def lines = proc.text.readLines()
    if (lines.size() > 1) {
        def parts = lines[1].trim().split(/\s+/, 3)
        log "Имя: ${parts[1]}, PID: ${parts[0]}, RAM: ${(parts[2].toInteger() / 1024).round(2)} MB"
    }
}
