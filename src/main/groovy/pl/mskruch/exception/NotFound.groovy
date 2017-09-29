package pl.mskruch.exception

class NotFound extends RuntimeException {
    def final name
    def final id

    NotFound(String name, Long id) {
        super("$name with id $id not found")
        this.name = name
        this.id = id
    }
}
